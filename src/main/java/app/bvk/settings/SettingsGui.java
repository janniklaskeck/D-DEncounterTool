package app.bvk.settings;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;

public class SettingsGui extends GridPane
{

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsGui.class);

    @FXML
    private CheckBox autoSaveCheckBox;

    private FXMLLoader loader;

    public SettingsGui()
    {
        this.loader = new FXMLLoader(this.getClass().getClassLoader().getResource("SettingsGui.fxml"));
        this.loader.setRoot(this);
        this.loader.setController(this);

        try
        {
            this.loader.load();
        }
        catch (final IOException e)
        {
            LOGGER.error("ERROR while loading settings fxml", e);
        }
    }

    @FXML
    private void initialize()
    {
        this.autoSaveCheckBox.selectedProperty().addListener((obs, oldValue, newValue) ->
        {
        });
    }

}
