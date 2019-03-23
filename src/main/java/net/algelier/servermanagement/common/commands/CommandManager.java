package net.algelier.servermanagement.common.commands;

import net.algelier.servermanagement.ServerManagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public abstract class CommandManager {

    public ServerManagement serverManagement;

    public List<AbstractCommand> commands;

    public CommandManager(ServerManagement serverManagement) {
        this.serverManagement = serverManagement;
        commands = new ArrayList<>();
    }

    public void inputCommand(String data) {
        String[] args = data.split(" ");
        String command = args[0];

        args = Arrays.copyOfRange(args, 1, args.length);

        if (command.equals("help")) {
            showHelp();
            return;
        }

        for (AbstractCommand command1 : commands) {

            if (command1.getCommand().equals(command)) {
                if (!command1.execute(args)) {
                    serverManagement.log(Level.WARNING, "Error while executing the command!");
                }
                return;
            }
        }

        serverManagement.log(Level.INFO, "Command doesn't exist.");
    }

    public void showHelp() {
        for (AbstractCommand command : commands) {
            String help = command.getHelp();

            if (help != null) {
                ServerManagement.getLogger().info(help);
            }
        }
    }
}
