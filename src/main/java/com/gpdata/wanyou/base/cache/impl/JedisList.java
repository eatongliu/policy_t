package com.gpdata.wanyou.base.cache.impl;

import org.apache.commons.lang3.SerializationUtils;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Jedis List
 * <pre>
 *      JedisList list = new JedisList(pool, "name");
 *      list.add(new Object());
 *      list.close();
 * </pre>
 *
 * @param <T>
 * @author philn
 * @see
 * @since 1.0
 */
public class JedisList<T extends Serializable> {
    private ShardedJedisPool pool;
    private ShardedJedis resource;
    private byte[] name;

    public JedisList(ShardedJedisPool pool, String name) {
        super();
        this.pool = pool;
        this.name = name.getBytes();
        resource = pool.getResource();
    }

    public void close() {
        pool.close();
    }

    public boolean add(T e) {
        byte[] bs = SerializationUtils.serialize((Serializable) e);
        Long len = resource.llen(name);
        return (resource.lpush(name, bs) != len + 1);
    }

    public boolean addAll(Collection<? extends T> c) {
        for (T t : c) {
            add(t);
        }
        return true;
    }

    public void clear() {
        resource.del(new String(name));
    }

    public boolean isEmpty() {
        return resource.llen(name) == 0;
    }

    @SuppressWarnings("unchecked")
    public List<T> range(int start, int count) {
        List<byte[]> srcs = resource.lrange(name, start, start + count);
        List<T> list = new ArrayList<T>(srcs.size());
        for (byte[] bs : srcs) {
            list.add((T) SerializationUtils.deserialize(bs));
        }
        return list;
    }


    public boolean remove(Object o) {
        byte[] bs = SerializationUtils.serialize((Serializable) o);
        return resource.lrem(name, 0, bs) > 0;
    }

    public boolean removeAll(Collection<?> c) {
        for (Object object : c) {
            remove(object);
        }
        return true;
    }

    public int size() {
        Long llen = resource.llen(name);
        return llen.intValue();
    }
}
