package app.bvk.controller;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.encounter.Encounter;
import app.bvk.encounter.EncounterEntry;
import app.bvk.entity.Creature;
import app.bvk.library.CreatureLibrary;
import app.bvk.library.LibraryEntry;
import app.bvk.utils.Settings;
import app.bvk.utils.Utils;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainController
{

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
    private static final String USERDIR = "user.dir";

    private Encounter encounter;

    @FXML
    private BorderPane rootBorderPane;
    @FXML
    private ListView<LibraryEntry> libraryList;
    @FXML
    private ListView<EncounterEntry> encounterList;
    @FXML
    private Text entryAmountText;
    @FXML
    private TextField filterTextField;
    @FXML
    private TextField encounterNameTextField;
    @FXML
    private CheckBox autoSaveCheckBox;
    @FXML
    private ProgressBar loadingProgressBar;
    @FXML
    private Label loadingProgressLabel;
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
    private Button addLibraryEntryButton;

    private SimpleDoubleProperty barProgress;
    private SimpleStringProperty progressText;
    private float progress = 0.0f;
    private float amountToLoad = 0.0f;

    @FXML
    public void initialize()
    {
        this.initProgressDisplay();
        final Thread t = new Thread(() ->
        {
            CreatureLibrary.getInstance();
            this.fillLibrary();
            this.addLibraryTextFieldFilter();
            Platform.runLater(() -> this.rootBorderPane.setBottom(null));
            LOGGER.debug("Loading finished!");
        });
        t.setDaemon(true);
        t.start();
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
        this.addLibraryEntryButton.setOnAction(event -> this.addNewLibraryEntry());
        this.autoSaveCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> this.autoSaveChanged(newValue));
    }

    private void initProgressDisplay()
    {
        this.barProgress = new SimpleDoubleProperty(0.0);
        this.progressText = new SimpleStringProperty();
        this.loadingProgressBar.progressProperty().bindBidirectional(this.barProgress);
        this.loadingProgressLabel.textProperty().bindBidirectional(this.progressText);
    }

    private void initEncounter()
    {
        this.encounter = new Encounter("unnamed");

        this.encounterNameTextField.setText(this.encounter.getEncounterNameProperty().get());
        this.encounterNameTextField.textProperty().bindBidirectional(this.encounter.getEncounterNameProperty());
        this.encounterNameTextField.textProperty().addListener((obs, oldValue, newValue) -> this.encounter.setEncounterName(newValue));
    }

    private void addLibraryTextFieldFilter()
    {
        final FilteredList<LibraryEntry> fData = new FilteredList<>(this.libraryList.getItems(), p -> true);
        this.filterTextField.textProperty().addListener((obs, oldValue, newValue) ->
        {
            fData.setPredicate(t ->
            {
                final boolean isEmpty = newValue == null || newValue.isEmpty();
                final boolean nameMatch = t.getCreature().getName().get().toLowerCase().contains(newValue.toLowerCase());
                return isEmpty || nameMatch;
            });
            this.libraryList.setItems(fData);
        });
        this.incProgress();

        this.setProgressTextAndBar(this.progress / this.amountToLoad);
    }

    private void incProgress()
    {
        this.progress++;
    }

    private void fillLibrary()
    {
        final ObservableList<LibraryEntry> leList = FXCollections.observableArrayList();
        for (final Creature c : CreatureLibrary.getInstance().getCreatures())
        {
            leList.add(new LibraryEntry(c));
        }

        this.libraryList.setItems(leList);
    }

    private void newEncounter()
    {
        this.encounter.reset();
        this.encounterList.setItems(this.encounter.getObsList());
    }

    private void loadEncounter()
    {
        final FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(System.getProperty(USERDIR)));
        fc.getExtensionFilters().add(new ExtensionFilter("D&D Encounter Save", "*.ddesav"));
        final File file = fc.showOpenDialog(Settings.getInstance().getMainStage());
        if (file != null)
        {
            this.encounter.readFromFile(file);
        }
        this.encounterList.setItems(this.encounter.getObsList());
    }

    private void saveEncounter()
    {
        final FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(System.getProperty(USERDIR)));
        fc.getExtensionFilters().add(new ExtensionFilter("D&D Encounter Save", "*.ddesav"));
        final File file = fc.showSaveDialog(Settings.getInstance().getMainStage());
        if (file != null)
        {
            this.encounter.saveToFile(file);
        }
    }

    private void addNPC()
    {
        Utils.newNPCWindow(this.encounter);
        this.encounterList.setItems(this.encounter.getObsList());
    }

    private void addPlayer()
    {
        Utils.newPlayerWindow(this.encounter);
        this.encounterList.setItems(this.encounter.getObsList());
    }

    private void deleteSelected()
    {
        if (!this.encounterList.getSelectionModel().isEmpty() && !this.encounter.getCreatureList().isEmpty())
        {
            this.encounter.remove(this.encounterList.getSelectionModel().getSelectedIndex());
            this.encounterList.setItems(this.encounter.getObsList());
        }
    }

    private void copySelected()
    {
        if (!this.encounterList.getSelectionModel().isEmpty())
        {
            this.encounter.copy(this.encounterList.getSelectionModel().getSelectedIndex());
            this.encounterList.setItems(this.encounter.getObsList());
        }
    }

    private void sortEncounter()
    {
        this.encounter.sort();
        this.encounterList.setItems(this.encounter.getObsList());
    }

    private void nextTurn()
    {
        this.encounter.setNextIndex();
        this.encounterList.setItems(this.encounter.getObsList());
        this.encounterList.scrollTo(this.encounter.getCurrentIndex());
    }

    private void previousTurn()
    {
        this.encounter.setLastIndex();
        this.encounterList.setItems(this.encounter.getObsList());
        this.encounterList.scrollTo(this.encounter.getCurrentIndex());
    }

    private void addNewLibraryEntry()
    {
        Utils.newLibraryEntryWindow();
    }

    private void autoSaveChanged(final boolean selected)
    {
        Settings.getInstance().setAutosave(selected);
    }

    private void setProgressTextAndBar(final double percentage)
    {
        this.barProgress.set(percentage);
        Platform.runLater(() -> this.progressText.set("Loading progress: " + (int) (percentage * 100) + "%"));
    }
}
