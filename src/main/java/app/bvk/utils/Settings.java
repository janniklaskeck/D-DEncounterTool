package app.bvk.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Settings
{

    private static final Logger LOGGER = LoggerFactory.getLogger(Settings.class);
    private static Settings instance;

    private Settings()
    {
    }

    public static synchronized Settings getInstance()
    {
        if (instance == null)
        {
            instance = new Settings();
        }
        return instance;
    }
}
