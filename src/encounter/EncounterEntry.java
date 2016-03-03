package encounter;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;

import entity.Creature;
import entity.Player;
import gui.MainGUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EncounterEntry extends GridPane {

    private static final int MAXIMUMNUMBERS = 5;
    private Creature creature;
    private FXMLLoader loader;

    @FXML
    private Label name;

    @FXML
    private TextField initiativeTextField;

    @FXML
    private TextField healthTextField;

    @FXML
    private TextField armorClassTextField;

    @FXML
    private TextArea statusTextArea;

    @FXML
    private TextArea miscTextArea;

    @FXML
    private Button openImageButton;

    public EncounterEntry(Creature c) {
	this.creature = c;
	loader = new FXMLLoader(getClass().getResource("EncounterEntry.fxml"));
	loader.setRoot(this);
	loader.setController(this);
	try {
	    loader.load();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	if (creature.isSelected()) {
	    setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
	}

	initiativeTextField.setText(creature.getInitiative() + "");
	healthTextField.setText(creature.getHealth() + "");
	armorClassTextField.setText(creature.getArmorClass() + "");
	statusTextArea.setText(creature.getStatusNotes());
	miscTextArea.setText(creature.getMiscNotes());

	initiativeTextField.textProperty().addListener(new ChangeListener<String>() {
	    @Override
	    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		float initiative = 0;
		try {
		    initiative = Float.parseFloat(newValue);
		} catch (Exception e1) {
		    initiative = 0;
		}
		creature.setInitiative(initiative);
		if (newValue.length() > MAXIMUMNUMBERS) {
		    EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
			    initiativeTextField.setText(initiativeTextField.getText(0, MAXIMUMNUMBERS));
			}
		    });
		}
	    }
	});

	healthTextField.textProperty().addListener(new ChangeListener<String>() {
	    @Override
	    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		int health = 0;
		try {
		    health = Integer.parseInt(newValue);
		} catch (Exception e1) {
		    health = 0;
		}
		creature.setHealth(health);
		if (newValue.length() > MAXIMUMNUMBERS) {
		    EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
			    healthTextField.setText(healthTextField.getText(0, MAXIMUMNUMBERS));
			}
		    });
		}

	    }
	});

	armorClassTextField.textProperty().addListener(new ChangeListener<String>() {

	    @Override
	    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		int armorClass = 0;
		try {
		    armorClass = Integer.parseInt(newValue);
		} catch (Exception e1) {
		    armorClass = 0;
		}
		creature.setArmorClass(armorClass);
		if (newValue.length() > MAXIMUMNUMBERS) {
		    EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
			    armorClassTextField.setText(armorClassTextField.getText(0, MAXIMUMNUMBERS));
			}
		    });
		}

	    }
	});

	statusTextArea.textProperty().addListener(new ChangeListener<String>() {
	    @Override
	    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		creature.setStatusNotes(newValue);
	    }
	});

	miscTextArea.textProperty().addListener(new ChangeListener<String>() {
	    @Override
	    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		creature.setMiscNotes(newValue);
	    }
	});

	if (creature.getClass().equals(Player.class)) {
	    openImageButton.setDisable(true);
	}
    }

    @FXML
    private void initialize() {
	name.setText(creature.getName());
    }

    @FXML
    private void openImage() {
	final Stage myDialog = new Stage();
	myDialog.initModality(Modality.WINDOW_MODAL);

	if (creature.getImage() == null) {
	    try {
		creature.setImage(
			new Image(new File(MainGUI.IMAGEFOLDER + creature.getImagePath()).toURI().toString()));
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	ImageView iv = new ImageView(creature.getImage());
	VBox vbox = new VBox(iv);
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
