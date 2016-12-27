package com.gpdata.wanyou.base.cache;

import java.util.Map;

public interface BaseCache {

    void add(String key, Object value, int expiration);

    void set(String key, Object value, int expiration);

    /**
     * Increment the integer value of a key by one
     *
     * @param key
     * @param by
     * @param def
     * @return
     */
    long incr(String key, int by, long def);

    /**
     * Decrement the integer value of a key by one
     *
     * @param key
     * @param by
     * @param def
     * @return
     */
    long decr(String key, int by, long def);

    Object get(String key);

    Map<String, Object> get(String... key);

    void delete(String key);

    /**
     * Remove all keys from the current database
     */
    void clear();

    <T> T get(String key, Class<T> clazz);

    boolean safeAdd(String key, Object value, int expiration);

    boolean safeDelete(String key);

    boolean safeSet(String key, Object value, int expiration);
}
