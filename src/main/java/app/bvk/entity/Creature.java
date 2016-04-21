package app.bvk.entity;

import com.google.gson.JsonObject;

import javafx.scene.image.Image;

public class Creature {

    private String name;
    private String imagePath;
    private Image image = null;
    private float initiative = 0;
    private int health = 0;
    private int armorClass = 0;
    private String statusNotes = "";
    private String miscNotes = "";
    private boolean isSelected = false;

    public Creature(String name, String path) {
        this.name = name;
        this.imagePath = path;
    }

    public Creature(Creature c) {
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
        this.name = jo.get("name").toString().replace("\"", "");
        this.imagePath = jo.get("imagePath").toString().replace("\"", "");
        this.initiative = jo.get("initiative").getAsFloat();
        this.health = jo.get("health").getAsInt();
        this.armorClass = jo.get("armorClass").getAsInt();
        this.statusNotes = jo.get("statusNotes").getAsString().replace("\"", "");
        this.miscNotes = jo.get("miscNotes").getAsString().replace("\"", "");
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath
     *            the imagePath to set
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * @param image
     *            the image to set
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * @return the initiative
     */
    public float getInitiative() {
        return initiative;
    }

    /**
     * @param initiative
     *            the initiative to set
     */
    public void setInitiative(float initiative) {
        this.initiative = initiative;
    }

    /**
     * @return the health
     */
    public int getHealth() {
        return health;
    }

    /**
     * @param health
     *            the health to set
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * @return the armorClass
     */
    public int getArmorClass() {
        return armorClass;
    }

    /**
     * @param armorClass
     *            the armorClass to set
     */
    public void setArmorClass(int armorClass) {
        this.armorClass = armorClass;
    }

    /**
     * @return the statusNotes
     */
    public String getStatusNotes() {
        return statusNotes;
    }

    /**
     * @param statusNotes
     *            the statusNotes to set
     */
    public void setStatusNotes(String statusNotes) {
        this.statusNotes = statusNotes;
    }

    /**
     * @return the miscNotes
     */
    public String getMiscNotes() {
        return miscNotes;
    }

    /**
     * @param miscNotes
     *            the miscNotes to set
     */
    public void setMiscNotes(String miscNotes) {
        this.miscNotes = miscNotes;
    }

    /**
     * @return the isSelected
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * @param isSelected
     *            the isSelected to set
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
