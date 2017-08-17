package app.bvk.entity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class Creature
{

    private static final Logger LOGGER = LoggerFactory.getLogger(Creature.class);
    private StringProperty name;
    private String imagePath;
    private Image image = null;
    private float initiative = 0;
    private int health = 0;
    private int armorClass = 0;
    private String notes = "";
    private boolean isSelected = false;

    private static final String IMAGEPATH_KEY = "imagePath";
    private static final String INITIATIVE_KEY = "initiative";
    private static final String HEALTH_KEY = "health";
    private static final String NOTES_KEY = "statusNotes";
    private static final String NAME_KEY = "name";
    private static final String ARMOR_CLASS = "ac";
    private static final String STRENGTH_KEY = "str";
    private static final String DEXTERITY_KEY = "dex";
    private static final String CONSTITUTION_KEY = "con";
    private static final String WISDOM_KEY = "wis";
    private static final String INTELLIGENCE_KEY = "int";
    private static final String CHARISMA_KEY = "cha";

    private static final String MOVESPEED_GROUND_KEY = "moveSpeedGround";
    private static final String MOVESPEED_AIR_KEY = "moveSpeedAir";
    private static final String MOVESPEED_WATER_KEY = "moveSpeedWater";

    private static final String SAVING_THROWS_KEY = "savingThrows";
    private static final String SKILLS_KEY = "skills";
    private static final String IMMUNITIES_KEY = "immunties";
    private static final String SENSES_KEY = "senses";
    private static final String LANGUAGES_KEY = "languages";
    private static final String CHALLENGE_RATING_KEY = "challengeRating";
    private static final String EXPERIENCE_KEY = "experience";
    private static final String PROPERTIES_KEY = "properties";
    private static final String ACTIONS_KEY = "actions";

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
    private String immunities = "";
    private String senses = "";
    private List<String> languages = new ArrayList<>();
    private int challengeRating = 0;
    private int experience = 0;

    private Map<String, String> properties = new HashMap<>();
    private Map<String, String> actions = new HashMap<>();

    public Creature(final String name, final String path)
    {
        this.name = new SimpleStringProperty(name);
        this.imagePath = path;
    }

    public Creature(final Creature c)
    {
        this.name = c.getName();
        this.imagePath = c.getImagePath();
        this.image = c.getImage();
        this.initiative = c.getInitiative();
        this.health = c.getHealth();
        this.armorClass = c.getArmorClass();
        this.notes = c.getNotes();
    }

    public Creature(final JsonObject jo)
    {
        this.name = new SimpleStringProperty(jo.get(NAME_KEY).toString().replace("\"", ""));
        this.imagePath = jo.get(IMAGEPATH_KEY).toString().replace("\"", "");
        this.initiative = jo.get(INITIATIVE_KEY).getAsFloat();
        this.health = jo.get(HEALTH_KEY).getAsInt();
        this.setArmorClass(jo.get(ARMOR_CLASS).getAsInt());
        this.notes = jo.get(NOTES_KEY).getAsString().replace("\"", "");

        this.setStrength(jo.get(STRENGTH_KEY).getAsInt());
        this.setDexterity(jo.get(DEXTERITY_KEY).getAsInt());
        this.setConstitution(jo.get(CONSTITUTION_KEY).getAsInt());
        this.setWisdom(jo.get(WISDOM_KEY).getAsInt());
        this.setIntelligence(jo.get(INTELLIGENCE_KEY).getAsInt());
        this.setCharisma(jo.get(CHARISMA_KEY).getAsInt());

        this.setMoveSpeedGround(jo.get(MOVESPEED_GROUND_KEY).getAsInt());
        this.setMoveSpeedAir(jo.get(MOVESPEED_AIR_KEY).getAsInt());
        this.setMoveSpeedWater(jo.get(MOVESPEED_WATER_KEY).getAsInt());
        for (final JsonElement savingThrow : jo.get(SAVING_THROWS_KEY).getAsJsonArray())
        {
            for (final Entry<String, JsonElement> entry : savingThrow.getAsJsonObject().entrySet())
            {
                this.getSavingThrows().put(entry.getKey(), entry.getValue().getAsInt());
            }
        }
        for (final JsonElement skill : jo.get(SKILLS_KEY).getAsJsonArray())
        {
            for (final Entry<String, JsonElement> entry : skill.getAsJsonObject().entrySet())
            {
                this.getSkills().put(entry.getKey(), entry.getValue().getAsInt());
            }
        }
        this.setImmunities(jo.get(IMMUNITIES_KEY).getAsString());
        this.setSenses(jo.get(SENSES_KEY).getAsString());

        this.setLanguages(this.languages);
        this.setChallengeRating(jo.get(CHALLENGE_RATING_KEY).getAsInt());
        this.setExperience(jo.get(EXPERIENCE_KEY).getAsInt());

        for (final JsonElement lang : jo.get(LANGUAGES_KEY).getAsJsonArray())
        {
            this.getLanguages().add(lang.getAsString());
        }

        for (final JsonElement property : jo.get(PROPERTIES_KEY).getAsJsonArray())
        {
            for (final Entry<String, JsonElement> entry : property.getAsJsonObject().entrySet())
            {
                this.getProperties().put(entry.getKey(), entry.getValue().getAsString());
            }
        }
        for (final JsonElement action : jo.get(ACTIONS_KEY).getAsJsonArray())
        {
            for (final Entry<String, JsonElement> entry : action.getAsJsonObject().entrySet())
            {
                this.getActions().put(entry.getKey(), entry.getValue().getAsString());
            }
        }
    }

    public void writeJsonToFile(final File file)
    {
        try (final FileWriter fw = new FileWriter(file); final JsonWriter jsonWriter = new JsonWriter(fw);)
        {
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name(NAME_KEY).value(this.getName().get());
            jsonWriter.name(IMAGEPATH_KEY).value(this.getImagePath());
            jsonWriter.name(INITIATIVE_KEY).value(this.getInitiative());
            jsonWriter.name(HEALTH_KEY).value(this.getHealth());
            jsonWriter.name(NOTES_KEY).value(this.getNotes());
            jsonWriter.name(STRENGTH_KEY).value(this.getStrength());
            jsonWriter.name(DEXTERITY_KEY).value(this.getDexterity());
            jsonWriter.name(CONSTITUTION_KEY).value(this.getConstitution());
            jsonWriter.name(WISDOM_KEY).value(this.getWisdom());
            jsonWriter.name(INTELLIGENCE_KEY).value(this.getIntelligence());
            jsonWriter.name(CHARISMA_KEY).value(this.getCharisma());
            jsonWriter.name(ARMOR_CLASS).value(this.getArmorClass());

            jsonWriter.name(MOVESPEED_GROUND_KEY).value(this.getMoveSpeedGround());
            jsonWriter.name(MOVESPEED_AIR_KEY).value(this.getMoveSpeedAir());
            jsonWriter.name(MOVESPEED_WATER_KEY).value(this.getMoveSpeedWater());

            jsonWriter.name(SAVING_THROWS_KEY).beginArray();
            for (final Map.Entry<String, Integer> savingThrow : this.getSavingThrows().entrySet())
            {
                jsonWriter.beginObject();
                jsonWriter.name(savingThrow.getKey()).value(savingThrow.getValue());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
            jsonWriter.name(SKILLS_KEY).beginArray();
            for (final Map.Entry<String, Integer> skill : this.getSkills().entrySet())
            {
                jsonWriter.beginObject();
                jsonWriter.name(skill.getKey()).value(skill.getValue());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
            jsonWriter.name(IMMUNITIES_KEY).value(this.getImmunities());
            jsonWriter.name(SENSES_KEY).value(this.getSenses());
            jsonWriter.name(LANGUAGES_KEY).beginArray();
            for (final String lang : this.getLanguages())
            {
                jsonWriter.value(lang);
            }
            jsonWriter.endArray();
            jsonWriter.name(CHALLENGE_RATING_KEY).value(this.getChallengeRating());
            jsonWriter.name(EXPERIENCE_KEY).value(this.getExperience());
            jsonWriter.name(PROPERTIES_KEY).beginArray();
            for (final Map.Entry<String, String> prop : this.getProperties().entrySet())
            {
                jsonWriter.beginObject();
                jsonWriter.name(prop.getKey()).value(prop.getValue());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
            jsonWriter.name(ACTIONS_KEY).beginArray();
            for (final Map.Entry<String, String> action : this.getActions().entrySet())
            {
                jsonWriter.beginObject();
                jsonWriter.name(action.getKey()).value(action.getValue());
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

    public StringProperty getName()
    {
        return this.name;
    }

    public void setName(final StringProperty name)
    {
        this.name = name;
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

    public boolean isSelected()
    {
        return this.isSelected;
    }

    public void setSelected(final boolean isSelected)
    {
        this.isSelected = isSelected;
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

    public String getImmunities()
    {
        return this.immunities;
    }

    public void setImmunities(final String immunities)
    {
        this.immunities = immunities;
    }

    public String getSenses()
    {
        return this.senses;
    }

    public void setSenses(final String senses)
    {
        this.senses = senses;
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

    public Map<String, String> getProperties()
    {
        return this.properties;
    }

    public void setProperties(final Map<String, String> properties)
    {
        this.properties = properties;
    }

    public Map<String, String> getActions()
    {
        return this.actions;
    }

    public void setActions(final Map<String, String> actions)
    {
        this.actions = actions;
    }

    public List<String> getLanguages()
    {
        return this.languages;
    }

    public void setLanguages(final List<String> languages)
    {
        this.languages = languages;
    }
}
