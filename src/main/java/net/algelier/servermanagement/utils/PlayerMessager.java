package net.algelier.servermanagement.utils;

import net.algelier.servermanagement.ServerManagement;

import java.util.UUID;

public class PlayerMessager {

    public static void sendMessage(UUID player, String message) {
        ServerManagement.getInstance().getRedisSubscriber().send("apiexec.send", player
                + " {\"text\":\"" + message + "\"}");
    }
}
