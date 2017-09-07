package app.bvk.entity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import app.bvk.library.CreatureLibrary;
import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileInputStream;
import de.schlichtherle.truezip.file.TFileWriter;
import javafx.scene.image.Image;

public class Creature
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Creature.class);
    private String name = "unnamed";
    private String imagePath;
    private Image image = null;
    private float initiative = 0;
    private int health = 0;
    private int armorClass = 0;
    private String notes = "";

    private int strength = 0;
    private int dexterity = 0;
    private int constitution = 0;
    private int wisdom = 0;
    private int intelligence = 0;
    private int charisma = 0;
    private int moveSpeedGround = 0;
    private int moveSpeedAir = 0;
    private int moveSpeedWater = 0;

    private Map<String, Integer> savingThrows = new HashMap<>();
    private Map<String, Integer> skills = new HashMap<>();
    private int challengeRating = 0;
    private int experience = 0;

    private static final Image DEFAULT_IMAGE = new Image(Creature.class.getClassLoader().getResourceAsStream("icon.png"));

    public Creature(final String name, final String path)
    {
        this.name = name;
        this.imagePath = path;
    }

    public Creature(final Creature creature)
    {
        this.name = creature.name;
        this.imagePath = creature.getImagePath();
        this.image = creature.getImage();
        this.initiative = creature.getInitiative();
        this.health = creature.getHealth();
        this.armorClass = creature.getArmorClass();
        this.notes = creature.getNotes();
    }

    public Creature(final JsonObject jo)
    {
        this.loadFromJson(jo);
    }

    public void loadFromJson(final JsonObject json)
    {
        this.name = json.get(CreatureStrings.NAME_KEY).toString().replace("\"", "");
        this.imagePath = json.get(CreatureStrings.IMAGEPATH_KEY).toString().replace("\"", "");
        this.initiative = json.get(CreatureStrings.INITIATIVE_KEY).getAsFloat();
        this.health = json.get(CreatureStrings.HEALTH_KEY).getAsInt();
        this.armorClass = json.get(CreatureStrings.ARMOR_CLASS).getAsInt();
        this.notes = json.get(CreatureStrings.NOTES_KEY).getAsString().replace("\"", "");

        this.setStrength(json.get(CreatureStrings.STRENGTH_KEY).getAsInt());
        this.setDexterity(json.get(CreatureStrings.DEXTERITY_KEY).getAsInt());
        this.setConstitution(json.get(CreatureStrings.CONSTITUTION_KEY).getAsInt());
        this.setWisdom(json.get(CreatureStrings.WISDOM_KEY).getAsInt());
        this.setIntelligence(json.get(CreatureStrings.INTELLIGENCE_KEY).getAsInt());
        this.setCharisma(json.get(CreatureStrings.CHARISMA_KEY).getAsInt());

        this.setMoveSpeedGround(json.get(CreatureStrings.MOVESPEED_GROUND_KEY).getAsInt());
        this.setMoveSpeedAir(json.get(CreatureStrings.MOVESPEED_AIR_KEY).getAsInt());
        this.setMoveSpeedWater(json.get(CreatureStrings.MOVESPEED_WATER_KEY).getAsInt());
        for (final JsonElement savingThrow : json.get(CreatureStrings.SAVING_THROWS_KEY).getAsJsonArray())
        {
            for (final Entry<String, JsonElement> entry : savingThrow.getAsJsonObject().entrySet())
            {
                this.getSavingThrows().put(entry.getKey(), entry.getValue().getAsInt());
            }
        }
        for (final JsonElement skill : json.get(CreatureStrings.SKILLS_KEY).getAsJsonArray())
        {
            for (final Entry<String, JsonElement> entry : skill.getAsJsonObject().entrySet())
            {
                this.getSkills().put(entry.getKey(), entry.getValue().getAsInt());
            }
        }
        this.setChallengeRating(json.get(CreatureStrings.CHALLENGE_RATING_KEY).getAsInt());
        this.setExperience(json.get(CreatureStrings.EXPERIENCE_KEY).getAsInt());
    }

    public void writeJsonToFile(final TFile file)
    {
        try (final TFileWriter fw = new TFileWriter(file); final JsonWriter jsonWriter = new JsonWriter(fw);)
        {
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name(CreatureStrings.NAME_KEY).value(this.name);
            jsonWriter.name(CreatureStrings.IMAGEPATH_KEY).value(this.getImagePath());
            jsonWriter.name(CreatureStrings.INITIATIVE_KEY).value(this.getInitiative());
            jsonWriter.name(CreatureStrings.HEALTH_KEY).value(this.getHealth());
            jsonWriter.name(CreatureStrings.NOTES_KEY).value(this.getNotes());
            jsonWriter.name(CreatureStrings.STRENGTH_KEY).value(this.getStrength());
            jsonWriter.name(CreatureStrings.DEXTERITY_KEY).value(this.getDexterity());
            jsonWriter.name(CreatureStrings.CONSTITUTION_KEY).value(this.getConstitution());
            jsonWriter.name(CreatureStrings.WISDOM_KEY).value(this.getWisdom());
            jsonWriter.name(CreatureStrings.INTELLIGENCE_KEY).value(this.getIntelligence());
            jsonWriter.name(CreatureStrings.CHARISMA_KEY).value(this.getCharisma());
            jsonWriter.name(CreatureStrings.ARMOR_CLASS).value(this.getArmorClass());

            jsonWriter.name(CreatureStrings.MOVESPEED_GROUND_KEY).value(this.getMoveSpeedGround());
            jsonWriter.name(CreatureStrings.MOVESPEED_AIR_KEY).value(this.getMoveSpeedAir());
            jsonWriter.name(CreatureStrings.MOVESPEED_WATER_KEY).value(this.getMoveSpeedWater());

            jsonWriter.name(CreatureStrings.CHALLENGE_RATING_KEY).value(this.getChallengeRating());
            jsonWriter.name(CreatureStrings.EXPERIENCE_KEY).value(this.getExperience());

            jsonWriter.name(CreatureStrings.SAVING_THROWS_KEY).beginArray();
            for (final Map.Entry<String, Integer> savingThrow : this.getSavingThrows().entrySet())
            {
                jsonWriter.beginObject();
                jsonWriter.name(savingThrow.getKey()).value(savingThrow.getValue());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
            jsonWriter.name(CreatureStrings.SKILLS_KEY).beginArray();
            for (final Map.Entry<String, Integer> skill : this.getSkills().entrySet())
            {
                jsonWriter.beginObject();
                jsonWriter.name(skill.getKey()).value(skill.getValue());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
            jsonWriter.endObject();
            jsonWriter.close();
            fw.close();
        }
        catch (final IOException e)
        {
            LOGGER.error("", e);
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
        if (this.image == null && !this.imagePath.isEmpty())
        {
            final File creatureImageFile = CreatureLibrary.getInstance().getLibraryFilePath().resolve(this.getImagePath()).toFile();
            try (final TFileInputStream is = new TFileInputStream(new TFile(creatureImageFile));)
            {
                this.image = new Image(is);
                is.close();
                LOGGER.debug("Loaded Image for {}", this.getName());
            }
            catch (final IOException e)
            {
                LOGGER.error("ERROR while loading image, using icon instead", e);
                this.image = DEFAULT_IMAGE;
            }
        }
        else if (this.imagePath.isEmpty())
        {
            this.image = DEFAULT_IMAGE;
        }
        return this.image;
    }

    public void setImage(final Image image)
    {
        this.image = image;
    }

    public float getInitiative()
    {
        return this.initiative;
    }

    public void setInitiative(final float initiative)
    {
        this.initiative = initiative;
    }

    public int getHealth()
    {
        return this.health;
    }

    public void setHealth(final int health)
    {
        this.health = health;
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

    public int getStrength()
    {
        return this.strength;
    }

    public void setStrength(final int strength)
    {
        this.strength = strength;
    }

    public int getDexterity()
    {
        return this.dexterity;
    }

    public void setDexterity(final int dexterity)
    {
        this.dexterity = dexterity;
    }

    public int getConstitution()
    {
        return this.constitution;
    }

    public void setConstitution(final int constitution)
    {
        this.constitution = constitution;
    }

    public int getWisdom()
    {
        return this.wisdom;
    }

    public void setWisdom(final int wisdom)
    {
        this.wisdom = wisdom;
    }

    public int getIntelligence()
    {
        return this.intelligence;
    }

    public void setIntelligence(final int intelligence)
    {
        this.intelligence = intelligence;
    }

    public int getCharisma()
    {
        return this.charisma;
    }

    public void setCharisma(final int charisma)
    {
        this.charisma = charisma;
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

    public Map<String, Integer> getSavingThrows()
    {
        return this.savingThrows;
    }

    public void setSavingThrows(final Map<String, Integer> savingThrows)
    {
        this.savingThrows = savingThrows;
    }

    public Map<String, Integer> getSkills()
    {
        return this.skills;
    }

    public void setSkills(final Map<String, Integer> skills)
    {
        this.skills = skills;
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
