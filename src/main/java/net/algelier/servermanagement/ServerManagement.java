package net.algelier.servermanagement;

import jline.console.ConsoleReader;
import joptsimple.OptionSet;
import net.algelier.servermanagement.client.ServerManagementClient;
import net.algelier.servermanagement.common.database.DatabaseConnector;
import net.algelier.servermanagement.common.database.RedisSubscriber;
import net.algelier.servermanagement.common.log.ServerManagementLogger;
import net.algelier.servermanagement.server.ServerManagementServer;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ServerManagement {

    private static ServerManagement instance;
    protected static Logger logger;

    protected final ConsoleReader consoleReader;
    protected final ScheduledExecutorService scheduledExecutorService;

    public boolean isRunning;

    protected UUID uuid;
    protected Configuration configuration;
    protected DatabaseConnector databaseConnector;
    protected RedisSubscriber redisSubscriber;
    protected OptionSet options;

    public ServerManagement(OptionSet option) throws IOException {
        instance = this;
        uuid = UUID.randomUUID();

        AnsiConsole.systemInstall();
        consoleReader = new ConsoleReader();
        consoleReader.setExpandEvents(false);

        logger = new ServerManagementLogger(this);
        logger.info("ServerManagement version 1.0.0");
        logger.info("----------------------------------------");

        this.scheduledExecutorService = Executors.newScheduledThreadPool(16);
        this.options = option;
        loadConfig();
        this.databaseConnector = new DatabaseConnector(this);

        this.redisSubscriber = new RedisSubscriber(this);

        this.onEnable();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.log(Level.INFO, "Shutdown asked");
            this.shutdown();
            this.log(Level.INFO, "Bye");
        }));

        isRunning = true;
    }

    public static Logger getLogger() {
        return logger;
    }

    public void loadConfig() {
        this.configuration = new Configuration(this,options);
    }

    public void shutdown() {
        isRunning = false;

        onDisable();
        scheduledExecutorService.shutdown();

        this.redisSubscriber.disable();
    }

    public ConsoleReader getConsoleReader() {
        return consoleReader;
    }

    public DatabaseConnector getDatabaseConnector() {
        return databaseConnector;
    }

    public RedisSubscriber getRedisSubscriber() {
        return redisSubscriber;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public ServerManagementClient getAsClient() {
        if (this instanceof ServerManagementClient)
            return (ServerManagementClient) this;

        else
            return null;
    }

    public ServerManagementServer getAsServer() {
        if (this instanceof ServerManagementServer)
            return (ServerManagementServer) this;
        else
            return null;
    }

    public UUID getUUID() {
        return uuid;
    }

    public OptionSet getOptions() {
        return options;
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

    public enum RestrictionMode {
        NONE("none"),
        WHITELIST("whitelist"),
        BLACKLIST("blacklist");

        private String mode;

        RestrictionMode(String mode) {
            this.mode = mode;
        }

        public String getMode() {
            return mode;
        }

        static public RestrictionMode valueFrom(String mode) {
            for (RestrictionMode data : RestrictionMode.values()) {
                if (data.getMode().equalsIgnoreCase(mode))
                    return data;
            }

            return RestrictionMode.NONE;
        }
    }
}
