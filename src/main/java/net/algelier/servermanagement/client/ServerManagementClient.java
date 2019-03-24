package net.algelier.servermanagement.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import joptsimple.OptionSet;
import net.algelier.servermanagement.ServerManagement;
import net.algelier.servermanagement.client.docker.DockerAPI;
import net.algelier.servermanagement.utils.MiscUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class ServerManagementClient extends ServerManagement {

    private String templatesDomain;
    private int maxWeight;
    private int minWeight;

    private File serverFolder;

    private RestrictionMode restrictionMode;
    private List<String> whitelist;
    private List<String> blacklist;

    private ClientConnectionManager connectionManager;


    private DockerAPI dockerAPI;
    private JsonObject dockerConfig;

    public ServerManagementClient(OptionSet option) throws IOException {
        super(option);
        dockerAPI = new DockerAPI();
    }

    @Override
    public void onEnable() {
        this.log(Level.INFO, "Starting ServerManagement Client...");
        this.loadConfig();

        serverFolder.mkdir();

        try {
            FileUtils.forceDelete(serverFolder);
            FileUtils.forceMkdir(serverFolder);

        } catch (IOException e) {
            e.printStackTrace();
        }

        connectionManager = new ClientConnectionManager(this);

    }

    @Override
    public void loadConfig() {
        super.loadConfig();

        JsonElement parsed = null;
        try {
            File file = new File("DockerConfig.json");
            file.createNewFile();

            InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            parsed = new JsonParser().parse(reader);
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (parsed == null)
            dockerConfig = new JsonObject();
        else
            dockerConfig = parsed.getAsJsonObject();

        blacklist = new ArrayList<>();
        whitelist = new ArrayList<>();

        this.templatesDomain = this.configuration.getJsonConfiguration().get("web-domain").getAsString();
        this.maxWeight = this.configuration.getJsonConfiguration().get("max-weight").getAsInt();
        this.serverFolder = new File(MiscUtils.getJarFolder(), "servers");

        try {
            this.restrictionMode = RestrictionMode.valueFrom(configuration.getJsonConfiguration().get("RestrictionMod").getAsString());
            getLogger().info("Server restriction is set to: " + restrictionMode.getMode());

        } catch (Exception e) {
            this.restrictionMode = RestrictionMode.NONE;
            getLogger().warning("Restriction mode not set. Default: NONE");
        }

        try {
            for (JsonElement data : configuration.getJsonConfiguration().get("Whitelist").getAsJsonArray()) {
                String templateID = data.getAsString();

                if (templateID != null) {
                    whitelist.add(templateID);
                    getLogger().info("Adding to whitelist: " + templateID);
                }
            }
        } catch (Exception e) {
            getLogger().info("No whitelist loaded.");
        }
    }

    @Override
    public void onDisable() {


    }

    public UUID getClientUUID() {
        return getUUID();
    }

    public DockerAPI getDockerAPI() {
        return dockerAPI;
    }

    public JsonObject getDockerConfig() {
        return dockerConfig;
    }
}
