package net.algelier.servermanagement.client;

import com.google.gson.JsonObject;
import joptsimple.OptionSet;
import net.algelier.servermanagement.ServerManagement;
import net.algelier.servermanagement.client.docker.DockerAPI;

import java.io.IOException;

public class ServerManagementClient extends ServerManagement {

    private DockerAPI dockerAPI;
    private JsonObject dockerConfig;

    public ServerManagementClient(OptionSet option) throws IOException {
        super(option);
        dockerAPI = new DockerAPI();
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
    }

    public DockerAPI getDockerAPI() {
        return dockerAPI;
    }

    public JsonObject getDockerConfig() {
        return dockerConfig;
    }
}
