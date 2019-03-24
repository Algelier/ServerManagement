package net.algelier.servermanagement.common.protocol.net;

import net.algelier.servermanagement.common.packets.AbstractPacket;

import java.util.UUID;

public class ByeFromClient extends AbstractPacket {

    private UUID uuid;

    public ByeFromClient(UUID uuid) {
        this.uuid = uuid;
    }

    public ByeFromClient() {
    }

    public UUID getUUID() {
        return uuid;
    }
}
