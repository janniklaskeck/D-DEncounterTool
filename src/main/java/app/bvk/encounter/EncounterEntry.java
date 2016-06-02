package app.bvk.encounter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.entity.Creature;
import app.bvk.entity.Player;
import app.bvk.gui.MainGUI;
import app.bvk.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class EncounterEntry extends GridPane { // NOSONAR

    private static final Logger LOGGER = LoggerFactory.getLogger(MainGUI.class);

    private static final int MAXIMUMNUMBERS = 5;
    private Creature creature;
    private FXMLLoader loader;

    @FXML
    private TextField initiativeTextField;

    @FXML
    private TextField healthTextField;

    @FXML
    private TextField armorClassTextField;

    @FXML
    private TextArea statusTextArea;

    @FXML
    private Button openImageButton;

    public EncounterEntry(final Creature c) {
        this.creature = c;
        loader = new FXMLLoader(getClass().getResource("EncounterEntry.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            LOGGER.error("ERROR while loading encounterentry fxml", e);
        }
        setupCreature();
    }

    private void setupCreature() {
        if (creature.isSelected()) {
            setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        if (creature.getClass().equals(Player.class)) {
            openImageButton.setDisable(true);
        } else {
            openImageButton.setOnAction(event -> Utils.showImageFrame(getCreature()));
        }

        initiativeTextField.setText(Float.toString(creature.getInitiative()));
        healthTextField.setText(Integer.toString(creature.getHealth()));
        armorClassTextField.setText(Integer.toString(creature.getArmorClass()));
        statusTextArea.setText(creature.getStatusNotes());

        addListeners();
    }

    private void addListeners() {
        initiativeTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            float initiative = 0;
            try {
                initiative = Float.parseFloat(newValue);
            } catch (Exception e) {
                initiative = 0;
                LOGGER.error("ERROR while parsing initiative, set to 0", e);
            }
            creature.setInitiative(initiative);
            if (newValue.length() > MAXIMUMNUMBERS) {
                initiativeTextField.setText(initiativeTextField.getText(0, MAXIMUMNUMBERS));
            }
        });
        healthTextField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                int health = 0;
                try {
                    health = Integer.parseInt(healthTextField.getText());
                } catch (Exception e) {
                    health = 0;
                    LOGGER.error("ERROR while parsing health, set to 0", e);
                }
                creature.setHealth(health);
            }

        });
        healthTextField.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                int health = 0;
                try {
                    health = Integer.parseInt(healthTextField.getText());
                } catch (Exception e) {
                    health = 0;
                    LOGGER.error("ERROR while parsing health, set to 0", e);
                }
                creature.setHealth(health);

            }
        });
        healthTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.length() > MAXIMUMNUMBERS) {
                healthTextField.setText(healthTextField.getText(0, MAXIMUMNUMBERS));
            }
        });

        armorClassTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            int armorClass = 0;
            try {
                armorClass = Integer.parseInt(newValue);
            } catch (Exception e) {
                armorClass = 0;
                LOGGER.error("ERROR while parsing armorclass, set to 0", e);
            }
            creature.setArmorClass(armorClass);
            if (newValue.length() > MAXIMUMNUMBERS) {
                armorClassTextField.setText(armorClassTextField.getText(0, MAXIMUMNUMBERS));
            }
        });

        statusTextArea.textProperty().addListener((obs, oldValue, newValue) -> creature.setStatusNotes(newValue));
    }

    @FXML
    public void initialize() {
        openImageButton.textProperty().bind(creature.getName());
    }

    public Creature getCreature() {
        return creature;
    }

}
