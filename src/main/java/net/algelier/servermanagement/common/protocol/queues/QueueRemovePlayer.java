package net.algelier.servermanagement.common.protocol.queues;

import net.algelier.servermanagement.server.waitingqueue.QPlayer;

public class QueueRemovePlayer extends QueuePacket {

    private QPlayer player;

    public QueueRemovePlayer() {
    }

    public QueueRemovePlayer(QPlayer player) {
        this.player = player;
    }

    public QPlayer getPlayer() {
        return player;
    }
}
