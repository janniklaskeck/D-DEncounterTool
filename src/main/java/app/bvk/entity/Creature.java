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

public class Creature {

    private static final Logger LOGGER = LoggerFactory.getLogger(Creature.class);
    private StringProperty name;
    private String imagePath;
    private Image image = null;
    private float initiative = 0;
    private int health = 0;
    private int armorClass = 0;
    private String statusNotes = "";
    private boolean isSelected = false;

    private static final String NAME = "name";
    private static final String ARMOR_CLASS = "ac";
    private static final String STRENGTH_KEY = "str";
    private static final String DEXTERITY = "dex";
    private static final String CONSTITUTION = "con";
    private static final String WISDOM = "wis";
    private static final String INTELLIGENCE = "int";
    private static final String CHARISMA = "cha";

    private static final String MOVESPEED_GROUND = "moveSpeedGround";
    private static final String MOVESPEED_AIR = "moveSpeedAir";
    private static final String MOVESPEED_WATER = "moveSpeedWater";

    private static final String SAVING_THROWS = "savingThrows";
    private static final String SKILLS = "skills";
    private static final String IMMUNITIES = "immunties";
    private static final String SENSES = "senses";
    private static final String LANGUAGES = "languages";
    private static final String CHALLENGE_RATING = "challengeRating";
    private static final String EXPERIENCE = "experience";
    private static final String PROPERTIES = "properties";
    private static final String ACTIONS = "actions";

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

    public Creature(final String name, final String path) {
        this.name = new SimpleStringProperty(name);
        this.imagePath = path;
    }

    public Creature(final Creature c) {
        this.name = c.getName();
        this.imagePath = c.getImagePath();
        this.image = c.getImage();
        this.initiative = c.getInitiative();
        this.health = c.getHealth();
        this.armorClass = c.getArmorClass();
        this.statusNotes = c.getStatusNotes();
    }

    public Creature(final JsonObject jo) {
        this.name = new SimpleStringProperty(jo.get(NAME).toString().replace("\"", ""));
        this.imagePath = jo.get("imagePath").toString().replace("\"", "");
        this.initiative = jo.get("initiative").getAsFloat();
        this.health = jo.get("health").getAsInt();
        setArmorClass(jo.get(ARMOR_CLASS).getAsInt());
        this.statusNotes = jo.get("statusNotes").getAsString().replace("\"", "");

        setStrength(jo.get(STRENGTH_KEY).getAsInt());
        setDexterity(jo.get(DEXTERITY).getAsInt());
        setConstitution(jo.get(CONSTITUTION).getAsInt());
        setWisdom(jo.get(WISDOM).getAsInt());
        setIntelligence(jo.get(INTELLIGENCE).getAsInt());
        setCharisma(jo.get(CHARISMA).getAsInt());

        setMoveSpeedGround(jo.get(MOVESPEED_GROUND).getAsInt());
        setMoveSpeedAir(jo.get(MOVESPEED_AIR).getAsInt());
        setMoveSpeedWater(jo.get(MOVESPEED_WATER).getAsInt());
        for (final JsonElement savingThrow : jo.get(SAVING_THROWS).getAsJsonArray()) {
            for (final Entry<String, JsonElement> entry : savingThrow.getAsJsonObject().entrySet()) {
                getSavingThrows().put(entry.getKey(), entry.getValue().getAsInt());
            }
        }
        for (final JsonElement skill : jo.get(SKILLS).getAsJsonArray()) {
            for (final Entry<String, JsonElement> entry : skill.getAsJsonObject().entrySet()) {
                getSkills().put(entry.getKey(), entry.getValue().getAsInt());
            }
        }
        setImmunities(jo.get(IMMUNITIES).getAsString());
        setSenses(jo.get(SENSES).getAsString());

        setLanguages(languages);
        setChallengeRating(jo.get(CHALLENGE_RATING).getAsInt());
        setExperience(jo.get(EXPERIENCE).getAsInt());

        for (final JsonElement lang : jo.get(LANGUAGES).getAsJsonArray()) {
            getLanguages().add(lang.getAsString());
        }

        for (final JsonElement property : jo.get(PROPERTIES).getAsJsonArray()) {
            for (final Entry<String, JsonElement> entry : property.getAsJsonObject().entrySet()) {
                getProperties().put(entry.getKey(), entry.getValue().getAsString());
            }
        }
        for (final JsonElement action : jo.get(ACTIONS).getAsJsonArray()) {
            for (final Entry<String, JsonElement> entry : action.getAsJsonObject().entrySet()) {
                getActions().put(entry.getKey(), entry.getValue().getAsString());
            }
        }
    }

    public void writeJsonToFile(final File file) {
        try {
            final JsonWriter jsonWriter;
            final FileWriter fw = new FileWriter(file);
            jsonWriter = new JsonWriter(fw);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name(NAME).value(getName().get());
            jsonWriter.name(STRENGTH_KEY).value(getStrength());
            jsonWriter.name(DEXTERITY).value(getDexterity());
            jsonWriter.name(CONSTITUTION).value(getConstitution());
            jsonWriter.name(WISDOM).value(getWisdom());
            jsonWriter.name(INTELLIGENCE).value(getIntelligence());
            jsonWriter.name(CHARISMA).value(getCharisma());
            jsonWriter.name(ARMOR_CLASS).value(getArmorClass());

            jsonWriter.name(MOVESPEED_GROUND).value(getMoveSpeedGround());
            jsonWriter.name(MOVESPEED_AIR).value(getMoveSpeedAir());
            jsonWriter.name(MOVESPEED_WATER).value(getMoveSpeedWater());

            jsonWriter.name(SAVING_THROWS).beginArray();
            for (Map.Entry<String, Integer> savingThrow : getSavingThrows().entrySet()) {
                jsonWriter.beginObject();
                jsonWriter.name(savingThrow.getKey()).value(savingThrow.getValue());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
            jsonWriter.name(SKILLS).beginArray();
            for (Map.Entry<String, Integer> skill : getSkills().entrySet()) {
                jsonWriter.beginObject();
                jsonWriter.name(skill.getKey()).value(skill.getValue());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
            jsonWriter.name(IMMUNITIES).value(getImmunities());
            jsonWriter.name(SENSES).value(getSenses());
            jsonWriter.name(LANGUAGES).beginArray();
            for (final String lang : getLanguages()) {
                jsonWriter.value(lang);
            }
            jsonWriter.endArray();
            jsonWriter.name(CHALLENGE_RATING).value(getChallengeRating());
            jsonWriter.name(EXPERIENCE).value(getExperience());
            jsonWriter.name(PROPERTIES).beginArray();
            for (final Map.Entry<String, String> prop : getProperties().entrySet()) {
                jsonWriter.beginObject();
                jsonWriter.name(prop.getKey()).value(prop.getValue());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
            jsonWriter.name(ACTIONS).beginArray();
            for (final Map.Entry<String, String> action : getActions().entrySet()) {
                jsonWriter.beginObject();
                jsonWriter.name(action.getKey()).value(action.getValue());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
            jsonWriter.endObject();
            jsonWriter.close();
            fw.close();
        } catch (IOException e) {
            LOGGER.error("", e);
        }
    }

    public StringProperty getName() {
        return name;
    }

    public void setName(final StringProperty name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(final String imagePath) {
        this.imagePath = imagePath;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(final Image image) {
        this.image = image;
    }

    public float getInitiative() {
        return initiative;
    }

    public void setInitiative(final float initiative) {
        this.initiative = initiative;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(final int health) {
        this.health = health;
    }

    public int getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(final int armorClass) {
        this.armorClass = armorClass;
    }

    public String getStatusNotes() {
        return statusNotes;
    }

    public void setStatusNotes(final String statusNotes) {
        this.statusNotes = statusNotes;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(final boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getConstitution() {
        return constitution;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    public int getWisdom() {
        return wisdom;
    }

    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getCharisma() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    public int getMoveSpeedGround() {
        return moveSpeedGround;
    }

    public void setMoveSpeedGround(int moveSpeedGround) {
        this.moveSpeedGround = moveSpeedGround;
    }

    public int getMoveSpeedAir() {
        return moveSpeedAir;
    }

    public void setMoveSpeedAir(int moveSpeedAir) {
        this.moveSpeedAir = moveSpeedAir;
    }

    public int getMoveSpeedWater() {
        return moveSpeedWater;
    }

    public void setMoveSpeedWater(int moveSpeedWater) {
        this.moveSpeedWater = moveSpeedWater;
    }

    public Map<String, Integer> getSavingThrows() {
        return savingThrows;
    }

    public void setSavingThrows(final Map<String, Integer> savingThrows) {
        this.savingThrows = savingThrows;
    }

    public Map<String, Integer> getSkills() {
        return skills;
    }

    public void setSkills(final Map<String, Integer> skills) {
        this.skills = skills;
    }

    public String getImmunities() {
        return immunities;
    }

    public void setImmunities(final String immunities) {
        this.immunities = immunities;
    }

    public String getSenses() {
        return senses;
    }

    public void setSenses(final String senses) {
        this.senses = senses;
    }

    public int getChallengeRating() {
        return challengeRating;
    }

    public void setChallengeRating(final int challengeRating) {
        this.challengeRating = challengeRating;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(final int experience) {
        this.experience = experience;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }

    public Map<String, String> getActions() {
        return actions;
    }

    public void setActions(final Map<String, String> actions) {
        this.actions = actions;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(final List<String> languages) {
        this.languages = languages;
    }
}
