package encounter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import entity.Creature;
import gui.MainGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Encounter {
    private ArrayList<Creature> creatureList;
    private int currentIndex = 0;

    public Encounter() {
	creatureList = new ArrayList<Creature>();
    }

    public void reset() {
	creatureList.clear();
    }

    public void addCreature(Creature creature) {
	creatureList.add(creature);
    }

    public void addCreature(String name) {
	for (Creature c : MainGUI.creatureList) {
	    if (c.getName().equals(name)) {
		creatureList.add(new Creature(c));
		break;
	    }
	}
    }

    public void copy(int index) {
	getCreatureList().add(new Creature(getCreatureList().get(index)));
    }

    /**
     * @return the creatureList
     */
    public ArrayList<Creature> getCreatureList() {
	return creatureList;
    }

    public ObservableList<EncounterEntry> getObsList() {
	ArrayList<EncounterEntry> eeList = new ArrayList<EncounterEntry>();
	for (Creature c : getCreatureList()) {
	    eeList.add(new EncounterEntry(c));
	}
	return FXCollections.observableArrayList(eeList);
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
    }

    public void setNextIndex() {
	if (currentIndex + 1 > creatureList.size() - 1) {
	    currentIndex = 0;
	} else {
	    currentIndex++;
	}
    }

    public void setLastIndex() {
	if (currentIndex - 1 < 0) {
	    currentIndex = creatureList.size() - 1;
	} else {
	    currentIndex--;
	}
    }

    public void saveToFile(File file) {
	JsonWriter jsonWriter;
	try {
	    jsonWriter = new JsonWriter(new FileWriter(file));
	    jsonWriter.setIndent("  ");
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
	    jsonWriter.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
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
	    for (JsonElement je : jArray) {
		JsonObject jo = je.getAsJsonObject();
		Creature c = new Creature(jo);
		getCreatureList().add(c);
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
