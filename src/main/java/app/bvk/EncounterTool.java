package app.bvk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.gui.MainGUI;
import javafx.application.Application;

public class EncounterTool
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EncounterTool.class);

    public static void main(final String[] args)
    {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
        LOGGER.info("Starting Application.");
        Application.launch(MainGUI.class, args);
    }
}
