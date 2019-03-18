package net.algelier.servermanagement.common.packets;

public class AbstractPacket {

    private final Runnable callback;

    public AbstractPacket(Runnable callback) {
        this.callback = callback;
    }

    public AbstractPacket() {
        this.callback = null;
    }

    public void callback()  {
        try {
            if (this.callback != null)
                this.callback.run();

        } catch (Exception ignored) {}
    }
}
