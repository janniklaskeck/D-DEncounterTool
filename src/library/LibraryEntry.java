package library;

import java.io.IOException;

import entity.Creature;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class LibraryEntry extends AnchorPane {

    private Creature creature;

    @FXML
    private Text nameText;

    public LibraryEntry(Creature creature) {
	this.creature = creature;
	FXMLLoader loader = new FXMLLoader(getClass().getResource("LibraryEntry.fxml"));
	loader.setRoot(this);
	loader.setController(this);

	try {
	    loader.load();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @FXML
    private void initialize() {
	nameText.setText(creature.getName());
    }

    @FXML
    protected void openImage(ActionEvent event) {
	// TODO open Image
    }

}
