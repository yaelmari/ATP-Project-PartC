package View;

public class ViewConstants {
    public static final int START_WIDTH_OF_THE_SCENE = 620;
    public static final int START_HEIGHT_OF_THE_SCENE = 450;
    public static final double CANVAS_PROPORTION = 0.8;
    public static final double OPTION_WINDOW_WIDTH = 178.0;

    private static String imagePathPlayer = "./resources/MazeImages/right.jpg";
    private static String imagePathWall = "./resources/MazeImages/wall.png";

    public static String getImagePathPlayer()
    {
        return imagePathPlayer;
    }

    public static String getImagePathWall() {
        return imagePathWall;
    }
}
