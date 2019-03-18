package net.algelier.servermanagement.common.packets;

public class CommandPacket extends AbstractPacket {

    private String sourceUUID;
    private String action;

    public CommandPacket() {
    }

    public CommandPacket(String sourceUUID, String action) {
        this.sourceUUID = sourceUUID;
        this.action = action;
    }

    public String getSourceUUID() {
        return sourceUUID;
    }

    public String getAction() {
        return action;
    }
}
