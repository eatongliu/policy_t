package com.gpdata.wanyou.base.cache.impl;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;


/**
 * 连接和使用数据库资源的工具类
 *
 * @author philn
 */
public class RedisUtil {

    /**
     * 数据??
     */
    private ShardedJedisPool shardedJedisPool;


    /**
     * 获取数据库连??
     *
     * @return conn
     */
    public ShardedJedis getConnection() {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jedis;
    }

    /**
     * 关闭数据库连??
     *
     * @param conn
     */
    public void closeConnection(ShardedJedis jedis) {
        if (null != jedis) {
            try {
                shardedJedisPool.returnResource(jedis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置数据
     *
     * @param conn
     */
    public boolean setData(String key, String value) {
        try {
            ShardedJedis jedis = shardedJedisPool.getResource();
            jedis.set(key, value);
            shardedJedisPool.returnResource(jedis);
            return true;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    /**
     * 获取数据
     *
     * @param conn
     */
    public String getData(String key) {
        String value = null;
        try {
            ShardedJedis jedis = shardedJedisPool.getResource();
            value = jedis.get(key);
            shardedJedisPool.returnResource(jedis);
            return value;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return value;
    }

    /**
     * 获取连接??
     *
     * @return 数据??
     */
    public ShardedJedisPool getShardedJedisPool() {
        return shardedJedisPool;
    }

    /**
     * 设置连接??
     *
     * @return 数据??
     */
    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }
}
