package app.bvk.library;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public final class CreatureLibrary
{

    private static final Logger LOGGER = LoggerFactory.getLogger(CreatureLibrary.class);
    private static final String LIBRARY_FILE_NAME = "library.zip";
    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final Path CREATURE_FOLDER = Paths.get(System.getProperty("user.dir"), "creatures");
    private final List<Creature> creatureList = new ArrayList<>();

    private ZipFile libraryZipFile;

    private static CreatureLibrary instance;

    private CreatureLibrary()
    {
        this.init(CREATURE_FOLDER);
    }

    private boolean init(final Path libraryFolder)
    {
        final Path libraryFilePath = libraryFolder.resolve(LIBRARY_FILE_NAME);
        if (!libraryFilePath.toFile().exists())
        {
            LOGGER.error("'{}' does not exist!", libraryFilePath);
            return false;
        }
        try
        {
            this.libraryZipFile = new ZipFile(libraryFilePath.toFile());
        }
        catch (final ZipException e)
        {
            LOGGER.error("Error while loading zip file!", e);
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
        try
        {
            @SuppressWarnings("unchecked")
            final List<FileHeader> fileHeaders = this.libraryZipFile.getFileHeaders();
            for (final FileHeader fh : fileHeaders)
            {
                final String fileName = fh.getFileName();
                final InputStream fileInputStream = this.libraryZipFile.getInputStream(fh);
                if (fileName.toLowerCase().endsWith("json"))
                {
                    final String fileContent = new BufferedReader(new InputStreamReader(fileInputStream)).lines().parallel().collect(Collectors.joining("\n"));
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
                else if (fileName.toLowerCase().endsWith("png"))
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
                else
                {
                    LOGGER.debug("Unknown FileType for File '{}'.", fileName);
                }
            }
        }
        catch (final ZipException e)
        {
            LOGGER.error("Error while reading zip entries", e);
        }
    }

    public void saveCreatures()
    {
        if (this.creatureList.isEmpty())
        {
            return;
        }
        final File creatureFolderTemp = Paths.get(System.getProperty("java.io.tmpdir"), "dndEncounterTemp").toFile();
        if (!creatureFolderTemp.exists())
        {
            creatureFolderTemp.mkdir();
        }
        final ArrayList<File> creatureFiles = new ArrayList<>();
        for (final Creature creature : this.creatureList)
        {
            try
            {
                final String creatureFileName = String.format("%s.json", creature.getName().getValue());
                LOGGER.debug("Remove File '{}' from ZipFile.", creatureFileName);
                final FileHeader fileHeader = this.libraryZipFile.getFileHeader(creatureFileName);
                this.libraryZipFile.removeFile(fileHeader);
            }
            catch (final ZipException e)
            {
                LOGGER.error("Creature File not existing in ZipFile!", e);
            }
            final File creatureJson = Paths.get(creatureFolderTemp.getAbsolutePath(), creature.getName().get() + ".json").toFile();
            if (!creatureJson.exists())
            {
                boolean jsonCreationSuccessful = false;
                try
                {
                    jsonCreationSuccessful = creatureJson.createNewFile();
                }
                catch (final IOException e)
                {
                    LOGGER.error("Could not create new creature json file!", e);
                }
                LOGGER.trace("Could create creature json for creature {}? {}", creature.getName(), jsonCreationSuccessful);
            }
            creature.writeJsonToFile(creatureJson);
            creatureFiles.add(creatureJson);
        }
        final ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
        for (final File file : creatureFiles)
        {
            try
            {
                this.libraryZipFile.addFile(file, parameters);
            }
            catch (final ZipException e)
            {
                LOGGER.error("Could not add File to ZipFile!", e);
            }
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

    public ZipFile getZipFile()
    {
        return this.libraryZipFile;
    }

    public void addCreature(final Creature creature)
    {
        this.creatureList.add(creature);
    }
}
