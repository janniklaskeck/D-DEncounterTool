package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;

import encounter.Encounter;
import encounter.EncounterEntry;
import entity.Creature;
import entity.Monster;
import entity.Player;
import gui.MainGUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import library.LibraryEntry;

public class MainController {

    private static int libEntries = 0;
    private ObservableList<LibraryEntry> libEntryList;
    private ObservableList<EncounterEntry> encounterEntryList;

    private Encounter encounter;

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
    public void initialize() {
	loadData();
	fillLibrary();
	addLibraryTextFieldFilter();
	MainGUI.encounter = new Encounter("Unnamed Encounter");
	encounter = MainGUI.encounter;

	encounterNameTextField.setText(encounter.getEncounterName());
	encounterNameTextField.textProperty().bindBidirectional(encounter.encounterNameProperty);
	encounterNameTextField.textProperty().addListener(new ChangeListener<String>() {
	    @Override
	    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		encounter.setEncounterName(newValue);
	    }
	});

	encounterEntryList = encounter.getObsList();
	encounterList.setItems(encounterEntryList);
    }

    private void addLibraryTextFieldFilter() {
	FilteredList<LibraryEntry> fData = new FilteredList<>(libEntryList, p -> true);
	filterTextField.textProperty().addListener(new ChangeListener<String>() {
	    @Override
	    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		fData.setPredicate(new Predicate<LibraryEntry>() {
		    @Override
		    public boolean test(LibraryEntry t) {
			if (newValue == null || newValue.isEmpty()) {
			    return true;
			}
			if (t.getCreature().getName().toLowerCase().contains(newValue.toLowerCase())) {
			    return true;
			}
			return false;
		    }
		});
		libraryList.setItems(fData);
	    }
	});
    }

    private void loadData() {
	if (MainGUI.imageZipFile == null) {
	    try {
		Files.walk(Paths.get(System.getProperty("user.dir") + "\\images")).forEach(filePath -> {
		    if (Files.isRegularFile(filePath) && filePath.toString().toLowerCase().contains("png")) {
			String filePathString = filePath.toString();
			String[] filePathSplitBackslash = filePathString.split("\\\\");
			String[] fileNameSplitDot = filePathSplitBackslash[filePathSplitBackslash.length - 1]
				.split("\\.");
			String creatureName = fileNameSplitDot[0];
			Monster m = new Monster(creatureName,
				filePathSplitBackslash[filePathSplitBackslash.length - 1]);
			MainGUI.creatureList.add(m);
			libEntries++;
		    }
		});
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	} else {
	    Enumeration<? extends ZipEntry> entries = MainGUI.imageZipFile.entries();
	    while (entries.hasMoreElements()) {
		ZipEntry ne = (ZipEntry) entries.nextElement();
		String name = ne.getName().split("\\.")[0];
		Monster m = new Monster(name, ne.getName());
		MainGUI.creatureList.add(m);
		libEntries++;
	    }
	}
	entryAmountText.setText("#Entries: " + libEntries);
    }

    private void fillLibrary() {
	ArrayList<LibraryEntry> leList = new ArrayList<LibraryEntry>();
	for (Creature c : MainGUI.creatureList) {
	    leList.add(new LibraryEntry(c));
	}
	libEntryList = FXCollections.observableArrayList(leList);
	libraryList.setItems(libEntryList);
    }

    @FXML
    protected void newEncounter(ActionEvent event) {
	encounter.reset();
	encounterList.setItems(encounter.getObsList());
    }

    @FXML
    protected void loadEncounter(ActionEvent event) {
	final FileChooser fc = new FileChooser();
	fc.setInitialDirectory(new File(System.getProperty("user.dir")));
	fc.getExtensionFilters().add(new ExtensionFilter("D&D Encounter Save", "*.ddesav"));
	File file = fc.showOpenDialog(MainGUI.mainStage);
	if (file != null) {
	    encounter.readFromFile(file);
	}
	encounterList.setItems(encounter.getObsList());
    }

    @FXML
    protected void saveEncounter(ActionEvent event) {
	final FileChooser fc = new FileChooser();
	fc.setInitialDirectory(new File(System.getProperty("user.dir")));
	fc.getExtensionFilters().add(new ExtensionFilter("D&D Encounter Save", "*.ddesav"));
	File file = fc.showSaveDialog(MainGUI.mainStage);
	if (file != null) {
	    encounter.saveToFile(file);
	}
    }

    @FXML
    protected void addNPC(ActionEvent event) {
	Dialog<String> d = new Dialog<String>();
	d.setTitle("Enter Player Name");
	d.setResizable(false);
	Label name = new Label("Filter: ");
	TextField filterTF = new TextField();
	ListView<String> lv = new ListView<String>();
	lv.setOnMouseClicked(new EventHandler<MouseEvent>() {

	    @Override
	    public void handle(MouseEvent event) {
		if (event.getClickCount() == 2) {
		    d.resultProperty().set(lv.getSelectionModel().getSelectedItem());
		}
	    }
	});
	ArrayList<String> names = new ArrayList<String>();
	for (LibraryEntry le : libEntryList) {
	    names.add(le.getCreature().getName());
	}
	ObservableList<String> ol = FXCollections.observableArrayList(names);
	lv.setItems(ol);
	FilteredList<String> fData = new FilteredList<String>(ol, p -> true);
	filterTF.textProperty().addListener(new ChangeListener<String>() {
	    @Override
	    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		fData.setPredicate(new Predicate<String>() {
		    @Override
		    public boolean test(String t) {
			if (newValue == null || newValue.isEmpty()) {
			    return true;
			}
			if (t.toLowerCase().contains(newValue.toLowerCase())) {
			    return true;
			}
			return false;
		    }
		});
		lv.setItems(fData);
	    }
	});

	GridPane grid = new GridPane();
	grid.add(name, 0, 0);
	grid.add(filterTF, 1, 0);
	grid.add(lv, 0, 1, 2, 1);
	d.getDialogPane().setContent(grid);

	ButtonType okButton = new ButtonType("Save", ButtonData.OK_DONE);
	ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
	d.getDialogPane().getButtonTypes().add(okButton);
	d.getDialogPane().getButtonTypes().add(cancelButton);
	d.setResultConverter(new Callback<ButtonType, String>() {

	    @Override
	    public String call(ButtonType param) {
		if (param == okButton) {
		    return lv.getSelectionModel().getSelectedItem();
		}
		if (param == cancelButton) {
		    return null;
		}
		return null;
	    }
	});

	Optional<String> a = d.showAndWait();
	if (a.isPresent()) {
	    encounter.addCreature(a.get());
	    encounterList.setItems(encounter.getObsList());
	}
    }

    @FXML
    protected void addPlayer(ActionEvent event) {
	Dialog<String> d = new Dialog<String>();
	d.setTitle("Enter Player Name");
	d.setResizable(false);
	Label name = new Label("Name: ");
	TextField tf = new TextField();

	GridPane grid = new GridPane();
	grid.add(name, 0, 0);
	grid.add(tf, 1, 0);
	d.getDialogPane().setContent(grid);
	ButtonType okButton = new ButtonType("Save", ButtonData.OK_DONE);
	ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
	d.getDialogPane().getButtonTypes().add(okButton);
	d.getDialogPane().getButtonTypes().add(cancelButton);
	d.setResultConverter(new Callback<ButtonType, String>() {

	    @Override
	    public String call(ButtonType param) {
		if (param == okButton) {
		    return tf.getText();
		}
		if (param == cancelButton) {
		    return null;
		}
		return null;
	    }
	});

	Optional<String> a = d.showAndWait();
	if (a.isPresent()) {
	    encounter.addCreature(new Player(tf.getText(), ""));
	    encounterList.setItems(encounter.getObsList());
	}
    }

    @FXML
    protected void deleteSelected(ActionEvent event) {
	if (!encounterList.getSelectionModel().isEmpty()) {
	    if (encounter.getCreatureList().size() > 0) {
		encounter.getCreatureList().remove(encounterList.getSelectionModel().getSelectedIndex());
		encounterList.setItems(encounter.getObsList());
	    }
	}
    }

    @FXML
    protected void copySelected(ActionEvent event) {
	if (!encounterList.getSelectionModel().isEmpty()) {
	    encounter.copy(encounterList.getSelectionModel().getSelectedIndex());
	    encounterList.setItems(encounter.getObsList());
	}
    }

    @FXML
    protected void sortEncounter(ActionEvent event) {
	encounter.sort();
	encounterList.setItems(encounter.getObsList());
    }

    @FXML
    protected void nextTurn(ActionEvent event) {
	encounter.setNextIndex();
	encounterList.setItems(encounter.getObsList());
	encounterList.scrollTo(encounter.getCurrentIndex());
    }

    @FXML
    protected void previousTurn(ActionEvent event) {
	encounter.setLastIndex();
	encounterList.setItems(encounter.getObsList());
	encounterList.scrollTo(encounter.getCurrentIndex());
    }

    private File imageFile = null;

    @FXML
    protected void addNewLibraryEntry(ActionEvent event) {
	Dialog<Creature> d = new Dialog<Creature>();
	d.setTitle("Enter Player Name");
	d.setResizable(false);
	Label name = new Label("Name: ");
	TextField tfName = new TextField();
	Label image = new Label("Name: ");
	TextField imageName = new TextField();
	imageName.setEditable(false);
	Button chooseImage = new Button("Select Image");
	chooseImage.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		final FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File(System.getProperty("user.dir")));
		fc.getExtensionFilters().add(new ExtensionFilter("Image File", "*.png", "*.jpeg", "*.bmp"));
		File file = fc.showOpenDialog(MainGUI.mainStage);
		if (file != null) {
		    imageFile = file;
		    imageName.setText(file.getName());
		}
	    }
	});

	GridPane grid = new GridPane();
	grid.add(name, 0, 0);
	grid.add(tfName, 1, 0);
	grid.add(image, 0, 1);
	grid.add(imageName, 1, 1);
	grid.add(chooseImage, 2, 1);
	d.getDialogPane().setContent(grid);
	ButtonType okButton = new ButtonType("Save", ButtonData.OK_DONE);
	ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
	d.getDialogPane().getButtonTypes().add(okButton);
	d.getDialogPane().getButtonTypes().add(cancelButton);
	d.setResultConverter(new Callback<ButtonType, Creature>() {

	    @Override
	    public Creature call(ButtonType param) {
		if (param == okButton) {
		    return new Creature(tfName.getText(), imageFile.getName());
		}
		if (param == cancelButton) {
		    return null;
		}
		return null;
	    }
	});

	Optional<Creature> a = d.showAndWait();
	if (a.isPresent()) {
	    MainGUI.creatureList.add(a.get());
	}
    }

    @FXML
    public void autoSaveChanged() {
	MainGUI.autosave = autoSaveCheckBox.isSelected();
    }
}
