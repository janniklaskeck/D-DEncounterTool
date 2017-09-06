package app.bvk.encounter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.encounter.dialog.EncounterNpcWindow;
import app.bvk.encounter.dialog.EncounterPlayerWindow;
import app.bvk.entity.Creature;
import app.bvk.entity.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class EncounterGui extends BorderPane
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EncounterGui.class);

    @FXML
    private Button addNpcButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button newEncounterButton;
    @FXML
    private Button loadEncounterButton;
    @FXML
    private Button saveEncounterButton;
    @FXML
    private Button previousButton;
    @FXML
    private Button copyButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button sortButton;
    @FXML
    private Button addPlayerButton;

    @FXML
    private ListView<EncounterEntry> encounterList;

    @FXML
    private TextField encounterNameTextField;

    private Encounter encounter;
    private FXMLLoader loader;

    private static final String USERDIR = System.getProperty("user.dir");

    public EncounterGui()
    {
        this.loader = new FXMLLoader(this.getClass().getClassLoader().getResource("EncounterGui.fxml"));
        this.loader.setRoot(this);
        this.loader.setController(this);

        try
        {
            this.loader.load();
        }
        catch (final IOException e)
        {
            LOGGER.error("ERROR while loading encounter fxml", e);
        }
    }

    @FXML
    private void initialize()
    {
        this.initEncounter();
        this.setupButtons();
    }

    private void setupButtons()
    {
        this.addNpcButton.setOnAction(event -> this.addNPC());
        this.deleteButton.setOnAction(event -> this.deleteSelected());
        this.newEncounterButton.setOnAction(event -> this.newEncounter());
        this.loadEncounterButton.setOnAction(event -> this.loadEncounter());
        this.saveEncounterButton.setOnAction(event -> this.saveEncounter());
        this.previousButton.setOnAction(event -> this.previousTurn());
        this.copyButton.setOnAction(event -> this.copySelected());
        this.nextButton.setOnAction(event -> this.nextTurn());
        this.sortButton.setOnAction(event -> this.sortEncounter());
        this.addPlayerButton.setOnAction(event -> this.addPlayer());
    }

    private void initEncounter()
    {
        this.encounter = new Encounter("unnamed");
        this.encounterNameTextField.textProperty().bindBidirectional(this.encounter.nameProperty());
        this.encounterNameTextField.textProperty().addListener((obs, oldValue, newValue) -> this.encounter.nameProperty().setValue(newValue));
    }

    private void newEncounter()
    {
        this.encounter.reset();
        this.encounterList.setItems(EncounterUtils.generateEncounterEntryList(this.encounter));
    }

    private void loadEncounter()
    {
        final FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(USERDIR));
        fc.getExtensionFilters().add(new ExtensionFilter("D&D Encounter Save", "*.ddesav"));
        final File file = fc.showOpenDialog(this.getScene().getWindow());
        if (file != null)
        {
            this.encounter = EncounterUtils.loadEncounterFromFile(Paths.get(file.getAbsolutePath()));
        }
        this.encounterList.setItems(EncounterUtils.generateEncounterEntryList(this.encounter));
    }

    private void saveEncounter()
    {
        final FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(USERDIR));
        fc.getExtensionFilters().add(new ExtensionFilter("D&D Encounter Save", "*.ddesav"));
        final File file = fc.showSaveDialog(this.getScene().getWindow());
        if (file != null)
        {
            EncounterUtils.saveEncounterToFile(Paths.get(file.getAbsolutePath()), this.encounter);
        }
    }

    private void addNPC()
    {
        final Optional<Creature> selectedCreature = new EncounterNpcWindow(this.getScene().getWindow()).showAndWait();
        if (selectedCreature.isPresent())
        {
            this.encounter.addCreature(selectedCreature.get());
            this.encounterList.setItems(EncounterUtils.generateEncounterEntryList(this.encounter));
        }
    }

    private void addPlayer()
    {
        final Optional<Player> createdPlayer = new EncounterPlayerWindow(this.getScene().getWindow()).showAndWait();
        if (createdPlayer.isPresent())
        {
            this.encounter.addCreature(createdPlayer.get());
            this.encounterList.setItems(EncounterUtils.generateEncounterEntryList(this.encounter));
        }
    }

    private void deleteSelected()
    {
        if (!this.encounterList.getSelectionModel().isEmpty() && !this.encounter.getCreatureList().isEmpty())
        {
            this.encounter.removeCreature(this.encounterList.getSelectionModel().getSelectedIndex());
            this.encounterList.setItems(EncounterUtils.generateEncounterEntryList(this.encounter));
        }
    }

    private void copySelected()
    {
        if (!this.encounterList.getSelectionModel().isEmpty())
        {
            this.encounter.copyCreature(this.encounterList.getSelectionModel().getSelectedIndex());
            this.encounterList.setItems(EncounterUtils.generateEncounterEntryList(this.encounter));
        }
    }

    private void sortEncounter()
    {
        this.encounter.sort();
        this.encounterList.setItems(EncounterUtils.generateEncounterEntryList(this.encounter));
    }

    private void nextTurn()
    {
        this.encounter.selectNext();
        this.encounterList.setItems(EncounterUtils.generateEncounterEntryList(this.encounter));
        this.encounterList.scrollTo(this.encounter.getIndexOfSelectedCreature());
    }

    private void previousTurn()
    {
        this.encounter.selectPrevious();
        this.encounterList.setItems(EncounterUtils.generateEncounterEntryList(this.encounter));
        this.encounterList.scrollTo(this.encounter.getIndexOfSelectedCreature());
    }
}
