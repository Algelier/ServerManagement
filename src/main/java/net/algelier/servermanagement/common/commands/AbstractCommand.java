package net.algelier.servermanagement.common.commands;

public abstract class AbstractCommand {

    protected String command;

    public AbstractCommand(String command) {
        this.command = command;
    }

    public abstract boolean execute(String[] args);

    public String getCommand() {
        return command;
    }

    public abstract String getHelp();
}
