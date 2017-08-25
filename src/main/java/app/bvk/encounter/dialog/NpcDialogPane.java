package app.bvk.encounter.dialog;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.entity.Creature;
import app.bvk.library.CreatureLibrary;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class NpcDialogPane extends DialogPane
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NpcDialogPane.class);
    private FXMLLoader loader;

    @FXML
    private ListView<Creature> creatureListView;
    @FXML
    private TextField filterTextField;

    private ObjectProperty<Creature> selectedCreature = new SimpleObjectProperty<>();
    private Dialog<Creature> parentDialog;

    public NpcDialogPane(final Dialog<Creature> parent)
    {
        this.parentDialog = parent;
        this.loader = new FXMLLoader(this.getClass().getClassLoader().getResource("EncounterNpcWindowGui.fxml"));
        this.loader.setRoot(this);
        this.loader.setController(this);
        try
        {
            this.loader.load();
        }
        catch (final IOException e)
        {
            LOGGER.error("ERROR while loading encounter fxml", e);
        }
    }

    @FXML
    private void initialize()
    {
        final ButtonType addButtonType = new ButtonType("Add", ButtonData.APPLY);
        final ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        this.getButtonTypes().add(addButtonType);
        this.getButtonTypes().add(cancelButtonType);
        final Button addButton = (Button) this.lookupButton(addButtonType);
        addButton.setDefaultButton(true);
        final Button cancelButton = (Button) this.lookupButton(cancelButtonType);
        cancelButton.setCancelButton(true);
        Platform.runLater(() -> this.filterTextField.requestFocus());
        this.creatureListView.setCellFactory(cellData -> new ListCell<Creature>()
        {
            @Override
            protected void updateItem(final Creature creature, final boolean empty)
            {
                super.updateItem(creature, empty);
                if (empty || creature == null)
                {
                    this.setText(null);
                    this.setGraphic(null);
                }
                else
                {
                    this.setText(creature.getName().getValue());
                }
            }
        });
        this.creatureListView.setOnMouseClicked(event ->
        {
            if (event.getClickCount() == 2)
            {
                this.parentDialog.resultProperty().set(this.creatureListView.getSelectionModel().getSelectedItem());
            }
        });
        final ObservableList<Creature> creatureObservableList = FXCollections.observableArrayList(CreatureLibrary.getInstance().getCreatures());
        this.creatureListView.setItems(creatureObservableList);
        this.filterTextField.textProperty().addListener((obs, oldValue, newValue) ->
        {
            final FilteredList<Creature> filteredList = new FilteredList<>(creatureObservableList, predicate -> true);
            filteredList.setPredicate(creature ->
            {
                final boolean isEmpty = newValue == null || newValue.isEmpty();
                final boolean nameMatch = creature.getName().getValue().toLowerCase().contains(newValue.toLowerCase());
                return isEmpty || nameMatch;
            });
            this.creatureListView.setItems(filteredList);
        });
        this.selectedCreature.bind(this.creatureListView.getSelectionModel().selectedItemProperty());
    }

    public Creature getSelectedCreature()
    {
        return this.selectedCreature.getValue();
    }
}
