package net.algelier.servermanagement.server.client;

import net.algelier.servermanagement.client.ServerManagementClient;
import net.algelier.servermanagement.common.api.GameConnector;
import net.algelier.servermanagement.common.data.MinecraftServer;

import java.util.List;
import java.util.UUID;

public class MinecraftServerS extends MinecraftServer {

    private boolean started;
    private boolean available;

    private List<Runnable> hooks;

    private ServerManagementClient client;

    public void sendPlayer(UUID uuid) {
        GameConnector.sendPlayerToServer(getServerName(), uuid);
    }
}
