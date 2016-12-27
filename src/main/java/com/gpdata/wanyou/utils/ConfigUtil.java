package com.gpdata.wanyou.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.*;

/**
 * Created by chengchao on 16-10-13.
 */
public class ConfigUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUtil.class);

    private static Map<String, String> config;


    static {

        config = new HashMap<String, String>();

        ResourceBundle resourceBundle = ResourceBundle.getBundle("config");

        Enumeration<String> keys = resourceBundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = resourceBundle.getString(key);
            LOGGER.debug("读取配置信息 : {} ==> {}", key, value);
            config.put(key, value);
        }

    }

    public static final String getConfig(String key) {

        String value = config.get(key);
        return (value == null) ? "" : value;
    }
    public static final String getConfig(String key,Object params[]) {
        /*例如String str = "I'm not a {0}, age is {1,number,short}", height is {2,number,#.#};  参数格式可以不定义*/
        String value = MessageFormat.format(config.get(key), params);
        return (value == null) ? "" : value;
    }

    public static final void setConfig(String key, String value) {
        config.put(key, value);
    }


    public static final Set<String> getConfigKeySet() {
        return config.keySet();
    }


}
