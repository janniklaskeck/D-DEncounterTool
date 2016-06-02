package app.bvk.utils;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.zip.ZipEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.encounter.Encounter;
import app.bvk.entity.Creature;
import app.bvk.entity.Player;
import app.bvk.gui.MainGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
    private static File imageFile = null;
    private static final String NAMESTRING = "Name: ";
    private static final String CANCELSTRING = "Cancel";
    private static final String ENTERPLAYERNAMESTRING = "Enter Player Name";

    private Utils() {

    }

    public static void showImageFrame(Creature creature) {
        Image img;
        final Stage imgFrame = new Stage();
        imgFrame.getIcons().add(Settings.getInstance().getImageIcon());
        imgFrame.initModality(Modality.WINDOW_MODAL);

        if (creature.getImage() == null) {
            if (Settings.getInstance().getImageZipFile() == null) {
                img = new Image(
                        new File(Settings.getInstance().getImageFolder() + creature.getImagePath()).toURI().toString());
            } else {
                ZipEntry ze = Settings.getInstance().getImageZipFile().getEntry(creature.getImagePath());
                try {
                    img = new Image(Settings.getInstance().getImageZipFile().getInputStream(ze));
                } catch (IOException e) {
                    LOGGER.error("ERROR while loading image, using icon instead", e);
                    img = new Image(MainGUI.class.getResourceAsStream("icon.png"));
                }
            }
            creature.setImage(img);
        } else {
            img = creature.getImage();
        }

        final WrappedImageView iv = new WrappedImageView(creature.getImage());
        final VBox vbox = new VBox(iv);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));

        final Scene myDialogScene = new Scene(vbox);

        final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        final double width = img.getWidth() >= gd.getDisplayMode().getWidth() * 0.8
                ? gd.getDisplayMode().getWidth() * 0.8 : img.getWidth();
        final double height = img.getHeight() >= gd.getDisplayMode().getHeight() * 0.8
                ? gd.getDisplayMode().getHeight() * 0.8 : img.getHeight();
        final double windowXPos = 80;
        final double windowYPos = 45;

        imgFrame.setWidth(width);
        imgFrame.setHeight(height);
        imgFrame.setX(windowXPos);
        imgFrame.setY(windowYPos);

        imgFrame.setScene(myDialogScene);
        imgFrame.show();
    }

    public static void newLibraryEntryWindow() {
        Dialog<Creature> d = new Dialog<>();
        Stage stage = (Stage) d.getDialogPane().getScene().getWindow();
        stage.getIcons().add(Settings.getInstance().getImageIcon());
        d.initOwner(Settings.getInstance().getMainStage());
        d.initModality(Modality.WINDOW_MODAL);
        d.setTitle(ENTERPLAYERNAMESTRING);
        d.setResizable(false);
        Label name = new Label(NAMESTRING);
        TextField tfName = new TextField();
        Label image = new Label(NAMESTRING);
        TextField imageName = new TextField();
        imageName.setEditable(false);
        Button chooseImage = new Button("Select Image");
        chooseImage.setOnAction(event -> {
            final FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File(System.getProperty("user.dir")));
            fc.getExtensionFilters().add(new ExtensionFilter("Image File", "*.png", "*.jpeg", "*.bmp"));
            File file = fc.showOpenDialog(Settings.getInstance().getMainStage());
            if (file != null) {
                imageFile = file;
                imageName.setText(file.getName());
            }
        });

        GridPane grid = new GridPane();
        grid.add(name, 0, 0);
        grid.add(tfName, 1, 0);
        grid.add(image, 0, 1);
        grid.add(imageName, 1, 1);
        grid.add(chooseImage, 2, 1);
        d.getDialogPane().setContent(grid);
        ButtonType okButton = new ButtonType("Save", ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType(CANCELSTRING, ButtonData.CANCEL_CLOSE);
        d.getDialogPane().getButtonTypes().add(okButton);
        d.getDialogPane().getButtonTypes().add(cancelButton);
        d.setResultConverter(param -> {
            if (param == okButton) {
                return new Creature(tfName.getText(), imageFile.getName());
            }
            if (param == cancelButton) {
                return null;
            }
            return null;
        });

        Optional<Creature> a = d.showAndWait();
        if (a.isPresent()) {
            Settings.getInstance().getCreatureList().add(a.get());
        }
    }

    public static void newPlayerWindow(Encounter encounter) {
        Dialog<String> d = new Dialog<>();
        Stage stage = (Stage) d.getDialogPane().getScene().getWindow();
        stage.getIcons().add(Settings.getInstance().getImageIcon());
        d.initOwner(Settings.getInstance().getMainStage());
        d.initModality(Modality.WINDOW_MODAL);
        d.setTitle(ENTERPLAYERNAMESTRING);
        d.setResizable(false);
        Label name = new Label(NAMESTRING);
        TextField tf = new TextField();

        GridPane grid = new GridPane();
        grid.add(name, 0, 0);
        grid.add(tf, 1, 0);
        d.getDialogPane().setContent(grid);
        ButtonType okButton = new ButtonType("Save", ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType(CANCELSTRING, ButtonData.CANCEL_CLOSE);
        d.getDialogPane().getButtonTypes().add(okButton);
        d.getDialogPane().getButtonTypes().add(cancelButton);
        d.setResultConverter(param -> {
            if (param == okButton) {
                return tf.getText();
            }
            if (param == cancelButton) {
                return null;
            }
            return null;
        });

        Optional<String> a = d.showAndWait();
        if (a.isPresent()) {
            encounter.addCreature(new Player(tf.getText(), ""));
        }
    }

    public static void newNPCWindow(Encounter encounter) {
        Dialog<String> d = new Dialog<>();
        Stage stage = (Stage) d.getDialogPane().getScene().getWindow();
        stage.getIcons().add(Settings.getInstance().getImageIcon());
        d.initOwner(Settings.getInstance().getMainStage());
        d.initModality(Modality.WINDOW_MODAL);
        d.setTitle(ENTERPLAYERNAMESTRING);
        d.setResizable(false);
        Label name = new Label("Filter: ");
        TextField filterTF = new TextField();
        ListView<String> lv = new ListView<>();
        lv.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                d.resultProperty().set(lv.getSelectionModel().getSelectedItem());
            }
        });
        ArrayList<String> names = new ArrayList<>();
        for (Creature le : Settings.getInstance().getCreatureList()) {
            names.add(le.getName().get());
        }
        ObservableList<String> ol = FXCollections.observableArrayList(names);
        lv.setItems(ol);
        FilteredList<String> fData = new FilteredList<>(ol, p -> true);
        filterTF.textProperty().addListener((obs, oldValue, newValue) -> {
            fData.setPredicate(t -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                if (t.toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }
                return false;
            });
            lv.setItems(fData);
        });

        GridPane grid = new GridPane();
        grid.add(name, 0, 0);
        grid.add(filterTF, 1, 0);
        grid.add(lv, 0, 1, 2, 1);
        d.getDialogPane().setContent(grid);

        ButtonType okButton = new ButtonType("Save", ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType(CANCELSTRING, ButtonData.CANCEL_CLOSE);
        d.getDialogPane().getButtonTypes().add(okButton);
        d.getDialogPane().getButtonTypes().add(cancelButton);
        d.setResultConverter(param -> {
            if (param == okButton) {
                return lv.getSelectionModel().getSelectedItem();
            }
            if (param == cancelButton) {
                return null;
            }
            return null;

        });

        Optional<String> a = d.showAndWait();
        if (a.isPresent()) {
            encounter.addCreature(a.get());
        }
    }
}
