package net.algelier.servermanagement.server.data;

public enum Status {

    STARTING("starting", false),
    WAITING_FOR_PLAYERS("waitingForPlayers", true),
    READY_TO_START("readyToStart", true),
    IN_GAME("inGame", false),
    FINISHED("finished", false),
    REBOOTING("rebooting", false),
    NOT_RESPONDING("idle", false);

    private final String id;
    private final boolean allowJoin;


    Status(String id, boolean allowJoin) {
        this.id = id;
        this.allowJoin = allowJoin;
    }

    public static Status fromString(String str) {
        for (Status status : Status.values())
            if (status.getId().equals(str))
                return status;

        return null;
    }

    public String getId() {
        return id;
    }

    public boolean isAllowJoin() {
        return allowJoin;
    }

    @Override
    public String toString() {
        return getId();
    }
}
