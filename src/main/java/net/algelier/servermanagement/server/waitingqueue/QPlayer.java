package net.algelier.servermanagement.server.waitingqueue;

import java.util.UUID;

public class QPlayer {

    private UUID uuid;
    private int priority;

    public QPlayer(UUID uuid, int priority) {
        this.uuid = uuid;
        this.priority = priority;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
