package app.bvk.encounter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import app.bvk.entity.Creature;

public class EncounterUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EncounterUtils.class);

    private EncounterUtils()
    {
    }

    public static boolean saveEncounterToFile(final Path targetFile, final Encounter encounterToSave)
    {
        try (JsonWriter jsonWriter = new JsonWriter(new FileWriter(targetFile.toFile()));)
        {
            jsonWriter.setIndent("  ");
            jsonWriter.beginArray();
            jsonWriter.beginObject();
            final String encounterName = "".equals(encounterToSave.nameProperty().get()) ? "Unnamed Encounter" : encounterToSave.nameProperty().get();
            jsonWriter.name("encounterName").value(encounterName);
            jsonWriter.endObject();
            jsonWriter.beginArray();
            for (final EncounterEntry creatureEntry : encounterToSave.getCreatureList())
            {
                final Creature creature = creatureEntry.getCreature();
                jsonWriter.jsonValue(creature.getAsJson());
            }
            jsonWriter.endArray();
            jsonWriter.endArray();
            jsonWriter.close();
        }
        catch (final IOException e)
        {
            LOGGER.error("ERROR while save encounter to file", e);
            return false;
        }
        return true;
    }

    public static Encounter loadEncounterFromFile(final Path sourceFile)
    {
        final Encounter newEncounter = new Encounter("unnamed");
        try (FileInputStream fileInputStream = new FileInputStream(sourceFile.toFile());
                final InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);)
        {
            final StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                builder.append(line);
            }
            bufferedReader.close();
            final Gson g = new Gson();
            final JsonArray jArray = g.fromJson(builder.toString(), JsonArray.class);
            newEncounter.nameProperty().setValue(jArray.get(0).getAsJsonObject().get("encounterName").getAsString());

            final JsonArray creatureArray = jArray.get(1).getAsJsonArray();
            for (final JsonElement je : creatureArray)
            {
                final JsonObject jo = je.getAsJsonObject();
                final Creature creature = new Creature(jo);
                newEncounter.addCreatureEntry(creature);
            }
        }
        catch (final IOException e)
        {
            LOGGER.error("ERROR while reading encounter save file", e);
        }
        return newEncounter;
    }

    public static boolean autoSaveEncounter(final Encounter encounterToSave)
    {
        Path filePath = encounterToSave.getAutoSavePath();
        if (filePath != null)
        {
            final boolean deletionSuccessful = filePath.toFile().delete();
            LOGGER.debug("Deletion of auto save file was successful? {}", deletionSuccessful);
        }
        final String date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        filePath = Paths.get(System.getProperty("user.dir") + "\\saves\\" + encounterToSave.nameProperty().get() + "-" + date.replace(":", ".") + ".ddesav");
        filePath.toFile().getParentFile().mkdirs();
        try
        {
            final boolean creationSuccessful = filePath.toFile().createNewFile();
            LOGGER.debug("Creation of autosave file was successful? {}", creationSuccessful);
        }
        catch (final IOException e)
        {
            LOGGER.error("ERROR while performing autosave", e);
            return false;
        }
        saveEncounterToFile(filePath, encounterToSave);
        return true;
    }
}
