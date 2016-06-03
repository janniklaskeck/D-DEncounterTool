package app.bvk.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.encounter.Encounter;
import app.bvk.encounter.EncounterEntry;
import app.bvk.entity.Creature;
import app.bvk.entity.Monster;
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

public class MainController {

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
    public void initialize() {
        initProgressDisplay();
        Thread t = new Thread(() -> {
            loadData();
            fillLibrary();
            addLibraryTextFieldFilter();
            Platform.runLater(() -> rootBorderPane.setBottom(null));
        });
        t.setDaemon(true);
        t.start();
        initEncounter();
        setupButtons();
    }

    private void setupButtons() {
        addNpcButton.setOnAction(event -> addNPC());
        deleteButton.setOnAction(event -> deleteSelected());
        newEncounterButton.setOnAction(event -> newEncounter());
        loadEncounterButton.setOnAction(event -> loadEncounter());
        saveEncounterButton.setOnAction(event -> saveEncounter());
        previousButton.setOnAction(event -> previousTurn());
        copyButton.setOnAction(event -> copySelected());
        nextButton.setOnAction(event -> nextTurn());
        sortButton.setOnAction(event -> sortEncounter());
        addPlayerButton.setOnAction(event -> addPlayer());
        addLibraryEntryButton.setOnAction(event -> addNewLibraryEntry());
        autoSaveCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> autoSaveChanged(newValue));
    }

    private void initProgressDisplay() {
        barProgress = new SimpleDoubleProperty(0.0);
        progressText = new SimpleStringProperty();
        loadingProgressBar.progressProperty().bindBidirectional(barProgress);
        loadingProgressLabel.textProperty().bindBidirectional(progressText);
    }

    private void initEncounter() {
        encounter = Settings.getInstance().getEncounter();

        encounterNameTextField.setText(encounter.getEncounterNameProperty().get());
        encounterNameTextField.textProperty().bindBidirectional(encounter.getEncounterNameProperty());
        encounterNameTextField.textProperty()
                .addListener((obs, oldValue, newValue) -> encounter.setEncounterName(newValue));
    }

    private void addLibraryTextFieldFilter() {
        FilteredList<LibraryEntry> fData = new FilteredList<>(libraryList.getItems(), p -> true);
        filterTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            fData.setPredicate(t -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                if (t.getCreature().getName().get().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }
                return false;
            });
            libraryList.setItems(fData);
        });
        incProgress();

        setProgressTextAndBar(progress / amountToLoad);
    }

    private void incProgress() {
        progress++;
    }

    private void loadData() {
        if (Settings.getInstance().getImageZipFile() == null) {

            String userDir = System.getProperty(USERDIR);
            Stream<Path> str = null;
            try {
                str = Files.walk(Paths.get(userDir + "\\images"));
                str.forEach(filePath -> {
                    if (Files.isRegularFile(filePath) && filePath.toString().toLowerCase().contains("png")) {
                        amountToLoad++;
                    }
                });
                str.forEach(filePath -> {
                    if (Files.isRegularFile(filePath) && filePath.toString().toLowerCase().contains("png")) {
                        String filePathString = filePath.toString();
                        String[] filePathSplitBackslash = filePathString.split("\\\\");
                        String[] fileNameSplitDot = filePathSplitBackslash[filePathSplitBackslash.length - 1]
                                .split("\\.");
                        String creatureName = fileNameSplitDot[0];
                        Monster m = new Monster(creatureName,
                                filePathSplitBackslash[filePathSplitBackslash.length - 1]);
                        Settings.getInstance().getCreatureList().add(m);
                    }
                    incProgress();
                    setProgressTextAndBar(progress / (amountToLoad));
                });

            } catch (IOException e) {
                LOGGER.error("ERROR while reading zip file", e);
            } finally {
                if (str != null) {
                    str.close();
                }
            }
        } else {
            Enumeration<? extends ZipEntry> entries = Settings.getInstance().getImageZipFile().entries();
            while (entries.hasMoreElements()) {
                amountToLoad++;
                entries.nextElement();
            }
            entries = Settings.getInstance().getImageZipFile().entries();
            while (entries.hasMoreElements()) {
                ZipEntry ne = entries.nextElement();
                String name = ne.getName().split("\\.")[0];
                Monster m = new Monster(name, ne.getName());
                Settings.getInstance().getCreatureList().add(m);

            }
            incProgress();
            setProgressTextAndBar(progress / amountToLoad);
        }
        entryAmountText.setText("#Entries: " + (int) amountToLoad);
    }

    public void fillLibrary() {
        ObservableList<LibraryEntry> leList = FXCollections.observableArrayList();
        for (Creature c : Settings.getInstance().getCreatureList()) {
            leList.add(new LibraryEntry(c));
            incProgress();
            setProgressTextAndBar(progress / (amountToLoad));
        }

        libraryList.setItems(leList);
    }

    private void newEncounter() {
        encounter.reset();
        encounterList.setItems(encounter.getObsList());
    }

    private void loadEncounter() {
        final FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(System.getProperty(USERDIR)));
        fc.getExtensionFilters().add(new ExtensionFilter("D&D Encounter Save", "*.ddesav"));
        File file = fc.showOpenDialog(Settings.getInstance().getMainStage());
        if (file != null) {
            encounter.readFromFile(file);
        }
        encounterList.setItems(encounter.getObsList());
    }

    private void saveEncounter() {
        final FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(System.getProperty(USERDIR)));
        fc.getExtensionFilters().add(new ExtensionFilter("D&D Encounter Save", "*.ddesav"));
        File file = fc.showSaveDialog(Settings.getInstance().getMainStage());
        if (file != null) {
            encounter.saveToFile(file);
        }
    }

    private void addNPC() {
        Utils.newNPCWindow(encounter);
        encounterList.setItems(encounter.getObsList());
    }

    private void addPlayer() {
        Utils.newPlayerWindow(encounter);
        encounterList.setItems(encounter.getObsList());
    }

    private void deleteSelected() {
        if (!encounterList.getSelectionModel().isEmpty() && !encounter.getCreatureList().isEmpty()) {
            encounter.remove(encounterList.getSelectionModel().getSelectedIndex());
            encounterList.setItems(encounter.getObsList());
        }
    }

    private void copySelected() {
        if (!encounterList.getSelectionModel().isEmpty()) {
            encounter.copy(encounterList.getSelectionModel().getSelectedIndex());
            encounterList.setItems(encounter.getObsList());
        }
    }

    private void sortEncounter() {
        encounter.sort();
        encounterList.setItems(encounter.getObsList());
    }

    @FXML
    private void nextTurn() {
        encounter.setNextIndex();
        encounterList.setItems(encounter.getObsList());
        encounterList.scrollTo(encounter.getCurrentIndex());
    }

    private void previousTurn() {
        encounter.setLastIndex();
        encounterList.setItems(encounter.getObsList());
        encounterList.scrollTo(encounter.getCurrentIndex());
    }

    private void addNewLibraryEntry() {
        Utils.newLibraryEntryWindow();
    }

    private void autoSaveChanged(final boolean selected) {
        Settings.getInstance().setAutosave(selected);
    }

    private void setProgressTextAndBar(final double percentage) {
        barProgress.set(percentage);
        Platform.runLater(() -> progressText.set("Loading progress: " + (int) (percentage * 100) + "%"));
    }
}