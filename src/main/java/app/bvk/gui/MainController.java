package app.bvk.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.encounter.EncounterGui;
import app.bvk.library.LibraryGui;
import app.bvk.settings.SettingsGui;
import app.bvk.utilities.UtilitiesPane;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

public class MainController
{

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @FXML
    private BorderPane rootBorderPane;
    @FXML
    private Tab settingsTab;
    @FXML
    private Tab libraryTab;
    @FXML
    private Tab encounterTab;

    // TODO improve access
    public static final UtilitiesPane pane = new UtilitiesPane();

    @FXML
    public void initialize()
    {
        this.settingsTab.setContent(new SettingsGui());
        this.libraryTab.setContent(new LibraryGui());
        this.encounterTab.setContent(new EncounterGui());
        this.rootBorderPane.setRight(pane);

        LOGGER.debug("Main GUI initialized.");
    }
}
