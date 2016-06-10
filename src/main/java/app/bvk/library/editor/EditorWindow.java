package app.bvk.library.editor;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.utils.Settings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class EditorWindow extends GridPane { //NOSONAR

    private static final Logger LOGGER = LoggerFactory.getLogger(EditorWindow.class);
    private static Stage editorStage;

    public EditorWindow() {
        editorStage = new Stage();
        Parent root = null;
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("EditorWindow.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {            
            root = loader.load();
        } catch (IOException e) {
            LOGGER.error("Error while loading fxml file", e);
        }
        final Scene scene = new Scene(root);

        editorStage.getIcons().add(Settings.getInstance().getImageIcon());
        editorStage.setScene(scene);
        editorStage.setTitle("Creature Editor");
        editorStage.show();
    }
}
