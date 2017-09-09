package app.bvk.entity;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import app.bvk.entity.stats.BasicStats;
import app.bvk.entity.stats.Health;
import app.bvk.gui.MainGUI;
import app.bvk.library.CreatureLibrary;
import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileInputStream;
import de.schlichtherle.truezip.file.TFileWriter;
import javafx.scene.image.Image;

public class Creature
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Creature.class);
    private static final Image DEFAULT_IMAGE = new Image(MainGUI.class.getClassLoader().getResourceAsStream("icon.png"));
    private static final Gson GSON = new Gson();

    private String imagePath;
    private String notes = "";

    private String name = "unnamed";
    private int armorClass = 0;
    private float rolledInitiative = 0;
    private Health health = new Health(0, 0, 0);
    private BasicStats stats = new BasicStats();

    private int moveSpeedGround = 30;
    private int moveSpeedAir = 0;
    private int moveSpeedWater = 15;

    private int challengeRating = 0;
    private int experience = 0;

    public Creature(final String name, final String path)
    {
        this.name = name;
        this.imagePath = path;
    }

    public Creature(final Creature creature)
    {
        this.copyCreature(creature);
    }

    public Creature(final JsonElement json)
    {
        final Creature loadedCreature = GSON.fromJson(json, this.getClass());
        this.copyCreature(loadedCreature);
    }

    public void copyCreature(final Creature creatureToCopy)
    {
        this.name = creatureToCopy.name;
        this.imagePath = creatureToCopy.getImagePath();
        this.health = creatureToCopy.getHealth();
        this.armorClass = creatureToCopy.getArmorClass();
        this.notes = creatureToCopy.getNotes();
        this.stats = creatureToCopy.getBasicStats();
    }

    public String getAsJson()
    {
        return GSON.toJson(this);
    }

    public void writeJsonToFile(final TFile file)
    {
        try (final TFileWriter tFileWriter = new TFileWriter(file);)
        {
            final String json = GSON.toJson(this);
            tFileWriter.write(json);
            tFileWriter.close();
        }
        catch (final IOException e)
        {
            LOGGER.error("Could not write Creature to JSON File.", e);
        }
    }

    public String getName()
    {
        return this.name;
    }

    public String getImagePath()
    {
        return this.imagePath;
    }

    public void setImagePath(final String imagePath)
    {
        this.imagePath = imagePath;
    }

    public Image getImage()
    {
        Image tempImage = null;
        if (!this.imagePath.isEmpty())
        {
            final File creatureImageFile = CreatureLibrary.getInstance().getLibraryFilePath().resolve(this.getImagePath()).toFile();
            try (final TFileInputStream tFileInputStream = new TFileInputStream(new TFile(creatureImageFile));)
            {
                tempImage = new Image(tFileInputStream);
                tFileInputStream.close();
                LOGGER.debug("Loaded Image for {}", this.getName());
            }
            catch (final IOException e)
            {
                LOGGER.error("ERROR while loading image, using icon instead", e);
                tempImage = DEFAULT_IMAGE;
            }
        }
        else
        {
            tempImage = DEFAULT_IMAGE;
        }
        return tempImage;
    }

    public float getInitiative()
    {
        return this.rolledInitiative + this.stats.getDexterity().getModifier();
    }

    public void setRolledInitiative(final float initiative)
    {
        this.rolledInitiative = initiative;
    }

    public Health getHealth()
    {
        return this.health;
    }

    public int getArmorClass()
    {
        return this.armorClass;
    }

    public void setArmorClass(final int armorClass)
    {
        this.armorClass = armorClass;
    }

    public String getNotes()
    {
        return this.notes;
    }

    public void setNotes(final String notes)
    {
        this.notes = notes;
    }

    public BasicStats getBasicStats()
    {
        return this.stats;
    }

    public int getMoveSpeedGround()
    {
        return this.moveSpeedGround;
    }

    public void setMoveSpeedGround(final int moveSpeedGround)
    {
        this.moveSpeedGround = moveSpeedGround;
    }

    public int getMoveSpeedAir()
    {
        return this.moveSpeedAir;
    }

    public void setMoveSpeedAir(final int moveSpeedAir)
    {
        this.moveSpeedAir = moveSpeedAir;
    }

    public int getMoveSpeedWater()
    {
        return this.moveSpeedWater;
    }

    public void setMoveSpeedWater(final int moveSpeedWater)
    {
        this.moveSpeedWater = moveSpeedWater;
    }

    public int getChallengeRating()
    {
        return this.challengeRating;
    }

    public void setChallengeRating(final int challengeRating)
    {
        this.challengeRating = challengeRating;
    }

    public int getExperience()
    {
        return this.experience;
    }

    public void setExperience(final int experience)
    {
        this.experience = experience;
    }
}
