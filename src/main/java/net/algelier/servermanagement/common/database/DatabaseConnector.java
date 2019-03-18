package net.algelier.servermanagement.common.database;

import net.algelier.servermanagement.ServerManagement;
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

    }

    public void connect() {
        this.serverManagement.log(Level.INFO, "Connecting to database...");

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(-1);
        jedisPoolConfig.setJmxEnabled(false);

        Logger logger = Logger.getLogger(JedisPool.class.getName());
        logger.setLevel(Level.OFF);


    }
}
