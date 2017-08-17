package app.bvk.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.utils.Settings;
import app.bvk.utils.Utils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class MainGUI extends Application
{

    private static final Logger LOGGER = LoggerFactory.getLogger(MainGUI.class);
    private Thread autoSaveThread;

    public static void main(final String[] args)
    {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
        try
        {
            final ZipFile zipImages = new ZipFile(Settings.getInstance().getCreatureFolder() + "/images.zip");
            final ZipFile zipCreatures = new ZipFile(Settings.getInstance().getCreatureFolder() + "/creatures.zip");
            Settings.getInstance().setImageZipFile(zipImages);
            Settings.getInstance().setCreatureZipFile(zipCreatures);
        }
        catch (final ZipException e)
        {
            LOGGER.error("ERROR while loading zip file", e);
            Settings.getInstance().setImageZipFile(null);
        }
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception
    {
        Settings.getInstance().setImageIcon(new Image(this.getClass().getResourceAsStream("icon.png")));
        final Parent root = FXMLLoader.load(this.getClass().getResource("gui.fxml"));
        final Scene scene = new Scene(root);
        scene.getStylesheets().add(this.getClass().getResource("customstyle.css").toString());

        Settings.getInstance().setMainStage(primaryStage);

        this.autoSaveThread = new Thread(() ->
        {
            while (Settings.getInstance().isDoAutoSave())
            {
                while (Settings.getInstance().isAutosave())
                {
                    if (!Settings.getInstance().getEncounter().getCreatureList().isEmpty())
                    {
                        Settings.getInstance().getEncounter().autoSave();
                        LOGGER.info("Autosave complete");
                    }
                }
                try
                {
                    Thread.sleep(Settings.getInstance().getAutoSaveInterval());
                }
                catch (final InterruptedException e)
                {
                    LOGGER.error("AutoSave Thread was interrupted", e);
                    Thread.currentThread().interrupt();
                }
            }
        });
        this.autoSaveThread.setDaemon(true);
        this.autoSaveThread.start();

        primaryStage.setOnCloseRequest(event ->
        {
            this.saveData();
            Settings.getInstance().setDoAutoSave(false);
        });
        primaryStage.getIcons().add(Settings.getInstance().getImageIcon());
        primaryStage.setTitle("D&D Encounter Tool v2.0");
        primaryStage.setScene(scene);
        primaryStage.show();
        LOGGER.info("GUI start finished");
    }

    public void saveData()
    {
        final Stage stage = Utils.showSavingWarning();
        Settings.getInstance().saveLibrary();
        stage.close();
    }
}
