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
        this.newEncounter();
        this.setupButtons();
    }

    private void setupButtons()
    {
        this.addNpcButton.setOnAction(event -> this.addNPC());
        this.deleteButton.setOnAction(event -> this.encounter.removeCreature(this.encounterList.getSelectionModel().getSelectedIndex()));
        this.newEncounterButton.setOnAction(event -> this.newEncounter());
        this.loadEncounterButton.setOnAction(event -> this.loadEncounter());
        this.saveEncounterButton.setOnAction(event -> this.saveEncounter());
        this.previousButton.setOnAction(event -> this.previousTurn());
        this.copyButton.setOnAction(event -> this.encounter.copyCreature(this.encounterList.getSelectionModel().getSelectedIndex()));
        this.nextButton.setOnAction(event -> this.nextTurn());
        this.sortButton.setOnAction(event -> this.encounter.sort());
        this.addPlayerButton.setOnAction(event -> this.addPlayer());
    }

    private void newEncounter()
    {
        if (this.encounter == null)
        {
            this.encounter = new Encounter("unnamed");
            this.encounterNameTextField.textProperty().bindBidirectional(this.encounter.nameProperty());
            this.encounterNameTextField.textProperty().addListener((obs, oldValue, newValue) -> this.encounter.nameProperty().setValue(newValue));
        }
        this.encounter.reset();
        this.encounterList.itemsProperty().bind(this.encounter.listProperty());
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
            this.encounter.addCreatureEntry(selectedCreature.get());
        }
    }

    private void addPlayer()
    {
        final Optional<Player> createdPlayer = new EncounterPlayerWindow(this.getScene().getWindow()).showAndWait();
        if (createdPlayer.isPresent())
        {
            this.encounter.addCreatureEntry(createdPlayer.get());
        }
    }

    private void nextTurn()
    {
        this.encounter.selectNext();
        this.encounterList.scrollTo(this.encounter.getIndexOfSelectedCreature());
    }

    private void previousTurn()
    {
        this.encounter.selectPrevious();
        this.encounterList.scrollTo(this.encounter.getIndexOfSelectedCreature());
    }
}
