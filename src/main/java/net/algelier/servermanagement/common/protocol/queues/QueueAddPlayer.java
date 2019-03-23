package net.algelier.servermanagement.common.protocol.queues;

import net.algelier.servermanagement.server.waitingqueue.QPlayer;

public class QueueAddPlayer extends QueuePacket {

    private QPlayer player;

    public QueueAddPlayer() {
    }

    public QueueAddPlayer(QueueType queueType, String game, String map, QPlayer player) {
        super(queueType, game, map);
        this.player = player;
    }

    public QueueAddPlayer(QueueType queueType, String templateID, QPlayer player) {
        super(queueType, templateID);
        this.player = player;
    }

    public QPlayer getPlayer() {
        return player;
    }
}
