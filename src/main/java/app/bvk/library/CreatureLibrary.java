package app.bvk.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import app.bvk.entity.Creature;
import app.bvk.entity.Monster;
import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileReader;
import de.schlichtherle.truezip.nio.file.TPath;

public final class CreatureLibrary
{

    private static final Logger LOGGER = LoggerFactory.getLogger(CreatureLibrary.class);
    private static final String LIBRARY_FILE_NAME = "library.zip";
    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final Path CREATURE_FOLDER = Paths.get(System.getProperty("user.dir"), "creatures");
    private final List<Creature> creatureList = new ArrayList<>();

    private Path libraryFilePath;

    private static CreatureLibrary instance;

    private CreatureLibrary()
    {
        final boolean success = this.init(CREATURE_FOLDER);
        LOGGER.debug("Loading Library Success: {}.", success);
    }

    private boolean init(final Path libraryFolder)
    {
        this.libraryFilePath = libraryFolder.resolve(LIBRARY_FILE_NAME);
        if (!this.getLibraryFilePath().toFile().exists())
        {
            LOGGER.error("'{}' does not exist!", this.getLibraryFilePath());
            return false;
        }
        this.loadCreatures();
        return true;
    }

    public static synchronized CreatureLibrary getInstance()
    {
        if (instance == null)
        {
            instance = new CreatureLibrary();
        }
        return instance;
    }

    private void loadCreatures()
    {

        final TFile zipFile = new TFile(this.getLibraryFilePath().toFile());
        for (final TFile file : zipFile.listFiles())
        {
            final String fileName = file.getName();
            if (fileName.toLowerCase().endsWith("json"))
            {
                this.readCreatureJson(file);
            }
            else if (fileName.toLowerCase().endsWith("png"))
            {
                this.readCreatureImage(fileName);
            }
            else
            {
                LOGGER.debug("Unknown FileType for File '{}'.", fileName);
            }
        }
        LOGGER.debug("Loaded {} Creatures.", this.creatureList.size());
    }

    private void readCreatureJson(final TFile file)
    {
        try (final BufferedReader reader = new BufferedReader(new TFileReader(file));)
        {
            final String fileContent = reader.lines().parallel().collect(Collectors.joining("\n"));
            final JsonObject creatureData = JSON_PARSER.parse(fileContent).getAsJsonObject();
            final Monster monster = new Monster(creatureData);
            final Creature creature = this.getCreature(monster.getName().getValue());
            if (creature == null)
            {
                this.creatureList.add(monster);
            }
            else
            {
                creature.load(creatureData);
            }
        }
        catch (final IOException e)
        {
            LOGGER.error("Error while reading zip entries", e);
        }
    }

    private void readCreatureImage(final String fileName)
    {
        final String creatureName = fileName.split("\\.")[0];
        final Creature creature = this.getCreature(creatureName);
        if (creature == null)
        {
            this.creatureList.add(new Monster(creatureName, fileName));
        }
        else
        {
            creature.setImagePath(fileName);
        }
    }

    public void saveCreatures()
    {
        if (this.creatureList.isEmpty())
        {
            return;
        }
        final TFile zipFile = new TFile(this.getLibraryFilePath().toFile());
        for (final TFile file : zipFile.listFiles())
        {
            if (file.getName().toLowerCase().endsWith(".json"))
            {
                try
                {
                    Files.delete(new TPath(file));
                }
                catch (final IOException e)
                {
                    LOGGER.error("Can't delete File from Archive.", e);
                }
            }
        }

        for (final Creature creature : this.creatureList)
        {
            final TFile creatureFile = new TFile(new TPath(zipFile).resolve(String.format("%s.json", creature.getName().getValue())).toFile());
            if (!creatureFile.exists())
            {
                boolean jsonCreationSuccessful = false;
                try
                {
                    jsonCreationSuccessful = creatureFile.createNewFile();
                }
                catch (final IOException e)
                {
                    LOGGER.error("Could not create new creature json file!", e);
                }
                LOGGER.trace("Could create creature json for creature {}? {}", creature.getName(), jsonCreationSuccessful);
            }
            creature.writeJsonToFile(creatureFile);
        }
    }

    /**
     * Get a creature by name
     *
     * @param name
     *            of the creature
     * @return the creature if it exists in the library or null if not
     */
    public Creature getCreature(final String name)
    {
        final Optional<Creature> creatureOptional = this.creatureList.stream().filter(creature -> creature.getName().getValue().equalsIgnoreCase(name))
                .findFirst();
        if (creatureOptional.isPresent())
        {
            return creatureOptional.get();
        }
        return null;
    }

    public List<Creature> getCreatures()
    {
        return this.creatureList;
    }

    public void addCreature(final Creature creature)
    {
        this.creatureList.add(creature);
    }

    public Path getLibraryFilePath()
    {
        return this.libraryFilePath;
    }
}
