package encounter;

import java.io.IOException;

import entity.Creature;
import entity.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import utils.Utils;

public class EncounterEntry extends GridPane {

    private static final int MAXIMUMNUMBERS = 5;
    private Creature creature;
    private FXMLLoader loader;

    @FXML
    private Label name;

    @FXML
    private TextField initiativeTextField;

    @FXML
    private TextField healthTextField;

    @FXML
    private TextField armorClassTextField;

    @FXML
    private TextArea statusTextArea;

    @FXML
    private TextArea miscTextArea;

    @FXML
    private Button openImageButton;

    public EncounterEntry(Creature c) {
        this.creature = c;
        loader = new FXMLLoader(getClass().getResource("EncounterEntry.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setupCreature();
    }

    private void setupCreature() {
        if (creature.isSelected()) {
            setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        if (creature.getClass().equals(Player.class)) {
            openImageButton.setDisable(true);
        }

        initiativeTextField.setText(Float.toString(creature.getInitiative()));
        healthTextField.setText(Integer.toString(creature.getHealth()));
        armorClassTextField.setText(Integer.toString(creature.getArmorClass()));
        statusTextArea.setText(creature.getStatusNotes());
        miscTextArea.setText(creature.getMiscNotes());

        addListeners();
    }

    private void addListeners() {
        initiativeTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            float initiative = 0;
            try {
                initiative = Float.parseFloat(newValue);
            } catch (Exception e1) {
                initiative = 0;
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
                } catch (Exception e1) {
                    health = 0;
                    e1.printStackTrace();
                }
                creature.setHealth(health);
            }

        });
        healthTextField.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                int health = 0;
                try {
                    health = Integer.parseInt(healthTextField.getText());
                } catch (Exception e1) {
                    health = 0;
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
            } catch (Exception e1) {
                armorClass = 0;
            }
            creature.setArmorClass(armorClass);
            if (newValue.length() > MAXIMUMNUMBERS) {
                armorClassTextField.setText(armorClassTextField.getText(0, MAXIMUMNUMBERS));
            }
        });

        statusTextArea.textProperty().addListener((obs, oldValue, newValue) -> creature.setStatusNotes(newValue));

        miscTextArea.textProperty().addListener((obs, oldValue, newValue) -> creature.setMiscNotes(newValue));
    }

    @FXML
    private void initialize() {
        name.setText(creature.getName());
    }

    @FXML
    private void openImage() {
        Utils.showImageFrame(getCreature());
    }

    public Creature getCreature() {
        return creature;
    }

}
