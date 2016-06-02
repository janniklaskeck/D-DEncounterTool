package app.bvk.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import app.bvk.encounter.Encounter;
import app.bvk.entity.Creature;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Settings {

    private final String imageFolder = System.getProperty("user.dir") + "\\images\\";
    private Image imageIcon;
    private ZipFile imageZipFile;

    private final List<Creature> creatureList = new ArrayList<>();
    private Encounter encounter = new Encounter("Unnamed Encounter");
    private Stage mainStage;

    private long autoSaveInterval = 60000L;
    private boolean doAutoSave = true;
    private boolean autosave = false;

    private static Settings instance;

    private Settings() {
    }

    public static synchronized Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public Image getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(final Image imageIcon) {
        this.imageIcon = imageIcon;
    }

    public ZipFile getImageZipFile() {
        return imageZipFile;
    }

    public void setImageZipFile(final ZipFile imageZipFile) {
        this.imageZipFile = imageZipFile;
    }

    public Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(final Encounter encounter) {
        this.encounter = encounter;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(final Stage mainStage) {
        this.mainStage = mainStage;
    }

    public boolean isDoAutoSave() {
        return doAutoSave;
    }

    public void setDoAutoSave(final boolean doAutoSave) {
        this.doAutoSave = doAutoSave;
    }

    public boolean isAutosave() {
        return autosave;
    }

    public void setAutosave(final boolean autosave) {
        this.autosave = autosave;
    }

    public String getImageFolder() {
        return imageFolder;
    }

    public List<Creature> getCreatureList() {
        return creatureList;
    }

    public long getAutoSaveInterval() {
        return autoSaveInterval;
    }

}
