package app.bvk.library.editor;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.utils.Settings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author Niklas 12.06.2016
 *
 */
public class EditorWindow extends GridPane { // NOSONAR

    private static final Logger LOGGER = LoggerFactory.getLogger(EditorWindow.class);
    private static Stage editorStage;

    @FXML
    private ListView<String> languageListView;

    @FXML
    private ListView<String> immunityListView;

    @FXML
    private ListView<String> sensesListView;

    @FXML
    private ListView<String> savingThrowListView;

    /**
     * Constructor
     */
    public EditorWindow() {
	final Scene scene = initScene();
	initStage(scene);
	initData();
	getEditorStage().show();
    }

    private void initStage(final Scene scene) {
	setEditorStage(new Stage());
	editorStage.getIcons().add(Settings.getInstance().getImageIcon());
	editorStage.setScene(scene);
	editorStage.setTitle("Creature Editor");
	editorStage.setResizable(false);
    }

    private Scene initScene() {
	Parent root = null;
	final FXMLLoader loader = new FXMLLoader(getClass().getResource("EditorWindow.fxml"));
	loader.setRoot(this);
	loader.setController(this);
	try {
	    root = loader.load();
	} catch (IOException e) {
	    LOGGER.error("Error while loading fxml file", e);
	}
	return new Scene(root);
    }

    private void initData() {
	initLanguages();
	initImmunities();
    }

    private void initImmunities() {
	immunityListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	immunityListView.getItems().add("Bludgeoning");
	immunityListView.getItems().add("Piercing");
	immunityListView.getItems().add("Slashing");
	immunityListView.getItems().add("Fire");
	immunityListView.getItems().add("Cold");
	immunityListView.getItems().add("Poison");
	immunityListView.getItems().add("Thunder");
	immunityListView.getItems().add("Acid");
	immunityListView.getItems().add("Lighting");
    }

    private void initLanguages() {
	languageListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	languageListView.getItems().add("Common");
	languageListView.getItems().add("Dwarvish");
	languageListView.getItems().add("Elvish");
	languageListView.getItems().add("Giant");
	languageListView.getItems().add("Gnomish");
	languageListView.getItems().add("Goblin");
	languageListView.getItems().add("Halfling");
	languageListView.getItems().add("Orc");
	languageListView.getItems().add("Abyssal");
	languageListView.getItems().add("Celestial");
	languageListView.getItems().add("Draconic");
	languageListView.getItems().add("Deep Speech");
	languageListView.getItems().add("Infernal");
	languageListView.getItems().add("Primordial");
	languageListView.getItems().add("Sylvan");
	languageListView.getItems().add("Undercommon");
    }

    public static Stage getEditorStage() {
	return editorStage;
    }

    private static void setEditorStage(final Stage stage) {
	editorStage = stage;
    }
}
