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

/**
 *
 * @author Niklas 12.06.2016
 *
 */
public class EditorWindow extends GridPane
{ // NOSONAR

    private static final Logger LOGGER = LoggerFactory.getLogger(EditorWindow.class);
    private static Stage editorStage;

    /**
     * Constructor
     */
    public EditorWindow()
    {
        final Scene scene = this.initScene();
        this.initStage(scene);
        getEditorStage().show();
    }

    private void initStage(final Scene scene)
    {
        setEditorStage(new Stage());
        editorStage.getIcons().add(Settings.getInstance().getImageIcon());
        editorStage.setScene(scene);
        editorStage.setTitle("Creature Editor");
        editorStage.setResizable(false);
    }

    private Scene initScene()
    {
        Parent root = null;
        final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("EditorWindow.fxml"));
        try
        {
            root = loader.load();
        }
        catch (final IOException e)
        {
            LOGGER.error("Error while loading fxml file", e);
        }
        return new Scene(root);
    }

    public static Stage getEditorStage()
    {
        return editorStage;
    }

    private static void setEditorStage(final Stage stage)
    {
        editorStage = stage;
    }
}
