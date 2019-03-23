package net.algelier.servermanagement.common.protocol.net;

import net.algelier.servermanagement.common.packets.AbstractPacket;

import java.util.UUID;

public class HeartbeatPacket extends AbstractPacket {

    private UUID uuid;
    private boolean online = true;

    public HeartbeatPacket(UUID uuid) {
        this.uuid = uuid;
    }

    public HeartbeatPacket() {
    }

    public UUID getUuid() {
        return uuid;
    }
}
