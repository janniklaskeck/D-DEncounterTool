package encounter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import entity.Creature;
import gui.MainGUI;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Encounter {
    private ArrayList<Creature> creatureList;
    private int currentIndex = 0;
    private File file;
    private String encounterName;
    public StringProperty encounterNameProperty;

    public Encounter(String name) {
        encounterName = name;
        encounterNameProperty = new SimpleStringProperty(encounterName);
        creatureList = new ArrayList<>();
    }

    public void reset() {
        creatureList.clear();
    }

    public void addCreature(Creature creature) {
        creatureList.add(creature);
        for (Creature c : getCreatureList()) {
            c.setSelected(false);
        }
        if (getCreatureList().size() - 1 >= getCurrentIndex()) {
            getCreatureList().get(getCurrentIndex()).setSelected(true);
        } else {
            getCreatureList().get(0).setSelected(true);
            setCurrentIndex(0);
        }
    }

    public void addCreature(String name) {
        for (Creature c : MainGUI.creatureList) {
            if (c.getName().equals(name)) {
                creatureList.add(new Creature(c));
                break;
            }
        }
        for (Creature c : getCreatureList()) {
            c.setSelected(false);
        }
        if (getCreatureList().size() - 1 >= getCurrentIndex()) {
            getCreatureList().get(getCurrentIndex()).setSelected(true);
        } else {
            getCreatureList().get(0).setSelected(true);
            setCurrentIndex(0);
        }
    }

    public void copy(int index) {
        getCreatureList().add(new Creature(getCreatureList().get(index)));
    }

    /**
     * @return the creatureList
     */
    public List<Creature> getCreatureList() {
        return creatureList;
    }

    public ObservableList<EncounterEntry> getObsList() {
        ArrayList<EncounterEntry> eeList = new ArrayList<>();
        for (Creature c : getCreatureList()) {
            eeList.add(new EncounterEntry(c));
        }
        return FXCollections.observableArrayList(eeList);
    }

    private void updateIndex() {
        for (int i = 0; i < getCreatureList().size(); i++) {
            if (getCreatureList().get(i).isSelected()) {
                setCurrentIndex(i);
            }
        }
    }

    public void sort() {
        getCreatureList().sort((o1, o2) -> {
            if (o1.getInitiative() < o2.getInitiative()) {
                return 1;
            } else if (o1.getInitiative() > o2.getInitiative()) {
                return -1;
            }
            return 0;
        });
        updateIndex();
    }

    /**
     * @return the currentIndex
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * @param currentIndex
     *            the currentIndex to set
     */
    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
        for (Creature c : getCreatureList()) {
            c.setSelected(false);
        }
        getCreatureList().get(getCurrentIndex()).setSelected(true);
    }

    public void setNextIndex() {
        if (currentIndex + 1 > creatureList.size() - 1) {
            currentIndex = 0;
        } else {
            currentIndex++;
        }
        setCurrentIndex(currentIndex);
    }

    public void setLastIndex() {
        if (currentIndex - 1 < 0) {
            currentIndex = creatureList.size() - 1;
        } else {
            currentIndex--;
        }
        setCurrentIndex(currentIndex);
    }

    public void saveToFile(File file) {
        JsonWriter jsonWriter;
        try {
            jsonWriter = new JsonWriter(new FileWriter(file));
            jsonWriter.setIndent("  ");
            jsonWriter.beginArray();
            jsonWriter.beginObject();
            jsonWriter.name("encounterName")
                    .value("".equals(getEncounterName()) ? "Unnamed Encounter" : getEncounterName());
            jsonWriter.endObject();
            jsonWriter.beginArray();
            for (Creature c : getCreatureList()) {
                jsonWriter.beginObject();
                jsonWriter.name("name").value(c.getName());
                jsonWriter.name("imagePath").value(c.getImagePath());
                jsonWriter.name("initiative").value(c.getInitiative());
                jsonWriter.name("health").value(c.getHealth());
                jsonWriter.name("armorClass").value(c.getArmorClass());
                jsonWriter.name("statusNotes").value(c.getStatusNotes());
                jsonWriter.name("miscNotes").value(c.getMiscNotes());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
            jsonWriter.endArray();
            jsonWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void autoSave() {
        if (file != null) {
            file.delete();
        }
        String date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        file = new File(System.getProperty("user.dir") + "\\saves\\" + encounterName + "-" + date.replace(":", ".")
                + ".ddesav");
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveToFile(file);
    }

    public void readFromFile(File file) {
        FileInputStream fis;
        reset();
        try {
            fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
            Gson g = new Gson();
            JsonArray jArray = g.fromJson(sb.toString(), JsonArray.class);
            setEncounterName(jArray.get(0).getAsJsonObject().get("encounterName").getAsString());

            JsonArray creatureArray = jArray.get(1).getAsJsonArray();
            for (JsonElement je : creatureArray) {
                JsonObject jo = je.getAsJsonObject();
                Creature c = new Creature(jo);
                getCreatureList().add(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setCurrentIndex(0);
    }

    /**
     * @return the encounterName
     */
    public String getEncounterName() {
        return encounterName;
    }

    /**
     * @param encounterName
     *            the encounterName to set
     */
    public void setEncounterName(String encounterName) {
        this.encounterName = encounterName;
        encounterNameProperty.set(encounterName);
    }

    public void remove(final int selectedIndex) {
        getCreatureList().remove(selectedIndex);
        if (selectedIndex == currentIndex && !getCreatureList().isEmpty()) {
            for (Creature c : getCreatureList()) {
                c.setSelected(false);
            }
            getCreatureList().get(getCurrentIndex()).setSelected(true);
        }
    }
}
