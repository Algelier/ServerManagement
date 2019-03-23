package net.algelier.servermanagement.common.protocol.queues;

import net.algelier.servermanagement.common.packets.AbstractPacket;

public abstract class QueuePacket extends AbstractPacket {

    private String game;
    private String map;

    private String templateID;

    private QueueType queueType;

    public QueuePacket() {
    }

    public QueuePacket(QueueType queueType, String game, String map) {
        this(queueType, game + "_" + map);
        this.game = game;
        this.map = map;
    }

    public QueuePacket(QueueType queueType, String templateID) {
        this.templateID = templateID;
        this.queueType = queueType;
    }

    public QueueType getQueueType() {
        return queueType;
    }

    public String getTemplateID() {
        return templateID;
    }

    public String getMap() {
        return map;
    }

    public String getGame() {
        return game;
    }

    public enum QueueType {
        NAMEDID,
        NAMED,
        RANDOM,
        FAST
    }
}
