package net.algelier.servermanagement.common.data;

import com.google.gson.JsonElement;
import net.algelier.servermanagement.server.data.Status;

import java.util.UUID;

public abstract class MinecraftServer {

    protected UUID uuid;
    protected UUID owner;

    protected boolean friendServer;

    protected String game;
    protected String map;

    protected int minSlot;
    protected int maxSlot;

    protected String templateID;

    protected JsonElement options;
    protected JsonElement startupOptions;

    protected Integer hubID = null;
    protected int weight;
    protected int port;

    protected Status status = Status.STARTING;
    protected int actualSlots;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }


    public String getTemplateID() {
        return templateID;
    }

    public void setTemplateID(String templateID) {
        this.templateID = templateID;
    }

    public String getServerName() {
        return this.game + "_" + ((hubID == null) ? this.uuid.toString().split("-")[0] : hubID);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


}
