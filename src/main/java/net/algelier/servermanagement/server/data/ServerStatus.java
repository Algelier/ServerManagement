package net.algelier.servermanagement.server.data;

public class ServerStatus {

    private String bungeeName;
    private String game;
    private String map;
    private Status status;
    private int players;
    private int maxPlayers;

    public ServerStatus(String bungeeName, String game, String map, Status status, int players, int maxPlayers) {
        this.bungeeName = bungeeName;
        this.game = game;
        this.map = map;
        this.status = status;
        this.players = players;
        this.maxPlayers = maxPlayers;
    }

    public String getBungeeName() {
        return bungeeName;
    }

    public void setBungeeName(String bungeeName) {
        this.bungeeName = bungeeName;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
}
