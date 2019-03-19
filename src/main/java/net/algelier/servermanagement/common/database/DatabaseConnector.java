package net.algelier.servermanagement.common.database;

import net.algelier.servermanagement.ServerManagement;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnector {

    private final ServerManagement serverManagement;
    public Thread reconnection;
    private JedisPool jedisPool;

    public DatabaseConnector(ServerManagement serverManagement) {
        this.serverManagement = serverManagement;
        this.connect();

        this.reconnection = new Thread(() -> {
            while (true) {
                try {
                    try {
                        jedisPool.getResource().close();

                    } catch (Exception e) {
                        e.printStackTrace();
                        serverManagement.getLogger().severe("Error redis connection, try to reconnect!");
                        connect();
                    }

                    Thread.sleep(10 * 1000);
                } catch (Exception e) {
                    break;
                }
            }
        }, "Redis reconnect");
        reconnection.start();
    }

    public void connect() {
        this.serverManagement.log(Level.INFO, "Connecting to database...");

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(-1);
        jedisPoolConfig.setJmxEnabled(false);

        Logger logger = Logger.getLogger(JedisPool.class.getName());
        logger.setLevel(Level.OFF);

        this.jedisPool = new JedisPool(jedisPoolConfig, this.serverManagement.getConfiguration().redisIp, this.serverManagement.getConfiguration().redisPort,
                0, this.serverManagement.getConfiguration().redisPassword);

        try {
            this.jedisPool.getResource().close();
        } catch (Exception e) {
            this.serverManagement.log(Level.SEVERE, "Can't connect to database!");
            System.exit(8);
        }

        this.serverManagement.log(Level.INFO, "Connected to database.");
    }

    public void disconnect() {
        reconnection.interrupt();
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public Jedis getResource() {
        return jedisPool.getResource();
    }
}
