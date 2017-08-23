package app.bvk.utils;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.encounter.Encounter;
import app.bvk.entity.Creature;
import app.bvk.entity.Player;
import app.bvk.gui.MainGUI;
import app.bvk.library.CreatureLibrary;
import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileInputStream;
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
import javafx.stage.Window;

public class Utils
{

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
    private static File imageFile = null;
    private static final String NAMESTRING = "Name: ";
    private static final String CANCELSTRING = "Cancel";
    private static final String ENTERPLAYERNAMESTRING = "Enter Player Name";

    public static final Image ICON = new Image(Utils.class.getClassLoader().getResourceAsStream("icon.png"));

    private Utils()
    {

    }

    public static void showImageFrame(final Creature creature)
    {
        Image img;
        final Stage imgFrame = new Stage();
        imgFrame.getIcons().add(ICON);
        imgFrame.initModality(Modality.WINDOW_MODAL);

        if (creature.getImage() == null)
        {
            final File creatureImageFile = CreatureLibrary.getInstance().getLibraryFilePath().resolve(creature.getImagePath()).toFile();
            try (final TFileInputStream is = new TFileInputStream(new TFile(creatureImageFile));)
            {
                img = new Image(is);
            }
            catch (final IOException e)
            {
                LOGGER.error("ERROR while loading image, using icon instead", e);
                img = new Image(MainGUI.class.getResourceAsStream("icon.png"));
            }

            creature.setImage(img);
        }
        else
        {
            img = creature.getImage();
        }
        final VBox vbox = new VBox();

        final Scene myDialogScene = new Scene(vbox);
        final WrappedImageView iv = new WrappedImageView(myDialogScene, creature.getImage());
        vbox.getChildren().add(iv);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));

        final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        final double width = img.getWidth() >= gd.getDisplayMode().getWidth() * 0.8 ? gd.getDisplayMode().getWidth() * 0.8 : img.getWidth();
        final double height = img.getHeight() >= gd.getDisplayMode().getHeight() * 0.8 ? gd.getDisplayMode().getHeight() * 0.8 : img.getHeight();
        final double windowXPos = 80;
        final double windowYPos = 45;

        imgFrame.setWidth(width);
        imgFrame.setHeight(height);
        imgFrame.setX(windowXPos);
        imgFrame.setY(windowYPos);

        imgFrame.setScene(myDialogScene);
        imgFrame.show();
    }

    public static Stage showSavingWarning()
    {
        final Stage stage = new Stage();
        stage.getIcons().add(ICON);
        stage.setTitle("Saving! Don't close this Window!");
        stage.initModality(Modality.APPLICATION_MODAL);
        final Label label = new Label("Saving!");
        final Scene scene = new Scene(label);
        stage.setScene(scene);
        stage.setWidth(200);
        stage.setHeight(100);
        stage.setResizable(false);
        stage.show();
        return stage;
    }

    public static void newLibraryEntryWindow(final Window ownerWindow)
    {
        final Dialog<Creature> d = new Dialog<>();
        final Stage stage = (Stage) d.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ICON);
        d.initOwner(ownerWindow);
        d.initModality(Modality.WINDOW_MODAL);
        d.setTitle(ENTERPLAYERNAMESTRING);
        d.setResizable(false);
        final Label name = new Label(NAMESTRING);
        final TextField tfName = new TextField();
        final Label image = new Label(NAMESTRING);
        final TextField imageName = new TextField();
        imageName.setEditable(false);
        final Button chooseImage = new Button("Select Image");
        chooseImage.setOnAction(event ->
        {
            final FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File(System.getProperty("user.dir")));
            fc.getExtensionFilters().add(new ExtensionFilter("Image File", "*.png", "*.jpeg", "*.bmp"));
            final File file = fc.showOpenDialog(ownerWindow);
            if (file != null)
            {
                imageFile = file;
                imageName.setText(file.getName());
            }
        });

        final GridPane grid = new GridPane();
        grid.add(name, 0, 0);
        grid.add(tfName, 1, 0);
        grid.add(image, 0, 1);
        grid.add(imageName, 1, 1);
        grid.add(chooseImage, 2, 1);
        d.getDialogPane().setContent(grid);
        final ButtonType okButton = new ButtonType("Save", ButtonData.OK_DONE);
        final ButtonType cancelButton = new ButtonType(CANCELSTRING, ButtonData.CANCEL_CLOSE);
        d.getDialogPane().getButtonTypes().add(okButton);
        d.getDialogPane().getButtonTypes().add(cancelButton);
        d.setResultConverter(param ->
        {
            if (param == okButton)
            {
                return new Creature(tfName.getText(), imageFile.getName());
            }
            if (param == cancelButton)
            {
                return null;
            }
            return null;
        });

        final Optional<Creature> a = d.showAndWait();
        if (a.isPresent())
        {
            CreatureLibrary.getInstance().addCreature(a.get());
        }
    }

    public static void newPlayerWindow(final Window ownerWindow, final Encounter encounter)
    {
        final Dialog<String> d = new Dialog<>();
        final Stage stage = (Stage) d.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ICON);
        d.initOwner(ownerWindow);
        d.initModality(Modality.WINDOW_MODAL);
        d.setTitle(ENTERPLAYERNAMESTRING);
        d.setResizable(false);
        final Label name = new Label(NAMESTRING);
        final TextField tf = new TextField();

        final GridPane grid = new GridPane();
        grid.add(name, 0, 0);
        grid.add(tf, 1, 0);
        d.getDialogPane().setContent(grid);
        final ButtonType okButton = new ButtonType("Save", ButtonData.OK_DONE);
        final ButtonType cancelButton = new ButtonType(CANCELSTRING, ButtonData.CANCEL_CLOSE);
        d.getDialogPane().getButtonTypes().add(okButton);
        d.getDialogPane().getButtonTypes().add(cancelButton);
        d.setResultConverter(param ->
        {
            if (param == okButton)
            {
                return tf.getText();
            }
            if (param == cancelButton)
            {
                return null;
            }
            return null;
        });

        final Optional<String> a = d.showAndWait();
        if (a.isPresent())
        {
            encounter.addCreature(new Player(tf.getText(), ""));
        }
    }

    public static void newNPCWindow(final Window ownerWindow, final Encounter encounter)
    {
        final Dialog<String> d = new Dialog<>();
        final Stage stage = (Stage) d.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ICON);
        d.initOwner(ownerWindow);
        d.initModality(Modality.WINDOW_MODAL);
        d.setTitle(ENTERPLAYERNAMESTRING);
        d.setResizable(false);
        final Label name = new Label("Filter: ");
        final TextField filterTF = new TextField();
        final ListView<String> lv = new ListView<>();
        lv.setOnMouseClicked(event ->
        {
            if (event.getClickCount() == 2)
            {
                d.resultProperty().set(lv.getSelectionModel().getSelectedItem());
            }
        });
        final ArrayList<String> names = new ArrayList<>();
        for (final Creature le : CreatureLibrary.getInstance().getCreatures())
        {
            names.add(le.getName().get());
        }
        final ObservableList<String> ol = FXCollections.observableArrayList(names);
        lv.setItems(ol);
        final FilteredList<String> fData = new FilteredList<>(ol, p -> true);
        filterTF.textProperty().addListener((obs, oldValue, newValue) ->
        {
            fData.setPredicate(t ->
            {
                final boolean isEmpty = newValue == null || newValue.isEmpty();
                final boolean nameMatch = t.toLowerCase().contains(newValue.toLowerCase());
                return isEmpty || nameMatch;
            });
            lv.setItems(fData);
        });

        final GridPane grid = new GridPane();
        grid.add(name, 0, 0);
        grid.add(filterTF, 1, 0);
        grid.add(lv, 0, 1, 2, 1);
        d.getDialogPane().setContent(grid);

        final ButtonType okButton = new ButtonType("Save", ButtonData.OK_DONE);
        final ButtonType cancelButton = new ButtonType(CANCELSTRING, ButtonData.CANCEL_CLOSE);
        d.getDialogPane().getButtonTypes().add(okButton);
        d.getDialogPane().getButtonTypes().add(cancelButton);
        d.setResultConverter(param ->
        {
            if (param == okButton)
            {
                return lv.getSelectionModel().getSelectedItem();
            }
            if (param == cancelButton)
            {
                return null;
            }
            return null;

        });

        final Optional<String> a = d.showAndWait();
        if (a.isPresent())
        {
            encounter.addCreature(a.get());
        }
    }
}
