package com.gpdata.wanyou.base.vo;


import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by chengchao on 2016/11/9.
 */
public final class KvPair implements Serializable {


    private Serializable key;
    private Object value;

    public static KvPair of(Serializable name, Object value) {
        return new KvPair( name, value);
    }

    public KvPair(Serializable name, Object value) {
        if (name == null) {
            throw new RuntimeException("key 不可为空");
        }
        this.key = name;
        this.value = value;
    }

    public Serializable getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KvPair)) return false;

        KvPair that = (KvPair) o;

        if (getKey() != null ? !getKey().equals(that.getKey()) : that.getKey() != null) return false;
        return getValue() != null ? getValue().equals(that.getValue()) : that.getValue() == null;

    }

    @Override
    public int hashCode() {
        int result = getKey() != null ? getKey().hashCode() : 0;
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public static void main(String[] args) {
        KvPair nvp = new KvPair(1L, "chengchao");
        KvPair nvp2 = new KvPair(1L, 3);

        String s1 = nvp.toString();
        String s2 = nvp2.toString();

        System.out.println(s1);
        System.out.println(s2);
    }
}
