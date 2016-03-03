package controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.function.Predicate;

import encounter.Encounter;
import encounter.EncounterEntry;
import entity.Creature;
import entity.Monster;
import gui.MainGUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
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
    public void initialize() {
	loadData();
	fillLibrary();
	addLibraryTextFieldFilter();

	encounter = new Encounter();
	encounter.addCreature(MainGUI.creatureList.get(0));
	encounter.addCreature(MainGUI.creatureList.get(1));
	encounter.addCreature(MainGUI.creatureList.get(2));
	encounter.addCreature(MainGUI.creatureList.get(3));
	encounter.addCreature(MainGUI.creatureList.get(4));

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
	try {
	    Files.walk(Paths.get(System.getProperty("user.dir") + "\\images")).forEach(filePath -> {
		if (Files.isRegularFile(filePath) && filePath.toString().contains("PNG")) {
		    String filePathString = filePath.toString();
		    String[] filePathSplitBackslash = filePathString.split("\\\\");
		    String[] fileNameSplitDot = filePathSplitBackslash[filePathSplitBackslash.length - 1].split("\\.");
		    String creatureName = fileNameSplitDot[0];
		    Monster m = new Monster(creatureName, filePathSplitBackslash[filePathSplitBackslash.length - 1]);
		    MainGUI.creatureList.add(m);
		    libEntries++;
		}
	    });
	} catch (IOException e) {
	    e.printStackTrace();
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
	System.out.println("new");
    }

    @FXML
    protected void loadEncounter(ActionEvent event) {
	System.out.println("load");
    }

    @FXML
    protected void saveEncounter(ActionEvent event) {
	System.out.println("save");
    }

    @FXML
    protected void addNPC(ActionEvent event) {
	System.out.println("npc");
    }

    @FXML
    protected void addPlayer(ActionEvent event) {
	System.out.println("player");
    }

    @FXML
    protected void deleteSelected(ActionEvent event) {
	if (encounter.getCreatureList().size() > 0) {
	    encounter.getCreatureList().remove(encounterList.getSelectionModel().getSelectedIndex());
	    encounterList.setItems(encounter.getObsList());
	}
    }

    @FXML
    protected void copySelected(ActionEvent event) {
	encounter.copy(encounterList.getSelectionModel().getSelectedIndex());
	encounterList.setItems(encounter.getObsList());
    }

    @FXML
    protected void sortEncounter(ActionEvent event) {
	System.out.println("sort");
    }

    @FXML
    protected void nextTurn(ActionEvent event) {
	System.out.println("next");
    }

    @FXML
    protected void previousTurn(ActionEvent event) {
	System.out.println("prev");
    }

    @FXML
    protected void addNewLibraryEntry(ActionEvent event) {
	System.out.println("newlib");
    }
}
