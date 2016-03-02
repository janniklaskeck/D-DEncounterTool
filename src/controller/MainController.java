package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class MainController {

    @FXML
    ListView<String> libraryList;

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
}
