package app.bvk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.encounter.EncounterGui;
import app.bvk.library.LibraryGui;
import app.bvk.settings.SettingsGui;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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

    @FXML
    private ProgressBar loadingProgressBar;
    @FXML
    private Label loadingProgressLabel;

    private SimpleDoubleProperty barProgress;
    private SimpleStringProperty progressText;

    @FXML
    public void initialize()
    {
        this.settingsTab.setContent(new SettingsGui());
        this.libraryTab.setContent(new LibraryGui());
        this.encounterTab.setContent(new EncounterGui());

        LOGGER.debug("Main GUI initialized.");
    }

    private void initProgressDisplay()
    {
        this.barProgress = new SimpleDoubleProperty(0.0);
        this.progressText = new SimpleStringProperty();
        this.loadingProgressBar.progressProperty().bindBidirectional(this.barProgress);
        this.loadingProgressLabel.textProperty().bindBidirectional(this.progressText);
    }

    private void setProgressTextAndBar(final double percentage)
    {
        this.barProgress.set(percentage);
        Platform.runLater(() -> this.progressText.set("Loading progress: " + (int) (percentage * 100) + "%"));
    }
}
