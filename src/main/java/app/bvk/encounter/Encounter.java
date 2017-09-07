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
    private final ObservableList<EncounterEntry> creatureEntryList = FXCollections.observableArrayList();
    private final ObjectProperty<ObservableList<EncounterEntry>> listProperty = new SimpleObjectProperty<>(this.creatureEntryList);
    private final StringProperty encounterNameProperty = new SimpleStringProperty("unnamed");
    private final ObjectProperty<EncounterEntry> currentSelectedCreature = new SimpleObjectProperty<>();
    private Path autoSaveFilePath;

    public Encounter(final String name)
    {
        this.encounterNameProperty.setValue(name);
        this.creatureEntryList.addListener((ListChangeListener<EncounterEntry>) change ->
        {
            if (this.creatureEntryList.size() == 1)
            {
                final EncounterEntry onlyCreature = this.creatureEntryList.get(0);
                this.currentSelectedCreature.setValue(onlyCreature);
            }
        });
        this.currentSelectedCreature.addListener((obs, oldValue, newValue) ->
        {
            if (oldValue != null)
            {
                this.deselectCreature(oldValue);
                LOGGER.trace("Deselect Creature {}.", oldValue.getCreature().getName());
            }
            if (newValue != null)
            {
                this.selectCreature(newValue);
                LOGGER.trace("Select Creature {}.", newValue.getCreature().getName());
            }
        });
    }

    public void reset()
    {
        this.creatureEntryList.clear();
        this.currentSelectedCreature.set(null);
        LOGGER.debug("Reset Encounter {}.", this.encounterNameProperty.getValue());
    }

    public void addCreatureEntry(final EncounterEntry entry)
    {
        this.creatureEntryList.add(entry);
        LOGGER.debug("Add Creature {} to Encounter.", entry.getCreature().getName());
    }

    public void addCreatureEntry(final Creature creature)
    {
        this.creatureEntryList.add(new EncounterEntry(creature));
        LOGGER.debug("Add Creature {} to Encounter.", creature.getName());
    }

    public void removeCreature(final int index)
    {
        final EncounterEntry removedCreature = this.creatureEntryList.remove(index);
        LOGGER.debug("Removed Creature {} from encounter.", removedCreature.getCreature().getName());
    }

    public void copyCreature(final int index)
    {
        final EncounterEntry creatureToCopy = this.creatureEntryList.get(index);
        final EncounterEntry copiedCreatureEntry = new EncounterEntry(creatureToCopy.getCreature());
        this.addCreatureEntry(copiedCreatureEntry);
        LOGGER.debug("Copied Creature {}.", creatureToCopy.getCreature().getName());
    }

    private void deselectCreature(final EncounterEntry entry)
    {
        entry.selectedProperty().setValue(false);
    }

    private void selectCreature(final EncounterEntry entry)
    {
        entry.selectedProperty().setValue(true);
        MainController.pane.updateCreaturePreview(entry.getCreature());
    }

    public void sort()
    {
        this.creatureEntryList.sort((entry1, entry2) ->
        {
            final Creature creature1 = entry1.getCreature();
            final Creature creature2 = entry2.getCreature();
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
        if (nextIndex > this.creatureEntryList.size() - 1)
        {
            nextIndex = 0;
        }
        this.currentSelectedCreature.setValue(this.creatureEntryList.get(nextIndex));
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
            previousIndex = this.creatureEntryList.size() - 1;
        }
        this.currentSelectedCreature.setValue(this.creatureEntryList.get(previousIndex));
    }

    public int getIndexOfSelectedCreature()
    {
        for (int index = 0; index < this.creatureEntryList.size(); index++)
        {
            if (this.creatureEntryList.get(index) == this.currentSelectedCreature.getValue())
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

    public ObjectProperty<ObservableList<EncounterEntry>> listProperty()
    {
        return this.listProperty;
    }

    public ObservableList<EncounterEntry> getCreatureList()
    {
        return this.creatureEntryList;
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
