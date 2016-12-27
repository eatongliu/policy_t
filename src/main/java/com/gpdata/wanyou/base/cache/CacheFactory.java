package com.gpdata.wanyou.base.cache;


import com.gpdata.wanyou.base.cache.impl.JedisClientPool;
import com.gpdata.wanyou.base.cache.impl.RedisCacheImpl;
import com.gpdata.wanyou.base.cache.impl.RedisClusterImpl;
import com.gpdata.wanyou.utils.ConfigUtil;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * User: liyj12
 * Date: 2015/2/6
 * Time: 16:00
 */
public class CacheFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheFactory.class);

    static RedisCache redisCache;

    public static RedisCache getInstances() {

        LOGGER.debug("CacheFactory.getInstances ... ");
        String noCluster = ConfigUtil.getConfig("redis.no.cluster");
        LOGGER.debug("noCluster : {}", noCluster);

        if (noCluster != null && noCluster.equals("true")) {
            return getInstancesFromProperties();
        } else {
            return getClusterInstancesFromProperties();
        }
    }

    /**
     * 从属性文件中获取 Redis
     * <p>
     * 属性文件中包括：<br />
     * redis.write.pool.maxIdle <br />
     * redis.pool.testOnBorrow <br />
     * redis.write.pool <br />
     * 还可以有 <br />
     * redis.read.pool <br />
     * redis.read.pool.maxIdle <br />
     * redis.pool.testOnBorrow <br />
     * redis.read.pool <br />
     *
     * @return
     */
    public synchronized static RedisCache getInstancesFromProperties() {
        if (CacheFactory.redisCache == null) {
            try {
                RedisCacheImpl redisCache = new RedisCacheImpl();
                GenericObjectPoolConfig readPoolConfig = new GenericObjectPoolConfig();
                JedisClientPool readPool = null;

                GenericObjectPoolConfig writePoolConfig = new GenericObjectPoolConfig();
                writePoolConfig.setMaxIdle(Integer.valueOf(ConfigUtil.getConfig("redis.write.pool.maxIdle")));
                writePoolConfig.setTestOnBorrow(Boolean.valueOf(ConfigUtil.getConfig("redis.pool.testOnBorrow")));
                JedisClientPool writePool = new JedisClientPool(writePoolConfig, ConfigUtil.getConfig("redis.write.pool"));

                if (ConfigUtil.getConfig("redis.read.pool") != null && !"".equals(ConfigUtil.getConfig("redis.read.pool"))) {
                    readPoolConfig.setMaxIdle(Integer.valueOf(ConfigUtil.getConfig("redis.read.pool.maxIdle")));
                    readPoolConfig.setTestOnBorrow(Boolean.valueOf(ConfigUtil.getConfig("redis.pool.testOnBorrow")));
                    readPool = new JedisClientPool(readPoolConfig, ConfigUtil.getConfig("redis.read.pool"));
                } else {
                    readPool = writePool;
                }

                LOGGER.debug("redis.read.pool.maxIdle : {}", ConfigUtil.getConfig("redis.read.pool.maxIdle"));

                redisCache.setPool(writePool);
                redisCache.setReadPool(readPool);
                CacheFactory.redisCache = redisCache;
                return redisCache;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return redisCache;
        }
    }

    /**
     * @return
     */
    public synchronized static RedisCache getClusterInstancesFromProperties() {
        if (CacheFactory.redisCache == null) {
            try {
                Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
                String nodesstr = ConfigUtil.getConfig("redis.write.pool");
                String[] nodes = nodesstr.split("[,]");
                for (String nodestr : nodes) {
                    String node[] = nodestr.split(":");
                    jedisClusterNodes.add(new HostAndPort(node[0], Integer.parseInt(node[1])));
                }
                RedisClusterImpl redisCache = new RedisClusterImpl();
                GenericObjectPoolConfig writePoolConfig = new GenericObjectPoolConfig();
                writePoolConfig.setMaxIdle(Integer.valueOf(ConfigUtil.getConfig("redis.write.pool.maxIdle")));
                writePoolConfig.setTestOnBorrow(Boolean.valueOf(ConfigUtil.getConfig("redis.pool.testOnBorrow")));
                JedisCluster jc = new JedisCluster(jedisClusterNodes, 5000, 1000, writePoolConfig);
                redisCache.setCluster(jc);
                LOGGER.debug(ConfigUtil.getConfig("redis.write.pool.maxIdle") + "-----");
                CacheFactory.redisCache = redisCache;
                return redisCache;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return redisCache;
        }
    }

    public static void main(String[] args) {
        CacheFactory.getInstancesFromProperties();
    }
}
