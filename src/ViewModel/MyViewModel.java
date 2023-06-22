package ViewModel;

import Model.MyModel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyViewModel {
    private static MyModel myModel;

    public MyViewModel()
    {
        myModel = MyModel.getInstance();
    }

    public static int getPlayerRow() {
        return MyModel.getInstance().getPlayerRow();
    }

    public static int getPlayerCol() {
        return MyModel.getInstance().getPlayerCol();
    }

    public static int getGoalRow() {
        return MyModel.getInstance().getGoalRow();
    }

    public static int getGoalCol() {
        return MyModel.getInstance().getGoalCol();
    }

    public static List<Pair<Integer, Integer>> getSolutionList() {
        // TODO: get solution from the model and convert it
        List<Pair<Integer, Integer>> solutionList = MyModel.getInstance().getSolution();
//        solutionList.add(new Pair<>(0,1));
//        solutionList.add(new Pair<>(1,1));
//        solutionList.add(new Pair<>(2,2));
//        solutionList.add(new Pair<>(3,3));
//        solutionList.add(new Pair<>(4,5));
//        solutionList.add(new Pair<>(6,5));
//        solutionList.add(new Pair<>(5,6));
        return solutionList;
    }

    public static int[][] generateMaze(int nRows, int nCols) {
        return MyModel.getInstance().generateMaze(nRows, nCols);
    }

    public static int isMoveValid(int row, int col) {
        // Check if the move is valid
        int direction = 0;
        if(MyModel.getInstance().isValidStep(row,col)) {
            if(col == getPlayerCol() && row == getPlayerRow() -1)
                direction  = 1; // up!
            else if (col == getPlayerCol() && row == getPlayerRow() + 1) {
                direction =  2;  // down!
            }else if (col == getPlayerCol() + 1) {
                direction = 3;  // right / right up / right down
            }else if (col == getPlayerCol() - 1) {
                direction = 4;  // left / left up / left down
            }
            MyModel.getInstance().setPlayerRow(row);
            MyModel.getInstance().setPlayerCol(col);
        }
        return direction;
    }


    public static int[][] loadMaze(String filePath){

        return MyModel.getInstance().loadMaze(filePath);

    }
    public static void closeAll(){
        MyModel.getInstance().closeAll();
    }

    public static boolean savedMaze(String filePath){return MyModel.getInstance().saveMaze(filePath);}
}