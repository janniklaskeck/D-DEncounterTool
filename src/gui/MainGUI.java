package gui;

import java.util.ArrayList;

import encounter.Encounter;
import entity.Creature;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainGUI extends Application {

    public static final String IMAGEFOLDER = System.getProperty("user.dir") + "\\images\\";

    public static ArrayList<Creature> creatureList;
    public static Encounter encounter;
    public static Stage mainStage;

    private static Thread autoSaveThread;
    private static final int AUTOSAVEINTERVAL = 60000;
    private static boolean autosaveThread = true;
    public static boolean autosave = false;

    public static void main(String[] args) {
	creatureList = new ArrayList<Creature>();
	launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
	Parent root = FXMLLoader.load(getClass().getResource("gui.fxml"));
	Scene scene = new Scene(root, 1280, 720);
	scene.getStylesheets().add(getClass().getResource("customstyle.css").toString());

	mainStage = primaryStage;

	autoSaveThread = new Thread(new Runnable() {
	    @Override
	    public void run() {
		while (autosaveThread) {
		    while (autosave) {
			if (encounter.getCreatureList().size() > 0) {
			    encounter.autoSave();
			    System.out.println("AutoSave Complete");
			}
		    }
		    try {
			Thread.sleep(AUTOSAVEINTERVAL);
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		}
	    }
	});
	autoSaveThread.setDaemon(true);
	autoSaveThread.start();

	primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	    @Override
	    public void handle(WindowEvent event) {
		autosaveThread = false;
	    }
	});
	primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
	primaryStage.setTitle("D&D Encounter Tool v2.0");
	primaryStage.setScene(scene);
	primaryStage.show();
    }
}
