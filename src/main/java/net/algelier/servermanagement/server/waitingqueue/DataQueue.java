package net.algelier.servermanagement.server.waitingqueue;

import net.algelier.servermanagement.ServerManagement;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DataQueue {

    private final static double NumberOfMinutePerServer = 10.0;
    private ServerManagement serverManagement;
    private AtomicInteger numberOfServerStarted = new AtomicInteger(0);
    private AtomicInteger lastServerStartNB = new AtomicInteger(0);

    public DataQueue(ServerManagement serverManagement) {
        this.serverManagement = serverManagement;
    }

    public void startServer() {
        numberOfServerStarted.incrementAndGet();

        serverManagement.getScheduledExecutorService().schedule(() -> numberOfServerStarted.decrementAndGet(), 10L, TimeUnit.MINUTES);
    }

    public boolean anticipate() {
        return ((((double) numberOfServerStarted.get()) > 0)
                ? 10.0 / ((double) numberOfServerStarted.get())
                : Integer.MAX_VALUE) < NumberOfMinutePerServer;
    }

    public int getLastServerStartNB() {
        return lastServerStartNB.get();
    }

    public void setLastServerStartNB(int lastServerStartNB) {
        this.lastServerStartNB.lazySet(lastServerStartNB);
    }
}
