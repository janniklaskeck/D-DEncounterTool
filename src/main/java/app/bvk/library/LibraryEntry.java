package app.bvk.library;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.entity.Creature;
import app.bvk.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class LibraryEntry extends AnchorPane { // NOSONAR

    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryEntry.class);

    private Creature creature;
    FXMLLoader loader;

    @FXML
    private Text nameText;
    
    @FXML
    private Button openImageButton;

    public LibraryEntry(final Creature creature) {
        this.creature = creature;
        loader = new FXMLLoader(getClass().getResource("LibraryEntry.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            LOGGER.error("ERROR while loadgin libraryentry fxml", e);
        }
    }

    @FXML
    public void initialize() {
        nameText.setText(creature.getName());
        openImageButton.setOnAction(event -> openImage());
    }

    private void openImage() {
        Utils.showImageFrame(creature);
    }

    public Creature getCreature() {
        return creature;
    }

}
