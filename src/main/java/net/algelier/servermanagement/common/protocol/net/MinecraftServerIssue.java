package net.algelier.servermanagement.common.protocol.net;

import net.algelier.servermanagement.common.packets.AbstractPacket;

import java.util.UUID;

public class MinecraftServerIssue extends AbstractPacket {

    private Type issueType;
    private String message;
    private UUID uuid;
    private String serverName;

    public MinecraftServerIssue(Type issueType, UUID uuid, String serverName) {
        this.issueType = issueType;
        this.uuid = uuid;
        this.serverName = serverName;

        switch (issueType) {
            case MAKE:
                this.message = "Unable to create server '" + serverName + "'!";
                break;

            case PATCH:
                this.message = "Unable to patch server '" + serverName + "'!";
                break;

            case START:
                this.message = "Unable to start server '" + serverName + "'!";
                break;

            case STOP:
                this.message = "Unable to stop server '" + serverName + "'!";
                break;

            default:
                this.message = "An error occurred on server '" + serverName + "'!";
                break;
        }
    }

    public MinecraftServerIssue() {
    }

    public Type getIssueType() {
        return issueType;
    }

    public String getMessage() {
        return message;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getServerName() {
        return serverName;
    }

    public enum Type {
        MAKE,
        PATCH,
        START,
        STOP;
    }
}
