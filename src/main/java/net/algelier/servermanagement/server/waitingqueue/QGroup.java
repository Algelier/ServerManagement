package net.algelier.servermanagement.server.waitingqueue;

import net.algelier.servermanagement.common.api.GameConnector;
import net.algelier.servermanagement.server.client.MinecraftServerS;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class QGroup {

    private QPlayer leader;
    private List<QPlayer> players = new ArrayList<>();
    private int priority;

    public QGroup(QPlayer leader) {
        this.leader = leader;
        players.add(leader);

        calculatePriority();
    }

    public QGroup(List<QPlayer> players) {
        this(players.get(0), players);
    }

    public QGroup(QPlayer qPlayer, List<QPlayer> players) {
        this.leader = qPlayer;
        priority = leader.getPriority();
        this.players.addAll(players);

        calculatePriority();
    }


    public void calculatePriority() {
        for (QPlayer qPlayer : players) {
            priority = Math.min(qPlayer.getPriority(), priority);
        }
    }

    public int getPriority() {
        return priority;
    }

    public boolean contains(UUID uuid) {
        for (QPlayer qPlayer : players) {

            if (qPlayer.getUUID().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(QPlayer qPlayer) {
        return players.contains(qPlayer);
    }

    public QPlayer getPlayerByUUID(UUID uuid) {
        for (QPlayer player : players) {

            if (player.getUUID().equals(uuid)) {
                return player;
            }
        }
        return null;
    }

    public boolean addPlayer(QPlayer player) {
        if (contains(player.getUUID()))
            return false;

        try {
            return players.add(player);

        } finally {
            calculatePriority();
        }
    }

    public boolean removePlayer(UUID uuid) {
        return removePlayer(getPlayerByUUID(uuid));
    }

    public boolean removePlayer(QPlayer player) {
        if (leader != null && player.getUUID().equals(leader.getUUID()))
            leader = null;

        try {
            return players.remove(player);

        } finally {
            calculatePriority();
        }
    }

    public List<UUID> getPlayers() {
        return players.stream().map(QPlayer::getUUID).collect(Collectors.toList());
    }

    public List<QPlayer> getQPlayers() {
        return players;
    }

    public int getSize() {
        return players.size();
    }

    public QPlayer getLeader() {
        return leader;
    }

    public void sendTo(String serverName) {
        for (QPlayer player : players) {
            GameConnector.sendPlayerToServer(serverName, player.getUUID());
        }
    }

    public void sendTo(MinecraftServerS serverS) {
        for (QPlayer player : players) {
            serverS.sendPlayer(player.getUUID());
        }
    }
}
