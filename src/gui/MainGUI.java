package gui;

import java.util.ArrayList;

import entity.Creature;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainGUI extends Application {

    public static final String IMAGEFOLDER = System.getProperty("user.dir") + "\\images\\";

    public static ArrayList<Creature> creatureList;
    public static Stage mainStage;

    public static void main(String[] args) {
	creatureList = new ArrayList<Creature>();
	launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
	Parent root = FXMLLoader.load(getClass().getResource("gui.fxml"));
	Scene scene = new Scene(root, 1280, 720);
	mainStage = primaryStage;

	primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
	primaryStage.setTitle("D&D Encounter Tool v2.0");
	primaryStage.setScene(scene);
	primaryStage.show();
    }
}
