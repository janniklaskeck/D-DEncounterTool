package entity;

import java.awt.image.BufferedImage;

import com.google.gson.JsonObject;

public class Creature {

    private String Name;
    private String ImagePath;
    private BufferedImage image = null;
    private float initiative = 0;
    private int health = 0;
    private int armorClass = 0;
    private String statusNotes = "";
    private String miscNotes = "";
    private boolean isSelected = false;

    public Creature(String Name, String Path) {
	this.Name = Name;
	this.ImagePath = Path;
    }

    public Creature(Creature c) {
	this.Name = c.getName();
	this.ImagePath = c.getImagePath();
	this.image = c.getImage();
	this.initiative = c.getInitiative();
	this.health = c.getHealth();
	this.armorClass = c.getArmorClass();
	this.statusNotes = c.getStatusNotes();
	this.miscNotes = c.getMiscNotes();
    }

    public Creature(JsonObject jo) {
	this.Name = jo.get("name").toString().replace("\"", "");
	this.ImagePath = jo.get("imagePath").toString();
	this.initiative = jo.get("initiative").getAsFloat();
	this.health = jo.get("health").getAsInt();
	this.armorClass = jo.get("armorClass").getAsInt();
	this.statusNotes = jo.get("statusNotes").getAsString();
	this.miscNotes = jo.get("miscNotes").getAsString();
    }

    /**
     * @return the name
     */
    public String getName() {
	return Name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	Name = name;
    }

    /**
     * @return the imagePath
     */
    public String getImagePath() {
	return ImagePath;
    }

    /**
     * @param imagePath
     *            the imagePath to set
     */
    public void setImagePath(String imagePath) {
	ImagePath = imagePath;
    }

    /**
     * @return the image
     */
    public BufferedImage getImage() {
	return image;
    }

    /**
     * @param image
     *            the image to set
     */
    public void setImage(BufferedImage image) {
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
