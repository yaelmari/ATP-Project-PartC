package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    private MyViewController myViewController;
    private Media sound = new Media(new File("./resources/sound/theme.mp3").toURI().toString());

    // Create a MediaPlayer using the Media object
    private MediaPlayer mediaPlayer = new MediaPlayer(sound);

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MazeWindow.fxml"));
        Scene scene = new Scene(root, ViewConstants.START_WIDTH_OF_THE_SCENE, ViewConstants.START_HEIGHT_OF_THE_SCENE);
        //primaryStage.setFullScreen(true);
//        scene.setOnKeyPressed(null);
//        scene.removeEventHandler(KeyEvent.KEY_PRESSED, this::keyPressed);

        primaryStage.setTitle("F1 maze");
        primaryStage.setScene(scene);

//        Media sound = new Media(new File("./resources/sound/theme.mp3").toURI().toString());
//
//        // Create a MediaPlayer using the Media object
//        MediaPlayer mediaPlayer = new MediaPlayer(sound);

        // Set the cycle count to INDEFINITE for looping
        mediaPlayer.setCycleCount(60);


        mediaPlayer.setVolume(0.5);
        // Play the sound
        mediaPlayer.play();
        // Create a Media object with the sound file path

        primaryStage.show();

//        myViewController = new MyViewController();
//        myViewController.setScene(scene);
//        myViewController.setMenu();
//        myViewController.setInstructionScene();

    }

    public static void main(String[] args) {
        launch(args);
    }


}
