package app.bvk.encounter;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.entity.Creature;
import app.bvk.gui.MainController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class Encounter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Encounter.class);
    private final ObservableList<Creature> creatureList = FXCollections.observableArrayList();
    private final StringProperty encounterNameProperty = new SimpleStringProperty("unnamed");
    private final ObjectProperty<Creature> currentSelectedCreature = new SimpleObjectProperty<>();
    private Path autoSaveFilePath;

    public Encounter(final String name)
    {
        this.encounterNameProperty.setValue(name);
        this.creatureList.addListener((ListChangeListener<Creature>) change ->
        {
            if (this.creatureList.size() == 1)
            {
                final Creature onlyCreature = this.creatureList.get(0);
                this.currentSelectedCreature.setValue(onlyCreature);
            }
        });
        this.currentSelectedCreature.addListener((obs, oldValue, newValue) ->
        {
            if (oldValue != null)
            {
                this.deselectCreature(oldValue);
                LOGGER.trace("Deselect Creature {}.", oldValue.nameProperty().getValue());
            }
            if (newValue != null)
            {
                this.selectCreature(newValue);
                LOGGER.trace("Select Creature {}.", newValue.nameProperty().getValue());
            }
        });
    }

    public void reset()
    {
        this.creatureList.clear();
        this.currentSelectedCreature.set(null);
        LOGGER.debug("Reset Encounter {}.", this.encounterNameProperty.getValue());
    }

    public void addCreature(final Creature creature)
    {
        this.creatureList.add(new Creature(creature));
        LOGGER.debug("Add Creature {} to Encounter.", creature.nameProperty().getValue());
    }

    public void removeCreature(final int index)
    {
        final Creature removedCreature = this.creatureList.remove(index);
        LOGGER.debug("Removed Creature {} from encounter.", removedCreature.nameProperty().getValue());
    }

    public void copyCreature(final int index)
    {
        final Creature creatureToCopy = this.creatureList.get(index);
        final Creature copiedCreature = new Creature(creatureToCopy);
        this.addCreature(copiedCreature);
        LOGGER.debug("Copied Creature {}.", creatureToCopy.nameProperty());
    }

    private void deselectCreature(final Creature creature)
    {
        creature.setSelected(false);
    }

    private void selectCreature(final Creature creature)
    {
        creature.setSelected(true);
        MainController.pane.updateCreaturePreview(creature);
    }

    public void sort()
    {
        this.creatureList.sort((creature1, creature2) ->
        {
            if (creature1.getInitiative() < creature2.getInitiative())
            {
                return 1;
            }
            else if (creature1.getInitiative() > creature2.getInitiative())
            {
                return -1;
            }
            return 0;
        });
    }

    public void selectNext()
    {
        final int currentIndex = this.getIndexOfSelectedCreature();
        if (currentIndex < 0)
        {
            LOGGER.warn("No Creature currently selected!");
            return;
        }
        int nextIndex = currentIndex + 1;
        if (nextIndex > this.creatureList.size() - 1)
        {
            nextIndex = 0;
        }
        this.currentSelectedCreature.setValue(this.creatureList.get(nextIndex));
    }

    public void selectPrevious()
    {
        final int currentIndex = this.getIndexOfSelectedCreature();
        if (currentIndex < 0)
        {
            LOGGER.warn("No Creature currently selected!");
            return;
        }
        int previousIndex = currentIndex - 1;
        if (previousIndex < 0)
        {
            previousIndex = this.creatureList.size() - 1;
        }
        this.currentSelectedCreature.setValue(this.creatureList.get(previousIndex));
    }

    public int getIndexOfSelectedCreature()
    {
        for (int index = 0; index < this.creatureList.size(); index++)
        {
            if (this.creatureList.get(index) == this.currentSelectedCreature.getValue())
            {
                return index;
            }
        }
        return -1;
    }

    public StringProperty nameProperty()
    {
        return this.encounterNameProperty;
    }

    public ObservableList<Creature> getCreatureList()
    {
        return this.creatureList;
    }

    public Path getAutoSavePath()
    {
        return this.autoSaveFilePath;
    }

    public void setAutoSavePath(final Path autoSaveFilePath)
    {
        this.autoSaveFilePath = autoSaveFilePath;
    }
}
