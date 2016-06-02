package app.bvk.entity;

import com.google.gson.JsonObject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class Creature {

    private StringProperty name;
    private String imagePath;
    private Image image = null;
    private float initiative = 0;
    private int health = 0;
    private int armorClass = 0;
    private String statusNotes = "";
    private String miscNotes = "";
    private boolean isSelected = false;

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
        this.miscNotes = c.getMiscNotes();
    }

    public Creature(JsonObject jo) {
        this.name.setValue(jo.get("name").toString().replace("\"", ""));
        this.imagePath = jo.get("imagePath").toString().replace("\"", "");
        this.initiative = jo.get("initiative").getAsFloat();
        this.health = jo.get("health").getAsInt();
        this.armorClass = jo.get("armorClass").getAsInt();
        this.statusNotes = jo.get("statusNotes").getAsString().replace("\"", "");
        this.miscNotes = jo.get("miscNotes").getAsString().replace("\"", "");
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

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public float getInitiative() {
        return initiative;
    }

    public void setInitiative(float initiative) {
        this.initiative = initiative;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(int armorClass) {
        this.armorClass = armorClass;
    }

    public String getStatusNotes() {
        return statusNotes;
    }

    public void setStatusNotes(String statusNotes) {
        this.statusNotes = statusNotes;
    }

    public String getMiscNotes() {
        return miscNotes;
    }

    public void setMiscNotes(String miscNotes) {
        this.miscNotes = miscNotes;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
