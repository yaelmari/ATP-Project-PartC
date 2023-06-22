package View;

import ViewModel.MyViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class MazeDisplayer extends Canvas {
    private int[][] maze;
    private StringProperty imageFileNameWall = new SimpleStringProperty();
    private StringProperty imageFileNamePlayer = new SimpleStringProperty();
    private StringProperty imageFileNameGoal = new SimpleStringProperty();
    private StringProperty imageFileNameGoalAndPlayer = new SimpleStringProperty();
    private StringProperty imageFileNameSolution = new SimpleStringProperty();
    private boolean showSolution = false;


    private List<Pair<Integer, Integer>> solutionList;

    public boolean tryToMove(int row, int col) {
        int checked = MyViewModel.isMoveValid(row, col);
        if(checked!=0) {
            boolean hasWon = maze.length == row +1 && maze[0].length == col +1;
            drawAfterMovement(hasWon,checked);
            return hasWon;
        }
        Media sound = new Media(new File("./resources/sound/carBoom.mp3").toURI().toString());

        // Create a MediaPlayer using the Media object
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

        return false;
    }

    public void setShowSolution()
    {
        solutionList = MyViewModel.getSolutionList();
        showSolution = true;
        draw(false);
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public void setImageFileNameGoal(String imageFileNameWall) {
        this.imageFileNameGoal.set(imageFileNameWall);
    }

    public void setImageFileNameGoalAndPlayer(String imageFileNameWall) {
        this.imageFileNameGoalAndPlayer.set(imageFileNameWall);
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    public void setImageFileNameSolution(String imageFileNamePlayer) {
        this.imageFileNameSolution.set(imageFileNamePlayer);
    }
    public String getImageFileNameSolution() {
        return imageFileNameSolution.get();
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }
    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public String getImageFileNameGoalAndPlayer() {
        return imageFileNameGoalAndPlayer.get();
    }

    public String getImageFileNameGoal() {
        return imageFileNameGoal.get();
    }

    public String getImageGoalAndPlayerForPopup() {
        return "/images/MarioAndThePrincess.png";
    }

    public void drawMaze(int[][] maze) {
        this.maze = maze;
        showSolution = false;
        draw(false);
    }

    private double getCurrHeight()
    {
        Stage stage = (Stage) getScene().getWindow();
        double height = stage.getHeight();
        return height * ViewConstants.CANVAS_PROPORTION;
    }
    private double getCurrWidth()
    {
        Stage stage = (Stage) getScene().getWindow();
        double width = stage.getWidth();
        return width * ViewConstants.CANVAS_PROPORTION;
    }
    private double getCellWidth()
    {
        Stage stage = (Stage) getScene().getWindow();
        double width = stage.getWidth();
        int cols = maze[0].length;

        this.setWidth(width*ViewConstants.CANVAS_PROPORTION);

        double cellWidth = (width - ViewConstants.OPTION_WINDOW_WIDTH) * ViewConstants.CANVAS_PROPORTION / cols;
        return cellWidth;
    }

    private double getCellHeight()
    {
        Stage stage = (Stage) getScene().getWindow();
        double height = stage.getHeight();
        int rows = maze.length;

        this.setHeight(height*ViewConstants.CANVAS_PROPORTION);

        double cellWidth = height * ViewConstants.CANVAS_PROPORTION / rows;
        return cellWidth;
    }

    private void draw(boolean theUserWon) {
        if(maze != null){
            double canvasHeight = getHeight();
            double canvasWidth = getWidth() ;
            int rows = maze.length;
            int cols = maze[0].length;

            double cellHeight = getCellHeight();
            double cellWidth = getCellWidth();

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
//            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);

            graphicsContext.clearRect(0, 0, getCurrWidth(), getCurrHeight());

            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);

            int goalRow = MyViewModel.getGoalRow(), goalCol = MyViewModel.getGoalCol();

            if(showSolution)
            {
                drawMazeSolution();
            }

            if(theUserWon) {
                drawCharacter(graphicsContext, goalRow, goalCol, cellHeight*3, cellWidth*1.5, getImageFileNameGoalAndPlayer());
            }
            else
            {
                int playerRow = MyViewModel.getPlayerRow(), playerCol = MyViewModel.getPlayerCol();

                // Draw Goal
                drawCharacter(graphicsContext, goalRow, goalCol, cellHeight, cellWidth, getImageFileNameGoal());
                // Draw player
                drawCharacter(graphicsContext, playerRow, playerCol, cellHeight, cellWidth, getImageFileNamePlayer());
            }
        }
    }
    private void drawAfterMovement(boolean theUserWon, int position) {
        if(maze != null){

            String movement = "resources/images/up.png";
            switch(position){
                case 1:
                    movement = "resources/images/up.png";
                    break;
                case 2:
                    movement = "resources/images/down.png";
                    break;
                case 3:
                    movement = "resources/images/right.png";
                    break;
                case 4 :
                    movement = "resources/images/left.png";
                default:
                    break;

            }
            int rows = maze.length;
            int cols = maze[0].length;

            double cellHeight = getCellHeight();
            double cellWidth = getCellWidth();

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, getCurrWidth(), getCurrHeight());

            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);

            int goalRow = MyViewModel.getGoalRow(), goalCol = MyViewModel.getGoalCol();

            if(showSolution)
            {
                drawMazeSolution();
            }

            if(theUserWon) {
                drawCharacter(graphicsContext, goalRow, goalCol, cellHeight, cellWidth, getImageFileNameGoalAndPlayer());
//                setOnKeyPressed(null);
            }
            else
            {
                int playerRow = MyViewModel.getPlayerRow(), playerCol = MyViewModel.getPlayerCol();

                // Draw Goal
                drawCharacter(graphicsContext, goalRow, goalCol, cellHeight, cellWidth, getImageFileNameGoal());
                // Draw player
                drawCharacter(graphicsContext, playerRow, playerCol, cellHeight, cellWidth, movement);
            }


        }
    }

    private void drawMazeSolution() {
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        int rows = maze.length;
        int cols = maze[0].length;

        double cellHeight = getCellHeight();
        double cellWidth = getCellWidth();
        GraphicsContext graphicsContext = getGraphicsContext2D();

        Image solutionImage = null;
        try{
            solutionImage = new Image(new FileInputStream(getImageFileNameSolution()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no solution image file");
        }

        for (Pair<Integer, Integer> tuple : solutionList) {
            int row = tuple.getKey();
            int col = tuple.getValue();
            double currCol = col * cellWidth;
            double currRow = row * cellHeight;
            if(solutionImage == null)
                graphicsContext.fillRect(currCol, currRow, cellWidth, cellHeight);
            else
                graphicsContext.drawImage(solutionImage, currCol, currRow, cellWidth, cellHeight);
        }
    }

    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
//        graphicsContext.setFill(Color.RED);

        Image wallImage = null;
        Image roadImage = null;
        try{

            wallImage = new Image(new FileInputStream("resources/images/racewall.png"));
            roadImage = new Image(new FileInputStream("resources/images/roadImage.jpg"));

            //            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no wall image file");
        }

        double mazeWidth = cols * cellWidth;
        double mazeHeight = rows * cellHeight;

        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(3.0);
        graphicsContext.strokeRect(0, 0, mazeWidth, mazeHeight);


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double x = j * cellWidth;
                double y = i * cellHeight;
                if(maze[i][j] == 1){
                    //if it is a wall:
                    if(wallImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                }
                else{
                    if(roadImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(roadImage, x, y, cellWidth, cellHeight);
                }
//                if (i == 0 || i == maze.length - 1 || j == 0 || j == maze[i].length - 1) {
//                    double x = j * cellWidth;
//                    double y = i * cellHeight;

//                    graphicsContext.setStroke(Color.BLACK);
//                    graphicsContext.setLineWidth(10.0);
//                    graphicsContext.strokeRect(x, y, cellWidth, cellHeight);
//                }
            }
        }
    }

    private void drawCharacter(GraphicsContext graphicsContext, int row, double col, double cellHeight, double cellWidth, String imageFileName) {
        double x = col * cellWidth;
        double y = row * cellHeight;
        graphicsContext.setFill(Color.GREEN);

        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(imageFileName));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if(playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }

    public double calculateCanvasSize(double oldScreenValue, double newScreenValue, double oldCanvasSize) {
        double newCanvasSize = newScreenValue * oldCanvasSize / oldScreenValue;
        return newCanvasSize;
    }

    public void updateCanvasWidth(double oldScreenSize, double newScreenSize) {
        double newCanvasSize = calculateCanvasSize(oldScreenSize, newScreenSize, getWidth());
        setWidth(newCanvasSize);
        draw(false);
    }

    public void updateCanvasHeight(double oldScreenSize, double newScreenSize) {
        double newCanvasSize = calculateCanvasSize(oldScreenSize, newScreenSize, getHeight());
        setHeight(newCanvasSize);
        draw(false);
    }


}
