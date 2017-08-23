package app.bvk.library;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.entity.Creature;
import app.bvk.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class LibraryGui extends BorderPane
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryGui.class);

    @FXML
    private Button addLibraryEntryButton;
    @FXML
    private Text entryAmountText;
    @FXML
    private TextField filterTextField;
    @FXML
    private ListView<LibraryEntry> libraryList;

    private FXMLLoader loader;

    public LibraryGui()
    {
        this.loader = new FXMLLoader(this.getClass().getClassLoader().getResource("LibraryGui.fxml"));
        this.loader.setRoot(this);
        this.loader.setController(this);

        try
        {
            this.loader.load();
        }
        catch (final IOException e)
        {
            LOGGER.error("ERROR while loading library fxml", e);
        }
    }

    @FXML
    private void initialize()
    {
        this.addLibraryEntryButton.setOnAction(event -> Utils.newLibraryEntryWindow(this.getScene().getWindow()));
        this.addLibraryTextFieldFilter();
        CreatureLibrary.getInstance();
        final ObservableList<LibraryEntry> leList = FXCollections.observableArrayList();
        for (final Creature c : CreatureLibrary.getInstance().getCreatures())
        {
            leList.add(new LibraryEntry(c));
        }

        this.libraryList.setItems(leList);
    }

    private void addLibraryTextFieldFilter()
    {
        final FilteredList<LibraryEntry> fData = new FilteredList<>(this.libraryList.getItems(), p -> true);
        this.filterTextField.textProperty().addListener((obs, oldValue, newValue) ->
        {
            fData.setPredicate(t ->
            {
                final boolean isEmpty = newValue == null || newValue.isEmpty();
                final boolean nameMatch = t.getCreature().getName().get().toLowerCase().contains(newValue.toLowerCase());
                return isEmpty || nameMatch;
            });
            this.libraryList.setItems(fData);
        });
    }

}
