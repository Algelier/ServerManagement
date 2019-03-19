package net.algelier.servermanagement.common.log;

import net.algelier.servermanagement.ServerManagement;

import java.io.IOException;
import java.util.logging.Formatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ServerManagementLogger extends Logger  {

    private final Formatter formatter = new ConciseFormatter();
    private final LogDispatcher dispatcher = new LogDispatcher(this);

    @SuppressWarnings(
            {
                    "CallToPrintStackTrace", "CallToThreadStartDuringObjectConstruction"
            })


    public ServerManagementLogger(ServerManagement serverManagement) {
        super("ServerManagement", null);
        setLevel(Level.ALL);

        try {
            FileHandler fileHandler = new FileHandler("ServerManagement.log", 1 << 24, 8, true);
            fileHandler.setFormatter(formatter);
            addHandler(fileHandler);

            ColouredWriter writer = new ColouredWriter(serverManagement.getConsoleReader());
            writer.setLevel(Level.INFO);
            writer.setFormatter(formatter);
            addHandler(writer);

        } catch (IOException e) {
            System.err.println("Could not register logger!");
            e.printStackTrace();
        }

    }

    @Override
    public void log(LogRecord record) {
        dispatcher.queue(record);
    }

    void doLog(LogRecord record) {
        super.log(record);
    }
}
