package View;

import Server.Configurations;
import ViewModel.MyViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MyViewController {
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public ImageView winningImageView;
    public Button btnShowSolution;
    private MyViewModel myViewModel;
    private boolean hasWon = false;
    private boolean wasMazeCreatedBefore = false;

    public void setScene(Scene scene) {

        Stage stage = (Stage) scene.getWindow();

        // set listeners to size changes
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            // Adjust the element's size based on the new window width
            mazeDisplayer.updateCanvasWidth((double) oldValue, (double) newValue);
        });

        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            // Adjust the element's size based on the new window height
            mazeDisplayer.updateCanvasHeight((double) oldValue, (double) newValue);
        });
        stage.setOnCloseRequest(event -> System.exit(0));
        stage.setScene(scene);
        stage.show();

    }

    public void onClickAbout(ActionEvent actionEvent){
        Platform.runLater(() -> {
            String message = "The exclusive creators of this game are Yael and Raanan.\n" +
                    "Their IDs are 315108043 and 313558967.\n\n" +
                    "The game was inspired by Formula 1 (we are die-hard fans!).\n" +
                    "All rights to the assignment are reserved to the faculty" +
                    " of the \"Advanced topics in programming\" course.";

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About Us");
            alert.setHeaderText(null);
            alert.setContentText(message);

            // Set the alert to have only the "OK" button
            alert.getButtonTypes().setAll(ButtonType.OK);

            // Show the alert and capture the result
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // OK button is clicked
                System.out.println("OK clicked.");
            }
        });
    }

    public void onClickHelp(ActionEvent actionEvent){
        Platform.runLater(() -> {
            String message = "Welcome to F1 maze! \n\n" +
                    "to start playing generate new maze or load an existing one.\n\n" +
                    "You will start at the top left corner and your goal will be the checkers flag.\n\n" +
                    "The movement controls are the keyboard's arrows and the num pad keys.\n\n" +
                    "To save the current maze you go to file -> save.\n\n" +
                    "Good Luck in the race!.";

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About Us");
            alert.setHeaderText(null);
            alert.setContentText(message);

            // Increase the size of the dialog window
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setMinWidth(600);
            dialogPane.setMinHeight(250);

            // Set the alert to have only the "OK" button
            alert.getButtonTypes().setAll(ButtonType.OK);

            // Show the alert and capture the result
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // OK button is clicked
                System.out.println("OK clicked.");
            }
        });
    }

    public void onClickExit(ActionEvent actionEvent){
        Platform.runLater(() -> {
            String message = "Do you want to Exit?";

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText(message);

            // Set the alert to have "Yes" and "No" buttons
            ButtonType buttonYes = new ButtonType("Yes");
            ButtonType buttonNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonYes, buttonNo);

            // Show the alert and capture the result
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonYes) {
//                saveFile(actionEvent);
                MyViewModel.closeAll();
//                System.out.println("Yes clicked.");
            }

        });
    }

    public void onClickProperties(ActionEvent actionEvent){
        Platform.runLater(() -> {
            String message = null;
            try {
//                String name = Configurations.getInstance().getMazeGenerator().getClass().getName();
                message = "The Properties of the maze: \n" +
                        "   1. Maze Generating Algorithm = " + Configurations.getInstance().getMazeGenerator().getClass().getName()+"\n"
                +"  2. Maze Solving Algorithm =      " + Configurations.getInstance().getMazeSearchingAlgorithm().getName()+"\n"+
                "   3. ThreadPool Size = " + Configurations.getInstance().getThreadPoolSize();


            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About Us");
            alert.setHeaderText(null);
            alert.setContentText(message);

            // Set the alert to have only the "OK" button
            alert.getButtonTypes().setAll(ButtonType.OK);

            // Show the alert and capture the result
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // OK button is clicked
                System.out.println("OK clicked.");
            }
        });
    }

    public void onClickGenerateMaze(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        // Play the sound

        winningImageView.setVisible(false);
        // Check if the source is a Node
        if (source instanceof Node) {
            Node node = (Node) source;
            Scene scene = node.getScene();
            setScene(scene);

            if(!wasMazeCreatedBefore)
            {
                scene.addEventHandler(KeyEvent.KEY_PRESSED, this::keyPressed);
                wasMazeCreatedBefore = true;
            }
            // Get the root node of the scene
            Parent fxmlRoot = scene.getRoot();

            textField_mazeColumns = (TextField) fxmlRoot.lookup("#textField_mazeColumns");
            textField_mazeRows = (TextField) fxmlRoot.lookup("#textField_mazeRows");
            // Check if the input is valid
            int nRows = InputValidity.isPositiveNumber(textField_mazeColumns.getText());
            int nCols = InputValidity.isPositiveNumber(textField_mazeRows.getText());
            if(nRows == -1 || nCols == -1)
            {
                // alert message
                String message = "One of the values is not a positive number.\n Fix it and try again :(";
                return;
            }else if(nRows < 2 || nCols < 2)
            {
                // alert message
                String message = "One of the values is not a positive number.\n Fix it and try again :(";
                return;
            }


            // Generate Maze - using the model view

            int[][] maze = MyViewModel.generateMaze(nRows, nCols);

            mazeDisplayer.drawMaze(maze);
            myViewModel.isMoveValid(0, 0);

            // Make the show solution button clickable
            btnShowSolution.setDisable(false);
            hasWon = false;
        }
    }

    public void openFile(ActionEvent actionEvent) {
        winningImageView.setVisible(false);
        // Check if the source is a Node

        MenuItem menuItem = (MenuItem) actionEvent.getSource();
        Scene scene = menuItem.getParentPopup().getOwnerWindow().getScene();
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showOpenDialog(null);
        if(chosen != null) {
            hasWon = false;
            String filePath = chosen.getAbsolutePath();
            System.out.println(filePath);
            int[][] maze = myViewModel.loadMaze(filePath);
            mazeDisplayer.drawMaze(maze);
            myViewModel.isMoveValid(0, 0);
            setScene(scene);

            btnShowSolution.setDisable(false);
            if(!wasMazeCreatedBefore)
            {
                scene.addEventHandler(KeyEvent.KEY_PRESSED, this::keyPressed);
                wasMazeCreatedBefore = true;
            }
        }
    }

    public void saveFile(ActionEvent actionEvent){

        Platform.runLater(() -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Save File");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter the file name:");

            // Show the dialog and capture the result
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String fileName = result.get();

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Maze");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
                fileChooser.setInitialDirectory(new File("./resources"));
                fileChooser.setInitialFileName(fileName);

                File file = fileChooser.showSaveDialog(null);
                myViewModel.savedMaze(file.getAbsolutePath());
//                if (file != null) {
//                    // Perform saving logic with the chosen file
//                    System.out.println("Saving file: " + file.getAbsolutePath());
//                } else {
//                    // User clicked cancel
//                    System.out.println("Save canceled.");
//                }
//            } else {
//                // User clicked cancel
//                System.out.println("Save canceled.");
//            }
            }
        });
    }

    public void keyPressed(KeyEvent keyEvent) {
        if (!hasWon) {
            int row = MyViewModel.getPlayerRow();
            int col = MyViewModel.getPlayerCol();

            KeyCode keyCode = keyEvent.getCode();

            switch (keyCode) {
                case NUMPAD1 -> { // Diagonal: Bottom Left
                    row += 1;
                    col -= 1;
                }
                case NUMPAD2, DOWN -> row += 1; // Down
                case NUMPAD3 -> { // Diagonal: Bottom Right
                    row += 1;
                    col += 1;
                }
                case NUMPAD4, LEFT -> col -= 1; // Left
                case NUMPAD6, RIGHT -> col += 1; // Right
                case NUMPAD7 -> { // Diagonal: Top Left
                    row -= 1;
                    col -= 1;
                }
                case NUMPAD8, UP -> row -= 1; // Up
                case NUMPAD9 -> { // Diagonal: Top Right
                    row -= 1;
                    col += 1;
                }
            }

            tryToMove(row, col);
            keyEvent.consume();
        }
    }

    public void tryToMove(int row, int col){
        // Create a Media object with the sound file path
        Media sound = new Media(new File("./resources/sound/car.mp3").toURI().toString());

        // Create a MediaPlayer using the Media object
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.5);

        // Play the sound
        Thread thread = new Thread() {
            @Override
            public void run() {
                mediaPlayer.play();
                // Code to be executed in the new thread
            }
        };

        hasWon = mazeDisplayer.tryToMove(row, col);
        if(hasWon)
        {
            // Show the winning image
            Image newImage = new Image("images/WinCard.JPG");
            winningImageView.setImage(newImage);
            winningImageView.setFitWidth(180);
            winningImageView.setFitHeight(200);

            winningImageView.setVisible(true);
        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
        Node sourceNode = (Node) mouseEvent.getSource();
        Scene currentScene = sourceNode.getScene();

        // Access or manipulate the current scene as needed
        if (currentScene != null) {
            setScene(currentScene);
        }
    }

    public void showSolution(ActionEvent actionEvent) {
        mazeDisplayer.setShowSolution();
    }
}
