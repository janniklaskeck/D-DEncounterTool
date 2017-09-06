package app.bvk.encounter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.entity.Creature;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class EncounterEntry extends BorderPane
{

    private static final Logger LOGGER = LoggerFactory.getLogger(EncounterEntry.class);

    private static final int MAXIMUM_NUMBERS = 5;
    private Creature creature;

    @FXML
    private TextField initiativeTextField;

    @FXML
    private TextField healthTextField;

    @FXML
    private TextField armorClassTextField;

    @FXML
    private TextField noteTextField;

    @FXML
    private HBox noteHBox;

    @FXML
    private Label creatureNameLabel;

    private BooleanProperty creatureSelected = new SimpleBooleanProperty();
    private Background defaultBackground;

    public EncounterEntry(final Creature creature)
    {
        this.creature = creature;
        final FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("EncounterEntryGui.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try
        {
            loader.load();
        }
        catch (final IOException e)
        {
            LOGGER.error("ERROR while loading encounterentry fxml", e);
        }
    }

    @FXML
    public void initialize()
    {
        this.defaultBackground = this.getBackground();
        this.creatureNameLabel.textProperty().bind(this.creature.nameProperty());
        this.setupCreature();
        this.addListeners();
    }

    private void setupCreature()
    {
        if (this.creature.isSelected())
        {
            this.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        this.initiativeTextField.setText(Float.toString(this.creature.getInitiative()));
        this.healthTextField.setText(Integer.toString(this.creature.getHealth()));
        this.armorClassTextField.setText(Integer.toString(this.creature.getArmorClass()));
        this.noteTextField.setText(this.creature.getNotes());
    }

    private void addListeners()
    {
        this.armorClassTextField.textProperty().addListener((obs, oldValue, newValue) -> this.parseArmorClass(newValue));
        this.noteTextField.textProperty().addListener((obs, oldValue, newValue) -> this.creature.setNotes(newValue));
        this.initiativeTextField.textProperty().addListener((obs, oldValue, newValue) -> this.parseInitiative(newValue));
        this.healthTextField.textProperty().addListener((obs, oldValue, newValue) -> this.parseHealth(newValue));
        this.creatureSelected.bind(this.creature.selectedProperty());
        this.creatureSelected.addListener((obs, oldValue, newValue) ->
        {
            if (newValue)
            {
                this.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
            }
            else
            {
                this.setBackground(this.defaultBackground);
            }
        });
    }

    private void parseHealth(final String healthString)
    {
        int health = 0;
        try
        {
            health = Integer.parseInt(healthString);
        }
        catch (final NumberFormatException e)
        {
            health = 0;
            LOGGER.error("ERROR while parsing health, set to 0", e);
        }
        this.creature.setHealth(health);
        if (healthString.length() > MAXIMUM_NUMBERS)
        {
            this.healthTextField.setText(this.healthTextField.getText(0, MAXIMUM_NUMBERS));
        }
    }

    private void parseInitiative(final String initiativeString)
    {
        float initiative = 0;
        try
        {
            initiative = Float.parseFloat(initiativeString);
        }
        catch (final NumberFormatException e)
        {
            initiative = 0;
            LOGGER.error("ERROR while parsing initiative, set to 0", e);
        }
        this.creature.setInitiative(initiative);
        if (initiativeString.length() > MAXIMUM_NUMBERS)
        {
            this.initiativeTextField.setText(this.initiativeTextField.getText(0, MAXIMUM_NUMBERS));
        }
    }

    private void parseArmorClass(final String armorClassString)
    {
        int armorClass = 0;
        try
        {
            armorClass = Integer.parseInt(armorClassString);
        }
        catch (final NumberFormatException e)
        {
            armorClass = 0;
            LOGGER.error("ERROR while parsing armorclass, set to 0", e);
        }
        this.creature.setArmorClass(armorClass);
        if (armorClassString.length() > MAXIMUM_NUMBERS)
        {
            this.armorClassTextField.setText(this.armorClassTextField.getText(0, MAXIMUM_NUMBERS));
        }
    }

    public Creature getCreature()
    {
        return this.creature;
    }

}
