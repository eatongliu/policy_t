package com.gpdata.wanyou.base.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Tuple;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public interface RedisCache extends BaseCache {

    /**
     * Set the string value of a key
     *
     * @param key
     * @param value
     * @param expiration
     */
    public void addString(String key, String value, int expiration);

    /**
     * Get the value of a key
     *
     * @param key
     * @return
     */
    public String getString(String key);

    /**
     * cache a collection of list objects
     *
     * @param key
     * @param list
     */
    void setList(String key, List<String> list);

    /**
     * get a collection of list objects
     *
     * @param key
     * @return
     */
    List<String> getList(String key, int end);


    /**
     * cache a collection of set objects
     *
     * @param key
     * @param list
     */
    void addSet(String key, Set<String> set);

    /**
     * get a collection of set objects
     *
     * @param key
     * @return
     */
    Set<String> getSet(String key);

    /**
     * Set the string value of a hash field
     *
     * @param key
     * @param fieldKey
     * @param fieldValue
     */
    void addHset(String key, String fieldKey, String fieldValue);

    /**
     * get the hash table fields in key fieldKey corresponding value
     *
     * @param key
     * @param fieldKey
     */
    String getHget(String key, String fieldKey);

    /**
     * cache a map
     *
     * @param key
     * @param field
     */
    void addHmset(String key, Map<String, String> field);

    /**
     * Get all the fields and values in a hash
     *
     * @param key
     * @param fieldKey
     * @return
     */
    List<String> getHmget(String key, String[] fieldKey);

    /**
     * Get all the fields and values in a hash
     *
     * @param key
     * @return
     */
    Map<String, String> getHmall(String key);

    /**
     * Delete one or more hash fields
     */
    void hdel(String key, String[] fieldKeys);

    /**
     * Returns a hash table key in all domains
     *
     * @param key
     * @return
     */
    Set<String> hkeys(String key);

    /**
     * Returns a hash table in key domain values
     *
     * @param key
     * @return
     */
    List<String> hvals(String key);

    long hlen(String key);

    /**
     * Set the object value of a hash field
     *
     * @param key
     * @param fieldKey
     * @param obj
     */
    void hset2Object(String key, String fieldKey, Object obj);

    /**
     * Get the value of a hash field
     *
     * @param key
     * @param fieldKey
     * @param clazz
     * @return
     */
    <T> T hget2Object(String key, String fieldKey, Class<T> clazz);

    /**
     * Set multiple hash fields to multiple values
     *
     * @param key
     * @param field
     */
    <T> void addHmset2Object(String key, Map<String, T> field);

    /**
     * Get the values of all the given hash fields
     *
     * @param key
     * @param fieldKey
     * @return
     */
    <T> List<T> getHmget2Object(String key, String[] fieldKey);


    /**
     * according variable parameter key set value
     * not recommended to use
     *
     * @param value
     * @param key
     * @throws IllegalArgumentException
     */
    void add2Hsetnx(String value, String... key) throws IllegalArgumentException;

    /**
     * according variable parameter key get value
     * not recommended to use
     *
     * @param key
     * @return
     */
    String get2Hget(String... key) throws IllegalArgumentException;

    void hincrby(String key, String field, long increment);

    /**
     * Set the value of a key, only if the key does not exist
     *
     * @param key
     * @param value
     * @return
     */
    Long setnx(String key, String value);

    /**
     * Set the value of a key, only if the key does not exist
     *
     * @param key
     * @param value
     * @return
     */
    Long setnx(byte[] key, byte[] value);

    /**
     * Delete a key
     *
     * @param key
     * @return
     */
    Long del(String key);

    /**
     * Set the string value of a key and return its old value
     *
     * @param key
     * @param value
     * @return
     */
    byte[] getset(byte[] key, byte[] value);

    /**
     * Set the string value of a key and return its old value
     *
     * @param key
     * @param value
     * @return
     */
    String getset(String key, String value);

    /**
     * Set a key's time to live in seconds
     *
     * @param key
     * @param seconds
     * @return
     */
    Long expire(String key, int seconds);

    /**
     * Set a key's time to live in seconds
     *
     * @param key
     * @param seconds
     * @return
     */
    void lpush(List<String> list, String key);

    void lpush(String key, String data);

    String rpop(String key);

    Long expire(byte[] key, int seconds);

    long llen(String key);

    <T> void lpush(String key, List<T> list);

    <T> T rpop(String key, Class<T> clazz);

    Set<String> keys(String key);

    ScanResult<Map.Entry<String, String>> hscan(String key, String cursor);

    //===zset
    Set<String> reverseRange(String key, long start, long end);

    long reverseRank(String key, String value);

    Double incrementScore(String key, String value, double score);

    double score(String key, String value);

    void zsetAdd(String key, String value, double score);

    long zsize(String key);

    Set<Tuple> rangeWithScores(String key, long start, long end);

    Jedis getShard(String key);

    Jedis getShard(byte[] key);

    Jedis getShard();

    boolean exists(String key);

    boolean hExists(String key, String field);

    /**
     * Recommend the use of safety interlock
     *
     * @param key
     * @param ttl
     * @param ilock
     * @throws Throwable
     */
    void lock(String key, int ttl, InterLockWorker ilock) throws Throwable;

    void lock(String key);

    void unlock(String key);

    /**
     * 自己关闭连接哦
     *
     * @return
     */
    public ShardedJedis getShardedJedis();

    /**
     * 从 cache 中取回一个对象, 如果没有就放一个进去
     *
     * @param key
     * @param supplier
     * @param timeMillis
     * @return
     */
    public <T extends Serializable> T retrieveOrPut(String key, Supplier<T> supplier, long timeMillis);

    /**
     * 删除缓存中的对象
     *
     * @param key
     * @return
     */
    public Long deleteCache(String key);

    public interface InterLockWorker {
        void call();
    }

}
