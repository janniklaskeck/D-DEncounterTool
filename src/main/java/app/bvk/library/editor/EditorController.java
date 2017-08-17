package app.bvk.library.editor;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class EditorController
{

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
    public void initialize()
    {
        this.initData();
        this.initButtons();
    }

    private void initButtons()
    {
        this.saveButton.setOnAction(event -> EditorWindow.getEditorStage().close());
        this.resetButton.setOnAction(event -> EditorWindow.getEditorStage().close());
        this.cancelButton.setOnAction(event -> EditorWindow.getEditorStage().close());
    }

    private void initData()
    {
        this.initLanguages();
        this.initImmunities();
    }

    private void initImmunities()
    {
        this.immunityListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.immunityListView.getItems().add("Bludgeoning");
        this.immunityListView.getItems().add("Piercing");
        this.immunityListView.getItems().add("Slashing");
        this.immunityListView.getItems().add("Bludgeoning (NM)");
        this.immunityListView.getItems().add("Piercing (NM)");
        this.immunityListView.getItems().add("Slashing (NM)");
        this.immunityListView.getItems().add("Fire");
        this.immunityListView.getItems().add("Cold");
        this.immunityListView.getItems().add("Poison");
        this.immunityListView.getItems().add("Thunder");
        this.immunityListView.getItems().add("Acid");
        this.immunityListView.getItems().add("Lighting");
    }

    private void initLanguages()
    {
        this.languageListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.languageListView.getItems().add("Common");
        this.languageListView.getItems().add("Dwarvish");
        this.languageListView.getItems().add("Elvish");
        this.languageListView.getItems().add("Giant");
        this.languageListView.getItems().add("Gnomish");
        this.languageListView.getItems().add("Goblin");
        this.languageListView.getItems().add("Halfling");
        this.languageListView.getItems().add("Orc");
        this.languageListView.getItems().add("Abyssal");
        this.languageListView.getItems().add("Celestial");
        this.languageListView.getItems().add("Draconic");
        this.languageListView.getItems().add("Deep Speech");
        this.languageListView.getItems().add("Infernal");
        this.languageListView.getItems().add("Primordial");
        this.languageListView.getItems().add("Sylvan");
        this.languageListView.getItems().add("Undercommon");
    }

}
