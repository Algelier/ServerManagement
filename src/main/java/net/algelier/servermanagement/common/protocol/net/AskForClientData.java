package net.algelier.servermanagement.common.protocol.net;

import net.algelier.servermanagement.common.packets.AbstractPacket;

import java.util.UUID;

public class AskForClientData extends AbstractPacket {

    private UUID uuid;

    public AskForClientData(UUID uuid) {
        this.uuid = uuid;
    }

    public AskForClientData() {
    }

    public UUID getUUID() {
        return uuid;
    }
}
