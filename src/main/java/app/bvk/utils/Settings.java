package app.bvk.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.encounter.Encounter;
import app.bvk.entity.Creature;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class Settings
{

    private static final Logger LOGGER = LoggerFactory.getLogger(Settings.class);
    private final String creatureFolder = System.getProperty("user.dir") + "/creatures";
    private final String imageFolder = System.getProperty("user.dir") + "/creatures/images";
    private Image imageIcon;
    private ZipFile imageZipFile;
    private ZipFile creatureZipFile;

    private final List<Creature> creatureList = new ArrayList<>();
    private Encounter encounter = new Encounter("Unnamed Encounter");
    private Stage mainStage;

    private long autoSaveInterval = 60000L;
    private boolean doAutoSave = true;
    private boolean autosave = false;

    private static Settings instance;

    private Settings()
    {
    }

    public static synchronized Settings getInstance()
    {
        if (instance == null)
        {
            instance = new Settings();
        }
        return instance;
    }

    public Image getImageIcon()
    {
        return this.imageIcon;
    }

    public void setImageIcon(final Image imageIcon)
    {
        this.imageIcon = imageIcon;
    }

    public ZipFile getImageZipFile()
    {
        return this.imageZipFile;
    }

    public void setImageZipFile(final ZipFile imageZipFile)
    {
        this.imageZipFile = imageZipFile;
    }

    public Encounter getEncounter()
    {
        return this.encounter;
    }

    public void setEncounter(final Encounter encounter)
    {
        this.encounter = encounter;
    }

    public Stage getMainStage()
    {
        return this.mainStage;
    }

    public void setMainStage(final Stage mainStage)
    {
        this.mainStage = mainStage;
    }

    public boolean isDoAutoSave()
    {
        return this.doAutoSave;
    }

    public void setDoAutoSave(final boolean doAutoSave)
    {
        this.doAutoSave = doAutoSave;
    }

    public boolean isAutosave()
    {
        return this.autosave;
    }

    public void setAutosave(final boolean autosave)
    {
        this.autosave = autosave;
    }

    public String getImageFolder()
    {
        return this.imageFolder;
    }

    public List<Creature> getCreatureList()
    {
        return this.creatureList;
    }

    public long getAutoSaveInterval()
    {
        return this.autoSaveInterval;
    }

    public void saveLibrary()
    {
        final File creatureFile = new File(this.getCreatureFolder(), "creatures.zip");
        try
        {
            if (creatureFile.exists())
            {
                LOGGER.info("Delete Existing zip file file {}", creatureFile.getAbsolutePath());
                final boolean wasDeleted = creatureFile.delete();
                LOGGER.debug("Existing creature zip was deleted? {}", wasDeleted);
            }

            final File creatureFolderTemp = new File(this.getCreatureFolder(), "creaturesTemp");
            if (!creatureFolderTemp.exists())
            {
                creatureFolderTemp.mkdir();
            }
            final ArrayList<File> creatureFiles = new ArrayList<>();
            for (final Creature c : this.getCreatureList())
            {
                final File creatureJson = new File(creatureFolderTemp.getAbsolutePath(), c.getName().get() + ".json");
                if (!creatureJson.exists())
                {
                    final boolean jsonCreationSuccessful = creatureJson.createNewFile();
                    LOGGER.trace("Could create creature json for creature {}? {}", c.getName(), jsonCreationSuccessful);
                }
                c.writeJsonToFile(creatureJson);
                creatureFiles.add(creatureJson);
            }

            final ZipFile zipFile = new ZipFile(creatureFile);
            final ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
            zipFile.createZipFile(creatureFiles, parameters);
            for (final File tempFile : creatureFolderTemp.listFiles())
            {
                final boolean couldDeleteTempFile = tempFile.delete();
                LOGGER.trace("Could delete temp File {}? {}", tempFile.getName(), couldDeleteTempFile);
            }
            final boolean couldDeleteTempFolder = creatureFolderTemp.delete();
            LOGGER.debug("Could delete temp folder?{}", couldDeleteTempFolder);
        }
        catch (ZipException | IOException e)
        {
            LOGGER.error("", e);
        }
    }

    public String getCreatureFolder()
    {
        return this.creatureFolder;
    }

    /**
     * @return the creatureZipFile
     */
    public ZipFile getCreatureZipFile()
    {
        return this.creatureZipFile;
    }

    /**
     * @param creatureZipFile
     *            the creatureZipFile to set
     */
    public void setCreatureZipFile(final ZipFile creatureZipFile)
    {
        this.creatureZipFile = creatureZipFile;
    }

}
