package net.algelier.servermanagement.common.database;

import net.algelier.servermanagement.ServerManagement;
import net.algelier.servermanagement.common.packets.PacketReceiver;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;

public class RedisSubscriber extends JedisPubSub {

    private final ServerManagement serverManagement;
    private final HashMap<String, HashSet<PacketReceiver>> packetReceivers;
    private boolean continueLoop;

    public RedisSubscriber(ServerManagement serverManagement) {
        this.serverManagement = serverManagement;
        this.packetReceivers = new HashMap<>();
        this.continueLoop = true;

        new Thread(() -> {
            while (this.continueLoop) {
            Jedis jedis = this.serverManagement.getDatabaseConnector().getJedisPool().getResource();

            try {
                jedis.psubscribe(this, "*");
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.serverManagement.log(Level.INFO, "Disconnected from database");
            jedis.close();
            }
        }).start();

        this.serverManagement.log(Level.INFO, "Subscribing pubsub...");
        while (!this.isSubscribed()) {
            try {
                Thread.sleep(100);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.serverManagement.log(Level.INFO, "Pubsub subscribed!");
    }

    public void disable() {
        this.continueLoop = false;
        this.unsubscribe();
        this.punsubscribe();
    }

    public void registerReceiver(String channel, PacketReceiver receiver) {
        HashSet<PacketReceiver> receivers = this.packetReceivers.get(channel);

        if (receivers == null)
            receivers = new HashSet<>();

        receivers.add(receiver);
        this.subscribe(channel);
        this.packetReceivers.put(channel, receivers);

        this.serverManagement.log(Level.INFO, "Registered receiver '" + receiver.getClass().getSimpleName() + "' on channel '" + channel + "'");
    }

    public void send(String channel, String packet) {
        Jedis jedis = this.serverManagement.getDatabaseConnector().getJedisPool().getResource();
        jedis.publish(channel, packet);
        jedis.close();
    }

    @Override
    public void onMessage(String channel, String message) {
        HashSet<PacketReceiver> receivers = this.packetReceivers.get(channel);

        if (receivers != null) {
            receivers.forEach((PacketReceiver receiver) -> receiver.receive(message));
        }
    }
}
