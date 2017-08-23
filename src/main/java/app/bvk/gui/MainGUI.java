package app.bvk.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.library.CreatureLibrary;
import app.bvk.utils.Utils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainGUI extends Application
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MainGUI.class);

    @Override
    public void start(final Stage primaryStage) throws Exception
    {
        final Parent root = FXMLLoader.load(this.getClass().getClassLoader().getResource("MainGui.fxml"));
        final Scene scene = new Scene(root);
        scene.getStylesheets().add(this.getClass().getClassLoader().getResource("customstyle.css").toString());
        primaryStage.setOnCloseRequest(event ->
        {
            CreatureLibrary.getInstance().saveCreatures();
            Platform.exit();
        });
        primaryStage.getIcons().add(Utils.ICON);
        primaryStage.setTitle("D&D Encounter Tool v2.0");
        primaryStage.setScene(scene);
        primaryStage.show();
        LOGGER.info("GUI start finished");
    }
}
