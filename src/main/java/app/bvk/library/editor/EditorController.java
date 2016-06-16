package app.bvk.library.editor;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class EditorController {

    @FXML
    private ListView<String> languageListView;

    @FXML
    private ListView<String> immunityListView;

    @FXML
    private ListView<String> sensesListView;

    @FXML
    private ListView<String> savingThrowListView;

    @FXML
    private Button saveButton;

    @FXML
    private Button resetButton;

    @FXML
    private Button cancelButton;

    @FXML
    public void initialize() {
        initData();
        initButtons();
    }

    private void initButtons() {
        saveButton.setOnAction(event -> EditorWindow.getEditorStage().close());
        resetButton.setOnAction(event -> EditorWindow.getEditorStage().close());
        cancelButton.setOnAction(event -> EditorWindow.getEditorStage().close());
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
        immunityListView.getItems().add("Bludgeoning (NM)");
        immunityListView.getItems().add("Piercing (NM)");
        immunityListView.getItems().add("Slashing (NM)");
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

}
