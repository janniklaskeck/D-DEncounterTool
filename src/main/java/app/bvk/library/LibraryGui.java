package app.bvk.library;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private final List<LibraryEntry> libraryEntries = new ArrayList<>();
    private ObservableList<LibraryEntry> libraryEntryDisplayList;

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
        CreatureLibrary.getInstance();
        for (final Creature creature : CreatureLibrary.getInstance().getCreatures())
        {
            this.libraryEntries.add(new LibraryEntry(creature));
        }
        this.libraryEntryDisplayList = FXCollections.observableArrayList(this.libraryEntries);
        this.libraryList.setItems(this.libraryEntryDisplayList);
        this.addLibraryTextFieldFilter();

        this.setEntryAmountText();
    }

    private void addLibraryTextFieldFilter()
    {
        final FilteredList<LibraryEntry> filteredData = new FilteredList<>(this.libraryList.getItems(), predicate -> true);
        this.filterTextField.textProperty().addListener((obs, oldValue, newValue) ->
        {
            filteredData.setPredicate(libraryEntry ->
            {
                final boolean isEmpty = newValue == null || newValue.isEmpty();
                final String creatureName = libraryEntry.getCreature().nameProperty().get().toLowerCase();
                final String filterValue = newValue.toLowerCase();
                final boolean nameMatch = creatureName.contains(filterValue);
                return isEmpty || nameMatch;
            });
            this.libraryList.setItems(filteredData);
            this.setEntryAmountText();
        });
    }

    private void setEntryAmountText()
    {
        this.entryAmountText.setText(String.format("# %d", this.libraryList.getItems().size()));
    }

}
