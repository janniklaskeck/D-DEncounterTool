package app.bvk.library;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.entity.Creature;
import app.bvk.library.editor.EditorWindow;
import app.bvk.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

/**
 *
 * @author Niklas 12.06.2016
 *
 */
public class LibraryEntry extends BorderPane
{

    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryEntry.class);

    private Creature creature;
    FXMLLoader loader;

    @FXML
    private Text nameText;
    @FXML
    private Button openImageButton;
    @FXML
    private Button openStatsButton;

    public LibraryEntry(final Creature creature)
    {
        this.creature = creature;
        this.loader = new FXMLLoader(this.getClass().getClassLoader().getResource("LibraryEntryGui.fxml"));
        this.loader.setRoot(this);
        this.loader.setController(this);

        try
        {
            this.loader.load();
        }
        catch (final IOException e)
        {
            LOGGER.error("ERROR while loadgin libraryentry fxml", e);
        }
    }

    @FXML
    protected void initialize()
    {
        this.nameText.setText(this.creature.getName());
        this.openImageButton.setOnAction(event -> Utils.showImageFrame(this.getCreature()));
        this.openStatsButton.setOnAction(event -> new EditorWindow());
    }

    public Creature getCreature()
    {
        return this.creature;
    }

}
