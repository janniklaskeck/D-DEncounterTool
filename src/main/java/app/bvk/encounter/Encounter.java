package app.bvk.encounter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import app.bvk.entity.Creature;
import app.bvk.utils.Settings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Encounter
{

    private static final Logger LOGGER = LoggerFactory.getLogger(Encounter.class);
    private ArrayList<Creature> creatureList;
    private int currentIndex = 0;
    private File file;
    private StringProperty encounterNameProperty;

    public Encounter(final String name)
    {
        this.encounterNameProperty = new SimpleStringProperty(name);
        this.creatureList = new ArrayList<>();
    }

    public void reset()
    {
        this.creatureList.clear();
    }

    public void addCreature(final Creature creature)
    {
        this.creatureList.add(creature);
        for (final Creature c : this.getCreatureList())
        {
            c.setSelected(false);
        }
        if (this.getCreatureList().size() - 1 >= this.getCurrentIndex())
        {
            this.getCreatureList().get(this.getCurrentIndex()).setSelected(true);
        }
        else
        {
            this.getCreatureList().get(0).setSelected(true);
            this.setCurrentIndex(0);
        }
    }

    public void addCreature(final String name)
    {
        for (final Creature c : Settings.getInstance().getCreatureList())
        {
            if (c.getName().get().equals(name))
            {
                this.creatureList.add(new Creature(c));
                break;
            }
        }
        for (final Creature c : this.getCreatureList())
        {
            c.setSelected(false);
        }
        if (this.getCreatureList().size() - 1 >= this.getCurrentIndex())
        {
            this.getCreatureList().get(this.getCurrentIndex()).setSelected(true);
        }
        else
        {
            this.getCreatureList().get(0).setSelected(true);
            this.setCurrentIndex(0);
        }
    }

    public void copy(final int index)
    {
        this.getCreatureList().add(new Creature(this.getCreatureList().get(index)));
    }

    /**
     * @return the creatureList
     */
    public List<Creature> getCreatureList()
    {
        return this.creatureList;
    }

    public ObservableList<EncounterEntry> getObsList()
    {
        final ArrayList<EncounterEntry> eeList = new ArrayList<>();
        for (final Creature c : this.getCreatureList())
        {
            eeList.add(new EncounterEntry(c));
        }
        return FXCollections.observableArrayList(eeList);
    }

    private void updateIndex()
    {
        for (int i = 0; i < this.getCreatureList().size(); i++)
        {
            if (this.getCreatureList().get(i).isSelected())
            {
                this.setCurrentIndex(i);
            }
        }
    }

    public void sort()
    {
        this.getCreatureList().sort((o1, o2) ->
        {
            if (o1.getInitiative() < o2.getInitiative())
            {
                return 1;
            }
            else if (o1.getInitiative() > o2.getInitiative())
            {
                return -1;
            }
            return 0;
        });
        this.updateIndex();
    }

    public int getCurrentIndex()
    {
        return this.currentIndex;
    }

    public void setCurrentIndex(final int currentIndex)
    {
        this.currentIndex = currentIndex;
        for (final Creature c : this.getCreatureList())
        {
            c.setSelected(false);
        }
        this.getCreatureList().get(this.getCurrentIndex()).setSelected(true);
    }

    public void setNextIndex()
    {
        if (this.currentIndex + 1 > this.creatureList.size() - 1)
        {
            this.currentIndex = 0;
        }
        else
        {
            this.currentIndex++;
        }
        this.setCurrentIndex(this.currentIndex);
    }

    public void setLastIndex()
    {
        if (this.currentIndex - 1 < 0)
        {
            this.currentIndex = this.creatureList.size() - 1;
        }
        else
        {
            this.currentIndex--;
        }
        this.setCurrentIndex(this.currentIndex);
    }

    public void saveToFile(final File file)
    {

        try (JsonWriter jsonWriter = new JsonWriter(new FileWriter(file));)
        {
            jsonWriter.setIndent("  ");
            jsonWriter.beginArray();
            jsonWriter.beginObject();
            jsonWriter.name("encounterName")
                    .value("".equals(this.encounterNameProperty.get()) ? "Unnamed Encounter" : this.encounterNameProperty.get());
            jsonWriter.endObject();
            jsonWriter.beginArray();
            for (final Creature c : this.getCreatureList())
            {
                jsonWriter.beginObject();
                jsonWriter.name("name").value(c.getName().get());
                jsonWriter.name("imagePath").value(c.getImagePath());
                jsonWriter.name("initiative").value(c.getInitiative());
                jsonWriter.name("health").value(c.getHealth());
                jsonWriter.name("armorClass").value(c.getArmorClass());
                jsonWriter.name("statusNotes").value(c.getNotes());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
            jsonWriter.endArray();
            jsonWriter.close();
        }
        catch (final Exception e)
        {
            LOGGER.error("ERROR while save encounter to file", e);
        }
    }

    public void autoSave()
    {
        if (this.file != null)
        {
            final boolean deletionSuccessful = this.file.delete();
            LOGGER.debug("Deletion of auto save file was successful? {}", deletionSuccessful);
        }
        final String date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.file = new File(
                System.getProperty("user.dir") + "\\saves\\" + this.encounterNameProperty.get() + "-" + date.replace(":", ".") + ".ddesav");
        this.file.getParentFile().mkdirs();
        try
        {
            final boolean creationSuccessful = this.file.createNewFile();
            LOGGER.debug("Creation of autosave file was successful? {}", creationSuccessful);
        }
        catch (final IOException e)
        {
            LOGGER.error("ERROR while performing autosave", e);
        }
        this.saveToFile(this.file);
    }

    public void readFromFile(final File file)
    {

        this.reset();
        try (FileInputStream fis = new FileInputStream(file);)
        {
            final InputStreamReader isr = new InputStreamReader(fis);
            final BufferedReader bufferedReader = new BufferedReader(isr);
            final StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                sb.append(line);
            }
            bufferedReader.close();
            final Gson g = new Gson();
            final JsonArray jArray = g.fromJson(sb.toString(), JsonArray.class);
            this.setEncounterName(jArray.get(0).getAsJsonObject().get("encounterName").getAsString());

            final JsonArray creatureArray = jArray.get(1).getAsJsonArray();
            for (final JsonElement je : creatureArray)
            {
                final JsonObject jo = je.getAsJsonObject();
                final Creature c = new Creature(jo);
                this.getCreatureList().add(c);
            }
        }
        catch (final IOException e)
        {
            LOGGER.error("ERROR while reading encounter save file", e);
        }
        this.setCurrentIndex(0);
    }

    public void setEncounterName(final String encounterName)
    {
        this.getEncounterNameProperty().set(encounterName);
    }

    public void remove(final int selectedIndex)
    {
        this.getCreatureList().remove(selectedIndex);
        if (selectedIndex == this.currentIndex && !this.getCreatureList().isEmpty())
        {
            for (final Creature c : this.getCreatureList())
            {
                c.setSelected(false);
            }
            this.getCreatureList().get(this.getCurrentIndex()).setSelected(true);
        }
    }

    public StringProperty getEncounterNameProperty()
    {
        return this.encounterNameProperty;
    }
}
