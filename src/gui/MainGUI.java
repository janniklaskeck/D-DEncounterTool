package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainGUI extends Application {

    public static void main(String[] args) {
	launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
	Parent root = FXMLLoader.load(getClass().getResource("gui.fxml"));
	Scene scene = new Scene(root, 1280, 720);
	primaryStage.setTitle("D&D Encounter Tool v2.0");
	primaryStage.setScene(scene);
	primaryStage.show();
    }
}
