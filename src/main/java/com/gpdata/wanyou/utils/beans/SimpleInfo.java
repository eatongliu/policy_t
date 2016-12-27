package com.gpdata.wanyou.utils.beans;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chengchao on 16-10-14.
 */
public class SimpleInfo implements Serializable {

    private Map<String, Object> map = new HashMap<>();

    /**
     * 构造方法两个
     */
    public SimpleInfo() {
        super();
    }

    public SimpleInfo(String key, Object value) {

        this.map.put(key, value);
    }

    public static String errorOf(String errorInfo) {
        return new SimpleInfo("error", errorInfo).toString();
    }

    public static String idOf(Object id) {
        return new SimpleInfo("id", id).toString();

    }

    public static void main(String[] args) {
        String s = SimpleInfo.idOf(10);
        System.out.println(s);

        String e = SimpleInfo.errorOf("kao");


        System.out.println(e);
        SimpleInfo si = new SimpleInfo("k", "v");
        System.out.println(JSONObject.toJSONString(si));
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this.map);
    }
}
