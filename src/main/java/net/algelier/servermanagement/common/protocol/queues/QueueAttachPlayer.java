package net.algelier.servermanagement.common.protocol.queues;

import net.algelier.servermanagement.server.waitingqueue.QPlayer;

import java.util.List;

public class QueueAttachPlayer extends QueuePacket {

    private QPlayer leader;
    private List<QPlayer> players;

    public QueueAttachPlayer() {
    }

    public QueueAttachPlayer(QPlayer leader, List<QPlayer> players) {
        this.leader = leader;
        this.players = players;
    }

    public QPlayer getLeader() {
        return leader;
    }

    public List<QPlayer> getPlayers() {
        return players;
    }
}
