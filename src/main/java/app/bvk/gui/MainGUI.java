package app.bvk.gui;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.encounter.Encounter;
import app.bvk.entity.Creature;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainGUI extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainGUI.class);

    public static final String IMAGEFOLDER = System.getProperty("user.dir") + "\\images\\";
    public static Image IMAGEICON;
    public static ZipFile imageZipFile;

    public static List<Creature> creatureList = new ArrayList<>();
    public static Encounter encounter = new Encounter("Unnamed Encounter");
    public static Stage mainStage;

    private static Thread autoSaveThread;
    private static final int AUTOSAVEINTERVAL = 60000;
    private static boolean autosaveThread = true;
    public static boolean autosave = false;

    public static void main(String[] args) {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
        try {
            Charset cp437 = Charset.forName("CP437");
            imageZipFile = new ZipFile(System.getProperty("user.dir") + "\\images.zip", cp437);
        } catch (IOException e) {
            LOGGER.error("ERROR while loading zip file", e);
            imageZipFile = null;
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        IMAGEICON = new Image(getClass().getResourceAsStream("icon.png"));
        Parent root = FXMLLoader.load(getClass().getResource("gui.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("customstyle.css").toString());

        mainStage = primaryStage;

        autoSaveThread = new Thread(() -> {
            while (autosaveThread) {
                while (autosave) {
                    if (!encounter.getCreatureList().isEmpty()) {
                        encounter.autoSave();
                        LOGGER.info("Autosave complete");
                    }
                }
                try {
                    Thread.sleep(AUTOSAVEINTERVAL);
                } catch (InterruptedException e) {
                    LOGGER.error("AutoSave Thread was interrupted", e);
                }
            }
        });
        autoSaveThread.setDaemon(true);
        autoSaveThread.start();

        primaryStage.setOnCloseRequest(event -> autosaveThread = false);
        primaryStage.getIcons().add(IMAGEICON);
        primaryStage.setTitle("D&D Encounter Tool v2.0");
        primaryStage.setScene(scene);
        primaryStage.show();
        LOGGER.info("GUI start finished");
    }
}
