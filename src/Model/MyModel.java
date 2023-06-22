package Model;

import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.util.Pair;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyModel implements IModel{

    private static int playersRow = 0;
    private static int playersCol = 0;
    private static Server generateMaze;
    private static Server solveMaze;
    private static Maze maze ;
    private static MyModel instance;

    public static MyModel getInstance() {
       if(instance == null)
           instance = new MyModel();
        return instance;
    }

    private MyModel()
    {


        maze = null;
        generateMaze = new Server(5400,1000,new ServerStrategyGenerateMaze());
        solveMaze = new Server(5401,1000,new ServerStrategySolveSearchProblem());
        generateMaze.start();
        solveMaze.start();
    }
    public  int getPlayerCol() {
        return playersCol;
    }
    public  int getPlayerRow() {
        return playersRow;
    }
    public  void setPlayerRow(int row) {
        playersRow = row;
    }
    public  void setPlayerCol(int col) {
        playersCol = col;
    }

    public  int getGoalRow() {
        return maze.getGoalPosition().getRowIndex();

    }

    public  int getGoalCol() {
        return maze.getGoalPosition().getColumnIndex();
    }

    public List<Pair<Integer, Integer>> getSolution(){
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), 5401);
            InputStream inFromServer;
            OutputStream outToServer;

            inFromServer = socket.getInputStream();
            outToServer = socket.getOutputStream();

            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);

            toServer.writeObject(maze); //send maze to server toServer.flush();
            Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with
            ArrayList<Pair<Integer,Integer>> pairs = new ArrayList<>();
            ArrayList<int[]> locations = mazeSolution.getSolutionLocations();
            for(int i = 0 ; i < locations.size(); i++){
                int[] curr = locations.get(i);
                Pair<Integer,Integer> step = new Pair<>(curr[0],curr[1]);
                pairs.add(step);
            }
            return pairs;


//            byte[] mazeDimensions = new int[]{nRows, nCols};
//            toServer.writeObject(mazeDimensions); //send maze
//            toServer.flush();
//            byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with
//
//            InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
//            byte[] decompressedMaze = new byte[(nRows*nCols)+12]; //allocating byte[] for the decompressed
//            is.read(decompressedMaze); //Fill decompressedMazewith bytes

//            maze = new Maze(decompressedMaze);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
//        return maze.getMazeTable();

        return null;
    }

    public int[][] generateMaze(int nRows, int nCols) {
        // TODO: generateMaze
        // Example for now - random maze
        try {
            playersCol = 0;
            playersRow = 0;
            Socket socket = new Socket(InetAddress.getLocalHost(), 5400);
            InputStream inFromServer;
            OutputStream outToServer;

            inFromServer = socket.getInputStream();
            outToServer = socket.getOutputStream();

            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);

            int[] mazeDimensions = new int[]{nRows, nCols};
            toServer.writeObject(mazeDimensions); //send maze
            toServer.flush();
            byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with

            InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
            byte[] decompressedMaze = new byte[(nRows*nCols)+12]; //allocating byte[] for the decompressed
            is.read(decompressedMaze); //Fill decompressedMazewith bytes

            maze = new Maze(decompressedMaze);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return maze.getMazeTable();
    }

    public boolean isValidStep(int row,int col){
        try {
            return 0 == maze.getMazeTable()[row][col];
        } catch(IndexOutOfBoundsException e){
            return false;
        }
    }

    public boolean saveMaze(String filePath){
        try {
            // save maze to a file
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(filePath));
            out.write(maze.toByteArray());
            out.flush();
            out.close();
        } catch (IOException e) {
//            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int[][] loadMaze(String filePath){

        byte[] savedMazeBytes = new byte[0];
        try {

            //read maze from file
            InputStream in = new MyDecompressorInputStream(new FileInputStream(filePath));
            savedMazeBytes = in.readAllBytes();
//            System.out.println("size = " + savedMazeBytes.length);
            in.read(savedMazeBytes);
            in.close();
            maze = new Maze(savedMazeBytes);
            setPlayerCol(0);
            setPlayerRow(0);
            return maze.getMazeTable();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void closeAll(){
        generateMaze.stop();
        solveMaze.stop();
        System.exit(0);
    }

}