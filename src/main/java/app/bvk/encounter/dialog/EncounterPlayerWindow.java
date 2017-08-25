package app.bvk.encounter.dialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.entity.Player;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import javafx.stage.Window;

public class EncounterPlayerWindow extends Dialog<Player>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EncounterPlayerWindow.class);
    private static final String CREATE_PLAYER_TEXT = "Create new Player";
    private final PlayerDialogPane playerDialogPane = new PlayerDialogPane();

    public EncounterPlayerWindow(final Window ownerWindow)
    {
        this.setDialogPane(this.playerDialogPane);
        this.initOwner(ownerWindow);
        this.initModality(Modality.WINDOW_MODAL);
        this.setTitle(CREATE_PLAYER_TEXT);
        this.setResizable(false);
        this.setResultConverter(button ->
        {
            final ButtonData buttonData = button.getButtonData();
            if (buttonData == ButtonData.APPLY)
            {
                LOGGER.debug("Pressed OK.");
                return this.playerDialogPane.getPlayer();
            }
            else if (buttonData == ButtonData.CANCEL_CLOSE)
            {
                LOGGER.debug("Pressed Cancel.");
                return null;
            }
            return null;
        });
    }
}
