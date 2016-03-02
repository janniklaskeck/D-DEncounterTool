package controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import entity.Creature;
import entity.Monster;
import gui.MainGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import library.LibraryEntry;

public class MainController {

    private static int libEntries = 0;
    private ObservableList<LibraryEntry> libEntryList;

    @FXML
    private ListView<LibraryEntry> libraryList;

    @FXML
    private Text entryAmountText;

    @FXML
    public void initialize() {
	loadData();
	fillLibrary();
    }

    private void loadData() {
	try {
	    Files.walk(Paths.get(System.getProperty("user.dir") + "\\images")).forEach(filePath -> {
		if (Files.isRegularFile(filePath) && filePath.toString().contains("PNG")) {
		    String filePathString = filePath.toString();
		    String[] filePathSplitBackslash = filePathString.split("\\\\");
		    String[] fileNameSplitDot = filePathSplitBackslash[filePathSplitBackslash.length - 1].split("\\.");
		    String creatureName = fileNameSplitDot[0];
		    MainGUI.creatureList
			    .add(new Monster(creatureName, filePathSplitBackslash[filePathSplitBackslash.length - 1]));
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
	System.out.println("delete");
    }

    @FXML
    protected void copySelected(ActionEvent event) {
	System.out.println("copy");
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
