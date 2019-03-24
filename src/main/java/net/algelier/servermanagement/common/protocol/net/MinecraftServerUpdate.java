package net.algelier.servermanagement.common.protocol.net;

import net.algelier.servermanagement.client.ServerManagementClient;
import net.algelier.servermanagement.common.packets.AbstractPacket;

import java.util.UUID;

public class MinecraftServerUpdate extends AbstractPacket {

    private UType action;

    private UUID uuid;
    private String serverName;

    private int maxWeight;
    private int newWeight;

    public MinecraftServerUpdate(ServerManagementClient client, String serverName, UType action) {
        this.uuid = client.getClientUUID();
    }

    public MinecraftServerUpdate() {
    }

    private enum UType {
        START,
        END
    }
}
