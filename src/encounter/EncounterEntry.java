package encounter;

import java.io.IOException;

import entity.Creature;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class EncounterEntry extends GridPane {

    private Creature creature;
    FXMLLoader loader;

    @FXML
    private Label name;

    public EncounterEntry(Creature creature) {
	this.creature = creature;
	loader = new FXMLLoader(getClass().getResource("EncounterEntry.fxml"));
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
	name.setText(creature.getName());
    }

}
