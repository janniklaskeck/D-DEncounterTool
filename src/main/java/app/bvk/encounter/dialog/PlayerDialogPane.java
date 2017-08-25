package app.bvk.encounter.dialog;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.entity.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

public class PlayerDialogPane extends DialogPane
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerDialogPane.class);
    private FXMLLoader loader;

    @FXML
    private TextField nameTextField;

    private Player player;

    public PlayerDialogPane()
    {
        this.loader = new FXMLLoader(this.getClass().getClassLoader().getResource("EncounterPlayerWindowGui.fxml"));
        this.loader.setRoot(this);
        this.loader.setController(this);
        try
        {
            this.loader.load();
        }
        catch (final IOException e)
        {
            LOGGER.error("ERROR while loading encounter fxml", e);
        }
    }

    @FXML
    private void initialize()
    {
        final ButtonType addButtonType = new ButtonType("Add", ButtonData.APPLY);
        final ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        this.getButtonTypes().add(addButtonType);
        this.getButtonTypes().add(cancelButtonType);
        final Button addButton = (Button) this.lookupButton(addButtonType);
        addButton.setDefaultButton(true);
        final Button cancelButton = (Button) this.lookupButton(cancelButtonType);
        cancelButton.setCancelButton(true);
        this.nameTextField.textProperty().addListener((obs, oldValue, newValue) -> this.player = new Player(newValue));
    }

    public Player getPlayer()
    {
        return this.player;
    }
}
