package com.gpdata.wanyou.ds.util;

import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 路径读取工具类
 *
 * @author qyl
 */
@Component
public class PropertiesReader {

    /**
     * properties文件读取类
     *
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException
     */

    @SuppressWarnings("rawtypes")
    public Map<String, String> getPropertiesReader(String configPath) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<String, String>();
        InputStreamReader reader = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(configPath), "utf-8");
        Properties properties = new Properties();//获取Properties实例
        try {
            properties.load(reader);//载入输入流
            Enumeration enumeration = properties.propertyNames();//取得配置文件里所有的key值
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                map.put(key, properties.getProperty(key));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
