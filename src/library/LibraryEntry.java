package library;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;

import entity.Creature;
import gui.MainGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LibraryEntry extends AnchorPane {

	private Creature creature;
	FXMLLoader loader;

	@FXML
	private Text nameText;

	public LibraryEntry(Creature creature) {
		this.creature = creature;
		loader = new FXMLLoader(getClass().getResource("LibraryEntry.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void initialize() {
		nameText.setText(creature.getName());
	}

	@FXML
	protected void openImage(ActionEvent event) {
		final Stage myDialog = new Stage();
		myDialog.initModality(Modality.WINDOW_MODAL);

		if (creature.getImage() == null) {
			if (MainGUI.imageZipFile == null) {
				try {
					creature.setImage(
							new Image(new File(MainGUI.IMAGEFOLDER + creature.getImagePath()).toURI().toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println(creature.getImagePath());
				ZipEntry ze = MainGUI.imageZipFile.getEntry(creature.getImagePath());
				Image img;
				try {
					img = new Image(MainGUI.imageZipFile.getInputStream(ze));
					creature.setImage(img);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

		ImageView iv = new ImageView(creature.getImage());
		VBox vbox = new VBox(new Text(creature.getName()), iv);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(10));

		Scene myDialogScene = new Scene(vbox);

		myDialog.setScene(myDialogScene);
		myDialog.show();
	}

	public Creature getCreature() {
		return creature;
	}

}
