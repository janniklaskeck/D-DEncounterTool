package app.bvk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.gui.MainGUI;
import app.bvk.utils.Settings;
import javafx.application.Application;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class EncounterTool
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EncounterTool.class);

    public static void main(final String[] args)
    {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
        try
        {
            final ZipFile zipImages = new ZipFile(Settings.getInstance().getCreatureFolder() + "/images.zip");
            final ZipFile zipCreatures = new ZipFile(Settings.getInstance().getCreatureFolder() + "/creatures.zip");
            Settings.getInstance().setImageZipFile(zipImages);
            Settings.getInstance().setCreatureZipFile(zipCreatures);
        }
        catch (final ZipException e)
        {
            LOGGER.error("ERROR while loading zip file", e);
            Settings.getInstance().setImageZipFile(null);
        }
        Application.launch(MainGUI.class, args);
    }
}
