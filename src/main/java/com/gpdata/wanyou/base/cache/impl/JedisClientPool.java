package com.gpdata.wanyou.base.cache.impl;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.net.URI;
import java.util.List;
import java.util.Vector;

/**
 *
 *
 */
public class JedisClientPool extends ShardedJedisPool {

    public JedisClientPool(GenericObjectPoolConfig poolConfig, List<JedisShardInfo> shards) {
        super(poolConfig, shards);
    }

    public JedisClientPool(GenericObjectPoolConfig poolConfig, String serverlist) {
        super(poolConfig, toList(serverlist));
    }

    /**
     * @param serverlist
     * @return
     */
    private static List<JedisShardInfo> toList(String serverlist) {
        String[] servers = serverlist.split("[,]");

        List<JedisShardInfo> list = new Vector<JedisShardInfo>(servers.length);
        for (String hostandport : servers) {
            if (hostandport.startsWith("redis://")) {
                try {
                    list.add(new JedisShardInfo(new URI(hostandport)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                String[] strs = hostandport.split(":");
                String host = strs[0];
                int port = 6379;
                if (strs.length > 1) {
                    port = Integer.parseInt(strs[1]);
                }
                list.add(new JedisShardInfo(host, port));
            }
        }
        return list;
    }
}
