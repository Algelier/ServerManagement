package net.algelier.servermanagement;

import joptsimple.OptionSet;

import java.io.IOException;
import java.util.logging.Logger;

public abstract class ServerManagement {

    private static ServerManagement instance;

    public static Logger logger;

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
}
