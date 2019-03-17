package net.algelier.servermanagement.utils.ping;

public class MinecraftPingReply {

    private String description;
    private Players players;
    private Version version;
    private String favicon;

    public String getDescription() {
        return description;
    }

    public Players getPlayers() {
        return players;
    }

    public Version getVersion() {
        return version;
    }

    public String getFavicon() {
        return favicon;
    }

    public class Players {
        private String name;
        private String id;

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }
    }

    public class Version {
        private String name;
        private int protocol;

        public String getName() {
            return name;
        }

        public int getProtocol() {
            return protocol;
        }
    }
}
