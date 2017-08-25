package app.bvk.encounter.dialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.entity.Creature;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import javafx.stage.Window;

public class EncounterNpcWindow extends Dialog<Creature>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EncounterNpcWindow.class);
    private static final String SELECT_CREATURE_TEXT = "Select Creature";
    private final NpcDialogPane npcDialogPane = new NpcDialogPane(this);

    public EncounterNpcWindow(final Window ownerWindow)
    {
        this.setDialogPane(this.npcDialogPane);
        this.initOwner(ownerWindow);
        this.initModality(Modality.WINDOW_MODAL);
        this.setTitle(SELECT_CREATURE_TEXT);
        this.setResizable(false);
        this.setResultConverter(button ->
        {
            final ButtonData buttonData = button.getButtonData();
            if (buttonData == ButtonData.APPLY)
            {
                LOGGER.debug("Pressed OK.");
                return this.npcDialogPane.getSelectedCreature();
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
