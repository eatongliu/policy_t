package com.gpdata.wanyou.base.core;

import java.io.Serializable;
import java.util.Objects;

/**
 * 
 * @author chengchaos
 *
 */
public class KeyValuePair implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final String key;
    private final Object value;
    
    
    public static final KeyValuePair valeOf(String key, Object value) {
        return new KeyValuePair(key, value);
    }
    
    public KeyValuePair(String key, Object value) {
        super();
        Objects.requireNonNull(key);
        this.key = key;
        this.value = value;
    }


    public String getKey() {
        return key;
    }


    public Object getValue() {
        return value;
    }

    
    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();
        buff.append("{\"key\" : \"");
        buff.append(this.getKey());
        buff.append("\", \"value\" : \"");
        buff.append(this.getValue());
        buff.append("\"}");
        return buff.toString();
    }
    
    public static void main(String[] args) {
        KeyValuePair kvp = KeyValuePair.valeOf("name", "chengchao");
        System.out.println(kvp);
    }
    
    
}
