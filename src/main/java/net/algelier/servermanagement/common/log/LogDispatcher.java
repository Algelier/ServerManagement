package net.algelier.servermanagement.common.log;

import net.algelier.servermanagement.ServerManagement;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.LogRecord;

public class LogDispatcher extends Thread {

    private final ServerManagementLogger logger;
    private final BlockingQueue<LogRecord> queue = new LinkedBlockingQueue<>();

    public LogDispatcher(ServerManagementLogger logger) {
        super("Hydroangeas Logger Thread");
        this.logger = logger;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            LogRecord record;
            try {
                record = queue.take();
            } catch (InterruptedException ex) {
                continue;
            }

            logger.doLog(record);
        }
        queue.forEach(logger::doLog);
    }

    public void queue(LogRecord record) {
        if (!isInterrupted()) {
            queue.add(record);
        }
    }
}
