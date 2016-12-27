package com.gpdata.wanyou.base.cache.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gpdata.wanyou.base.cache.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * User: liyj12
 * Date: 2015/6/25
 * Time: 13:28
 */
public class RedisClusterImpl implements RedisCache {
    private static final String SUCCESS = "OK";
    private static final int lockTimeout = 30000;
    private static final String LOCK_PREFIX = "lock.";
    private static final String KEY_PREFIX_LOCK = "lock:";
    private static final int LOCK_EXPIRE_TIME = 45;
    private static final long WAIT_TIME = 1000L;
    private static Logger log = LoggerFactory.getLogger(RedisCacheImpl.class);
    private final Map<String, AtomicInteger> keyLocks = new HashMap<String, AtomicInteger>();
    JedisCluster cluster;

    public static void main(String[] args) {
//		ApplicationContext ctx = new ClassPathXmlApplicationContext( "dc-common.xml" );
        /*final RedisCache cache = (RedisCache) ctx.getBean("cache");
        final String prefix = "lock:";
		for (int i = 0; i < 100; i++) {
			final String key = prefix + i;
			for (int j = 0; j < 10; j++) {
				new Thread("thread-" + i + "-" + j) {
					public void run() {
						String threadName = currentThread().getName();
						try {
							cache.lock(key);
							LOGGER.debug(String.format("%s\t%s\tlocked", threadName, key));
							Thread.sleep(40L);
							LOGGER.debug(String.format("%s\t%s\tunlocked", threadName, key));
						} catch (InterruptedException e) {
							return;
						} finally {
							cache.unlock(key);
						}
					}
				}.start();
			}
		}*/
    }

    /**
     * the pool to set
     *
     * @param cluster
     */
    public void setCluster(JedisCluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public void addString(String key, String value, int expiration) {
        if (expiration > 0) {
            cluster.setex(key, expiration, value);
        } else {
            cluster.set(key, value);
        }
    }

    @Override
    public String getString(String key) {
        return cluster.get(key);
    }

    @Override
    public void add(String key, Object value, int expiration) {
        cluster.del(key);
        String str = JSON.toJSONString(value, SerializerFeature.WriteClassName);

        if (expiration <= 0) {
            cluster.set(key, str);
        } else {
            cluster.setex(key, expiration, str);
        }
    }

    @Override
    public Object get(String key) {
        String str = cluster.get(key);
        if (null != str) {
            return JSON.parse(str);
        }
        return null;
    }

    @Override
    public void delete(String key) {
        cluster.del(key);
    }

    @Override
    public Map<String, Object> get(String... keys) {
        Map<String, Object> map = new HashMap<String, Object>(keys.length);
        for (String key : keys) {
            String str = cluster.get(key);
            if (null != str) {
                Object obj = JSON.parse(str);
                map.put(key, obj);
            }
        }
        return map;
    }

    @Override
    public <T> T get(String key, Class<T> value) {
        String str = cluster.get(key);
        if (null != str) {
            return JSON.parseObject(str, value);
        }
        return null;
    }

    @Override
    public long decr(String key, int by, long def) {

        long result = def;
        result = cluster.decrBy(key, by);
        return result;
    }

    @Override
    public synchronized long incr(String key, int by, long def) {
        long result = def;
        String val = cluster.get(key);
        if (val != null) {
            result = cluster.incrBy(key, by);
        } else {
            result = cluster.incrBy(key, by + def);
        }
        return result;
    }

    @Override
    public boolean safeAdd(String key, Object value, int expiration) {
        if (value instanceof Serializable) {
            String str = JSON.toJSONString(value, SerializerFeature.WriteClassName);
            String ret = null;
            if (expiration <= 0) {
                ret = cluster.set(key, str);
            } else {
                ret = cluster.setex(key, expiration, str);
            }
            return SUCCESS.equals(ret);
        }
        return false;
    }

    @Override
    public boolean safeDelete(String key) {

        return cluster.del(key) > 0;

    }

    @Override
    public boolean safeSet(String key, Object value, int arg2) {
        return safeAdd(key, value, arg2);
    }

    @Override
    public void set(String key, Object value, int arg2) {
        safeSet(key, value, arg2);
    }

    @Override
    public void clear() {
        cluster.flushDB();
//        Jedis jedis = getShard();
//        jedis.flushDB();
    }

    @Override
    public long llen(String key) {
        return cluster.llen(key);
    }

    @Override
    public List<String> getList(String key, int end) {
        List<String> list = null;
        if (end <= 0) {
            list = cluster.lrange(key, 0, -1);
        } else {
            list = cluster.lrange(key, 0, end);
        }
        return list;
    }

    @Override
    public void setList(String key, List<String> list) {
        for (String l : list) {
            cluster.lpush(key, l);
        }
    }

    @Override
    public void lpush(String key, String data) {

        cluster.lpush(key, data);

    }

    @Override
    public void lpush(List<String> list, String key) {
        for (String l : list) {
            cluster.lpush(key, l);
        }
    }

    @Override
    public String rpop(String key) {

        try {
            while (true) {
                if (!cluster.exists(key)) {
                    synchronized (this) {
                        wait(15);
                        continue;
                    }
                }
                return cluster.rpop(key);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public <T> void lpush(String key, List<T> list) {

        for (T l : list) {
            cluster.lpush("OBJ:" + key, JSON.toJSONString(l, SerializerFeature.WriteClassName));
        }


    }

    @Override
    public <T> T rpop(String key, Class<T> clazz) {

        String k = "OBJ:" + key;

        try {
            while (true) {
                if (!cluster.exists(k) || null == cluster.rpop(k)) {
                    synchronized (this) {
                        wait(200);
                        continue;
                    }
                }
                return JSON.parseObject(cluster.rpop(k), clazz);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public void addSet(String key, Set<String> set) {
        for (String l : set) {
            cluster.sadd(key, l);
        }
    }

    @Override
    public Set<String> getSet(String key) {
        return cluster.smembers(key);
    }

    @Override
    public void addHset(String key, String fieldKey, String fieldValue) {

        cluster.hset(key, fieldKey, fieldValue);

    }

    @Override
    public String getHget(String key, String fieldKey) {

        return cluster.hget(key, fieldKey);
    }

    @Override
    public void addHmset(String key, Map<String, String> field) {

        cluster.hmset(key, field);
    }

    @Override
    public List<String> getHmget(String key, String[] fieldKey) {

        return cluster.hmget(key, fieldKey);
    }

    @Override
    public long hlen(String key) {
        return cluster.hlen(key);
    }

    @Override
    public void hset2Object(String key, String fieldKey, Object obj) {

        if (obj instanceof Serializable) {
            String str = JSON.toJSONString(obj, SerializerFeature.WriteClassName);
            cluster.hset(key, fieldKey, str);
        }
    }

    @Override
    public <T> T hget2Object(String key, String fieldKey, Class<T> clazz) {

        String bs = cluster.hget(key, fieldKey);
        if (null != bs) {
            Object obj = JSON.parse(bs);
            return clazz.isInstance(obj) ? (T) obj : null;
        }
        return null;
    }

    @Override
    public <T> void addHmset2Object(String key, Map<String, T> field) {
        Iterator<Map.Entry<String, T>> iter = field.entrySet().iterator();
        Map<String, String> hash = new HashMap<>();
        while (iter.hasNext()) {
            Map.Entry<String, T> entry = (Map.Entry<String, T>) iter.next();
            String k = (String) entry.getKey();
            Object obj = entry.getValue();
            String str = JSON.toJSONString(obj, SerializerFeature.WriteClassName);
            hash.put(k, str);
        }
        cluster.hmset(key, hash);
    }

    @Override
    public <T> List<T> getHmget2Object(String key, String[] fieldKey) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Map<String, String> getHmall(String key) {
        return cluster.hgetAll(key);
    }

    @Override
    public void hdel(String key, String[] fieldKeys) {

        for (String fkey : fieldKeys) {
            cluster.hdel(key, fkey);
        }
    }

    @Override
    public Set<String> hkeys(String key) {
        return cluster.hkeys(key);

    }

    @Override
    public List<String> hvals(String key) {
        return cluster.hvals(key);
    }

    @Override
    public void add2Hsetnx(String value, String... key) throws IllegalArgumentException {
        String[] ks = findKey(key);
        cluster.hset(ks[0], ks[1], value);
    }

    private String[] findKey(String... key) {
        if (key.length == 2) {
            return key;
        } else if (key.length > 2) {
            String[] k = new String[key.length - 1];
            for (int i = 0; i < key.length - 1; i++) {
                k[i] = key[i];
            }
            return new String[]{findIndex(k), key[key.length - 1]};
        }

        return null;
    }

    private String findIndex(String[] k) {
        if (k.length == 1)
            return k[0];
        else {
            String index = cluster.hget(k[0], k[1]);
            if (null == index)
                throw new IllegalArgumentException("key is illegal argument");
            String[] tmp = new String[k.length - 1];
            tmp[0] = index;
            for (int i = 1; i < k.length - 1; i++) {
                tmp[i] = k[i + 1];
            }
            return findIndex(tmp);
        }

    }

    @Override
    public String get2Hget(String... key) throws IllegalArgumentException {
        String[] ks = findKey(key);
        return cluster.hget(ks[0], ks[1]);
    }

    @Override
    public void hincrby(String key, String field, long increment) {
        cluster.hincrBy(key, field, increment);
    }

    @Override
    public Long setnx(String key, String value) {
        return cluster.setnx(key, value);
    }

    @Override
    public Long setnx(byte[] key, byte[] value) {
        throw new IllegalArgumentException("Not supported");
//        return cluster.setnx(key, value);

    }

    @Override
    public Long del(String key) {
        return cluster.del(key);
    }

    @Override
    public byte[] getset(byte[] key, byte[] value) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public String getset(String key, String value) {
        return cluster.getSet(key, value);
    }

    @Override
    public Long expire(String key, int seconds) {
        return cluster.expire(key, seconds);
    }

    @Override
    public Long expire(byte[] key, int seconds) {
        throw new IllegalArgumentException("Not supported");
    }


    //================zset

    @Override
    public Jedis getShard(String key) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Jedis getShard(byte[] key) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Jedis getShard() {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor) {
        //ScanParams.SCAN_POINTER_START
        return cluster.hscan(key, cursor);
//
    }

    @Override
    public Set<String> keys(String key) {
        Map<String, JedisPool> map = cluster.getClusterNodes();
        Set mergedKeys = new HashSet();
        for (JedisPool jedisPool : map.values()) {
            Jedis resource = null;
            try {
                resource = jedisPool.getResource();
                Set<String> keys = resource.keys(key);
                mergedKeys.addAll(keys);
            } finally {
                if (resource != null) {
                    resource.close();
                }
            }
        }
        return mergedKeys;
    }

    @Override
    public boolean hExists(String key, String field) {
        return cluster.hexists(key, field);
    }

    @Override
    public boolean exists(String key) {
        return cluster.exists(key);
    }

    @Override
    public Set<String> reverseRange(String key, long start, long end) {
        return cluster.zrevrange(key, start, end);
    }

    @Override
    public long reverseRank(String key, String value) {
        return cluster.zrevrank(key, value);
    }

    @Override
    public Double incrementScore(String key, String value, double score) {
        return cluster.zincrby(key, score, value);
    }

    @Override
    public double score(String key, String value) {
        return cluster.zscore(key, value);
    }

    @Override
    public void zsetAdd(String key, String value, double score) {
        cluster.zadd(key, score, value);
    }

    @Override
    public long zsize(String key) {
        return cluster.zcard(key);
    }

    @Override
    public Set<Tuple> rangeWithScores(String key, long start, long end) {
        return cluster.zrangeWithScores(key, start, end);
    }

    /**
     * random_key = getrandomkey()
     * value = redis.setnx(key, random_key)
     * if value == 1:
     * redis.expire(key, timeout)
     * do_job()
     * redis.watch(mykey)
     * value = redis.get(mykey)
     * if random_key == value:
     * redis.multi()
     * redis.del(mykey)
     * redis.exec
     * else:
     * # not the lock itself, so can not be removed
     * else:
     * # don't get lock
     *
     * @param key
     * @param ttl
     * @param ilock
     * @throws Throwable
     */

    public void lock(String key, int ttl, InterLockWorker ilock) throws Throwable {
        throw new IllegalArgumentException("Not supported");
    }

    private Object getKeyLock(String key) {
        AtomicInteger result;
        synchronized (keyLocks) {
            result = keyLocks.get(key);
            if (result == null) {
                keyLocks.put(key, result = new AtomicInteger());
            }
            result.incrementAndGet();
        }
        return result;
    }

    private Object getAndReleaseKeyLock(String key) {
        AtomicInteger result;
        synchronized (keyLocks) {
            result = keyLocks.get(key);
            if (result.decrementAndGet() == 0) {
                keyLocks.remove(key);
            }
        }
        return result;
    }

    public void lock(String key) {
        throw new IllegalArgumentException("Not supported");
    }

    public void unlock(String key) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public ShardedJedis getShardedJedis() {
        throw new UnsupportedOperationException("方法未实现");
    }

    /**
     *
     */
    @Override
    public <T extends Serializable> T retrieveOrPut(String key, Supplier<T> supplier, long timeMillis) {
        throw new UnsupportedOperationException("方法未实现");
    }

    /**
     *
     */
    @Override
    public Long deleteCache(String key) {
        throw new UnsupportedOperationException("方法未实现");
    }


}
