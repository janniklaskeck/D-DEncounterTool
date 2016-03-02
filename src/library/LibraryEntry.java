package library;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class LibraryEntry extends AnchorPane {

    public LibraryEntry() {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("LibraryEntry.fxml"));
	loader.setRoot(this);
	loader.setController(this);

	try {
	    loader.load();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}
