package net.algelier.servermanagement;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.algelier.servermanagement.client.ServerManagementClient;
import net.algelier.servermanagement.server.ServerManagementServer;

import java.io.IOError;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {
        OptionParser parser = new OptionParser() {
            {
                acceptsAll(Arrays.asList("?", "help"), "Shows help");
                acceptsAll(Collections.singletonList("client"), "Be the client");
                acceptsAll(Collections.singletonList("server"), "Be the server");

                acceptsAll(Arrays.asList("c", "config"), "Configuration file")
                        .withRequiredArg()
                        .ofType(String.class);

                acceptsAll(Arrays.asList("d", "default"), "Create a default configuration file");
                acceptsAll(Arrays.asList("v", "version"), "Displays version information");
            }
        };

        try {
            OptionSet options = parser.parse(args);

            if (options == null || !options.hasOptions() || options.has("?")) {
                try {
                    parser.printHelpOn(System.out);
                } catch (IOException e) {
                    System.err.println(e.getLocalizedMessage());
                }

                System.exit(0);
                return;
            }

            if (options.has("version")) {
                System.exit(0);
                return;
            }

            if (!options.has("c") && !options.has("d")) {
                System.err.println("You must provide a configuration file");
                System.exit(-1);
            }

            if (!options.has("client") && !options.has("server")) {
                System.err.println("You must start ServerManagement as a client or a server");
                System.exit(6);

            } else if (options.has("client") && options.has("server")) {
                System.err.println("ServerManagement can't be a client AND a server");
                System.exit(7);
            }

            ServerManagement serverManagement;

            if (options.has("server"))
                serverManagement = new ServerManagementServer(options);
            else
                serverManagement = new ServerManagementClient(options);

            while (serverManagement.isRunning) {
                String line = null;
                try {
                    line = serverManagement.getConsoleReader().readLine(">");

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (line != null) {
                    //inputcommand
                }
            }
        } catch (OptionException | IOException e) {
            System.err.println(e.getLocalizedMessage());
            System.exit(42);
        }
    }

}
