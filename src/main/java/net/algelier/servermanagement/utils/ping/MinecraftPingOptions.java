package net.algelier.servermanagement.utils.ping;

public class MinecraftPingOptions {

    private String hostName;
    private int port = 25565;
    private int timeout = 2000;

    private String charset = "UTF-8";

    public MinecraftPingOptions setHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public MinecraftPingOptions setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public MinecraftPingOptions setCharset(String charset) {
        this.charset = charset;
        return this;
    }
}
