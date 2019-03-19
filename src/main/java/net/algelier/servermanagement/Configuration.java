package net.algelier.servermanagement;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import joptsimple.OptionSet;
import net.algelier.servermanagement.utils.MiscUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class Configuration {

    private final ServerManagement serverManagement;
    public String redisIp;
    public String redisPassword;
    public int redisPort;
    public String sqlUrl;
    public String sqlUser;
    public String sqlPassword;
    private JsonObject jsonConfiguration;

    public Configuration(ServerManagement serverManagement, OptionSet options) {
        this.serverManagement = serverManagement;

        if (options.has("d"))
            createDefaultConfiguration();

        try {
            this.loadConfiguration(options.valueOf("c").toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfiguration(String path) throws IOException {
        this.serverManagement.log(Level.INFO, "Configuration file is: " + path);
        File configurationFile = new File(path);

        if (!configurationFile.exists()) {
            this.serverManagement.log(Level.SEVERE, "Configuration file doesn't exist.");
            System.exit(4);
        }

        InputStreamReader reader = new InputStreamReader(new FileInputStream(configurationFile), StandardCharsets.UTF_8);
        try {
            this.jsonConfiguration = new JsonParser().parse(reader).getAsJsonObject();

        } finally {
            try {
                reader.close();
            } catch (IOException ignored) {}

            if (!validateJson(jsonConfiguration)) {
                this.serverManagement.log(Level.SEVERE, "Configuration file isn't valid Please modify the default configuration file!");
                System.exit(5);
            }

            this.redisIp = jsonConfiguration.get("redis-ip").getAsString();
            this.redisPort = jsonConfiguration.get("redis-port").getAsInt();
            this.redisPassword = jsonConfiguration.get("redis-password").getAsString();
            this.sqlUrl = jsonConfiguration.get("sql-url").getAsString();
            this.sqlUser = jsonConfiguration.get("sql-user").getAsString();
            this.sqlPassword = jsonConfiguration.get("sql-password").getAsString();
        }
    }

    public void createDefaultConfiguration() {
        try {
            File destinationFile = new File(MiscUtils.getJarFolder(), "config.json");
            FileUtils.copyURLToFile(Configuration.class.getResource("/config.json"), destinationFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.serverManagement.log(Level.INFO, "Default configuration file created.");
        System.exit(3);
    }

    public JsonObject getJsonConfiguration() {
        return jsonConfiguration;
    }

    public boolean validateJson(JsonObject object) {
        boolean flag = true;

        /** Common **/
        if (!object.has("redis-ip")) flag = false;
        if (!object.has("redis-port")) flag = false;
        if (!object.has("redis-password")) flag = false;
        if (!object.has("sql-url")) flag = false;
        if (!object.has("sql-user")) flag = false;
        if (!object.has("sql-password")) flag = false;
        if (!object.has("web-domain")) flag = false;

        /** Client **/
        if (!object.has("max-weight")) flag = false;

        return flag;
    }
}
