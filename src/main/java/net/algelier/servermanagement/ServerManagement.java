package net.algelier.servermanagement;

import joptsimple.OptionSet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ServerManagement {

    private static ServerManagement instance;

    public static Logger logger;

    protected Configuration configuration;

    public static Logger getLogger() {
        return logger;
    }

    public void loadConfig() {

    }

    public abstract void onEnable();

    public abstract void onDisable();

    public static ServerManagement getInstance() {
        return instance;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public void log(Level level, String message) {
        logger.log(level, message);
    }
}
