package net.algelier.servermanagement.common.protocol.net;

import net.algelier.servermanagement.common.packets.AbstractPacket;

import java.util.UUID;

public class AskForClientAction extends AbstractPacket {

    private UUID uuid;
    private ActionCommand command;

    private String data;

    public AskForClientAction() {
    }

    public AskForClientAction(UUID uuid, ActionCommand command, String data) {
        this.uuid = uuid;
        this.command = command;
        this.data = data;
    }

    public UUID getUUID() {
        return uuid;
    }

    public ActionCommand getCommand() {
        return command;
    }

    public String getData() {
        return data;
    }

    public enum ActionCommand {
        SERVEREND,
        CLIENTSHUDOWN,
        CONSOLECOMMAND
    }
}
