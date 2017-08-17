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

public class EncounterEntry extends GridPane
{ // NOSONAR

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

    public EncounterEntry(final Creature c)
    {
        this.creature = c;
        this.loader = new FXMLLoader(this.getClass().getClassLoader().getResource("EncounterEntry.fxml"));
        this.loader.setRoot(this);
        this.loader.setController(this);
        try
        {
            this.loader.load();
        }
        catch (final IOException e)
        {
            LOGGER.error("ERROR while loading encounterentry fxml", e);
        }
        this.setupCreature();
    }

    private void setupCreature()
    {
        if (this.creature.isSelected())
        {
            this.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        if (this.creature.getClass().equals(Player.class))
        {
            this.openImageButton.setDisable(true);
        }
        else
        {
            this.openImageButton.setOnAction(event -> Utils.showImageFrame(this.getCreature()));
        }

        this.initiativeTextField.setText(Float.toString(this.creature.getInitiative()));
        this.healthTextField.setText(Integer.toString(this.creature.getHealth()));
        this.armorClassTextField.setText(Integer.toString(this.creature.getArmorClass()));
        this.statusTextArea.setText(this.creature.getNotes());

        this.addListeners();
    }

    private void addListeners()
    {
        this.initiativeTextField.textProperty().addListener((obs, oldValue, newValue) ->
        {
            float initiative = 0;
            try
            {
                initiative = Float.parseFloat(newValue);
            }
            catch (final Exception e)
            {
                initiative = 0;
                LOGGER.error("ERROR while parsing initiative, set to 0", e);
            }
            this.creature.setInitiative(initiative);
            if (newValue.length() > MAXIMUMNUMBERS)
            {
                this.initiativeTextField.setText(this.initiativeTextField.getText(0, MAXIMUMNUMBERS));
            }
        });
        this.healthTextField.focusedProperty().addListener((obs, oldValue, newValue) ->
        {
            if (!newValue)
            {
                int health = 0;
                try
                {
                    health = Integer.parseInt(this.healthTextField.getText());
                }
                catch (final Exception e)
                {
                    health = 0;
                    LOGGER.error("ERROR while parsing health, set to 0", e);
                }
                this.creature.setHealth(health);
            }

        });
        this.healthTextField.setOnKeyPressed(key ->
        {
            if (key.getCode().equals(KeyCode.ENTER))
            {
                int health = 0;
                try
                {
                    health = Integer.parseInt(this.healthTextField.getText());
                }
                catch (final Exception e)
                {
                    health = 0;
                    LOGGER.error("ERROR while parsing health, set to 0", e);
                }
                this.creature.setHealth(health);

            }
        });
        this.healthTextField.textProperty().addListener((obs, oldValue, newValue) ->
        {
            if (newValue.length() > MAXIMUMNUMBERS)
            {
                this.healthTextField.setText(this.healthTextField.getText(0, MAXIMUMNUMBERS));
            }
        });

        this.armorClassTextField.textProperty().addListener((obs, oldValue, newValue) ->
        {
            int armorClass = 0;
            try
            {
                armorClass = Integer.parseInt(newValue);
            }
            catch (final Exception e)
            {
                armorClass = 0;
                LOGGER.error("ERROR while parsing armorclass, set to 0", e);
            }
            this.creature.setArmorClass(armorClass);
            if (newValue.length() > MAXIMUMNUMBERS)
            {
                this.armorClassTextField.setText(this.armorClassTextField.getText(0, MAXIMUMNUMBERS));
            }
        });

        this.statusTextArea.textProperty().addListener((obs, oldValue, newValue) -> this.creature.setNotes(newValue));
    }

    @FXML
    public void initialize()
    {
        this.openImageButton.textProperty().bind(this.creature.getName());
    }

    public Creature getCreature()
    {
        return this.creature;
    }

}
