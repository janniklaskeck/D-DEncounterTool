package app.bvk.library;

import java.io.IOException;

import app.bvk.entity.Creature;
import app.bvk.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class LibraryEntry extends AnchorPane {

    private Creature creature;
    FXMLLoader loader;

    @FXML
    private Text nameText;

    public LibraryEntry(Creature creature) {
        this.creature = creature;
        loader = new FXMLLoader(getClass().getResource("LibraryEntry.fxml"));
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
        Utils.showImageFrame(creature);
    }

    public Creature getCreature() {
        return creature;
    }

}
