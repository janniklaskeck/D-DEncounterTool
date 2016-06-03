package app.bvk.gui;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.utils.Settings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainGUI extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainGUI.class);
    private Thread autoSaveThread;

    public static void main(final String[] args) {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
        try {
            Charset cp437 = Charset.forName("CP437");
            Settings.getInstance().setImageZipFile(new ZipFile(System.getProperty("user.dir") + "\\images.zip", cp437));
        } catch (IOException e) {
            LOGGER.error("ERROR while loading zip file", e);
            Settings.getInstance().setImageZipFile(null);
        }
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        Settings.getInstance().setImageIcon(new Image(getClass().getResourceAsStream("icon.png")));
        Parent root = FXMLLoader.load(getClass().getResource("gui.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("customstyle.css").toString());

        Settings.getInstance().setMainStage(primaryStage);

        autoSaveThread = new Thread(() -> {
            while (Settings.getInstance().isDoAutoSave()) {
                while (Settings.getInstance().isAutosave()) {
                    if (!Settings.getInstance().getEncounter().getCreatureList().isEmpty()) {
                        Settings.getInstance().getEncounter().autoSave();
                        LOGGER.info("Autosave complete");
                    }
                }
                try {
                    Thread.sleep(Settings.getInstance().getAutoSaveInterval());
                } catch (InterruptedException e) {
                    LOGGER.error("AutoSave Thread was interrupted", e);
                    Thread.currentThread().interrupt();
                }
            }
        });
        autoSaveThread.setDaemon(true);
        autoSaveThread.start();

        primaryStage.setOnCloseRequest(event -> Settings.getInstance().setDoAutoSave(false));
        primaryStage.getIcons().add(Settings.getInstance().getImageIcon());
        primaryStage.setTitle("D&D Encounter Tool v2.0");
        primaryStage.setScene(scene);
        primaryStage.show();
        LOGGER.info("GUI start finished");
    }

    @Override
    public void stop() {
        try {
            Settings.getInstance().getImageZipFile().close();
        } catch (IOException e) {
            LOGGER.error("", e);
        }
    }
}