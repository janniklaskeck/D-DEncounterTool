package utils;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;

import entity.Creature;
import gui.MainGUI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Utils {

	public static void showImageFrame(Creature creature) {
		Image img = null;
		final Stage myDialog = new Stage();
		myDialog.initModality(Modality.WINDOW_MODAL);

		if (creature.getImage() == null) {
			System.out.println("loading");
			if (MainGUI.imageZipFile == null) {
				try {
					img = new Image(new File(MainGUI.IMAGEFOLDER + creature.getImagePath()).toURI().toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				ZipEntry ze = MainGUI.imageZipFile.getEntry(creature.getImagePath());
				try {
					img = new Image(MainGUI.imageZipFile.getInputStream(ze));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			creature.setImage(img);
		}

		ImageView iv = new ImageView(creature.getImage());
		VBox vbox = new VBox(iv);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(10));

		Scene myDialogScene = new Scene(vbox);

		myDialog.setScene(myDialogScene);
		myDialog.show();
	}

}
