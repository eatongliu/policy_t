package com.gpdata.wanyou.sp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gpdata.wanyou.sp.entity.IPStrategy;
import com.gpdata.wanyou.sp.service.IPStrategyService;
import com.gpdata.wanyou.sp.util.XMLBuilder;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

/**
 * IP策略
 * Created by guoxy on 2016/10/14.
 */
@Service
public class IPStrategyServiceImpl implements IPStrategyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IPStrategyServiceImpl.class);
    /*TODO*/
    /*文件路径待确定*/
    public static String WRITE_XMLFILE_PATH;
    public static String READ_XMLFILE_PATH;
    public static String IP_HOST;
    public static String IP_PORT;
    public static String IP_USERNAME;
    public static String IP_PASSWORD;

    static {
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("config.properties");
        Properties p = new Properties();
        try {
            p.load(is);
            WRITE_XMLFILE_PATH = p.getProperty("ipstgy.writepath");
            READ_XMLFILE_PATH = p.getProperty("ipstgy.readpath");
            IP_HOST = p.getProperty("ipstgy.host");
            IP_PORT = p.getProperty("ipstgy.port");
            IP_USERNAME = p.getProperty("ipstgy.username");
            IP_PASSWORD = p.getProperty("ipstgy.password");
            LOGGER.debug("IPStrategy:{}", WRITE_XMLFILE_PATH + ";" + READ_XMLFILE_PATH + ";" + IP_HOST + ";" + IP_PORT + ";" + IP_USERNAME + ";" + IP_PASSWORD);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 4.3.3.1浏览 /sp/ip/g
     * 说明：获取IP策略
     * 读取Nutch配置文件的内容，包括 代理服务器地址，代理服务器端口，代理服务器用户名，代理服务器密码；如果IP策略设置为不可用，
     * 则将配置信息保存到同名的.bak文件中。
     * 参数1：spiderid爬虫id（必填）
     * 成功：IP策略配置文件内容的JSON串
     * 失败：[“error”:”错误原因”]
     *
     * @param ipStrategy
     * @return
     */
    @Override
    public String getIPStrategy(IPStrategy ipStrategy) {

        long lasting = System.currentTimeMillis();
        JSONObject json = new JSONObject();
        try {
            Document doc = XMLBuilder.parse(READ_XMLFILE_PATH);
            /*获取文档的根节点. */
            Element root = doc.getRootElement();
            doc.setXMLEncoding("utf-8");    //默认utf-8
            /*取得某个节点的子节点.*/
            List list = new LinkedList();

            for (Iterator i = root.elementIterator(); i.hasNext(); ) {
                Map map = new HashMap();
                Element employee = (Element) i.next();
                for (Iterator j = employee.elementIterator(); j.hasNext(); ) {
                    Element node = (Element) j.next();
                    System.out.println(node.getName() + ":" + node.getText());
                    map.put(node.getName(), node.getText());
                }
                list.add(map);

            }

            json.put(root.getName(), list);
            LOGGER.debug("JSON:{}", json);
            LOGGER.debug("计时:{}", lasting);
        } catch (Exception e) {
            //e.printStackTrace();
            LOGGER.error("Exception", e);
            json.put("error", e.getCause());
        }
        return JSON.toJSONString(json);
    }

    /**
     * 4.3.3.2更新 /sp/ip/u
     * 说明：更新IP策略XML
     * IP策略配置XML文件，保存在/config/{爬虫标识}文件夹中。
     * 参数1：spiderid爬虫id（必填）
     * 参数2：ipconfig内容（必填），传递格式为JSON，保存格式为XML，格式请参看《爬虫处理流程及界面.vsd》中“爬取源”
     * 成功：success
     * 失败：[“error”:”错误原因”]
     *
     * @param ipStrategy
     */
    @Override
    public String setIPStrategy(IPStrategy ipStrategy) {
        long lasting = System.currentTimeMillis();
        org.json.JSONObject json = new org.json.JSONObject();
        try {

            Document doc = XMLBuilder.parse(READ_XMLFILE_PATH);
            /*获取文档的根节点. */
            Element root = doc.getRootElement();
            doc.setXMLEncoding("utf-8");    //默认utf-8
            /*取得某个节点的子节点.*/
            List list = new LinkedList();

            for (Iterator i = root.elementIterator(); i.hasNext(); ) {
                Map map = new HashMap();
                Element employee = (Element) i.next();
                if (employee.element("name").getText().equals(IP_HOST)) {
                    //employee.element("name").setText("");
                    employee.element("value").setText(ipStrategy.getIpHost());
                    employee.element("description").setText(ipStrategy.getIpHostDesc());
                }
                if (employee.element("name").getText().equals(IP_PORT)) {
                    //employee.element("name").setText("");
                    employee.element("value").setText(ipStrategy.getIpPort());
                    employee.element("description").setText(ipStrategy.getIpPortDesc());
                }
                if (employee.element("name").getText().equals(IP_USERNAME)) {
                    //employee.element("name").setText("");
                    employee.element("value").setText(ipStrategy.getIpUsername());
                    employee.element("description").setText(ipStrategy.getIpUsernameDesc());
                }
                if (employee.element("name").getText().equals(IP_PASSWORD)) {
                    //employee.element("name").setText("");
                    employee.element("value").setText(ipStrategy.getIpPassword());
                    employee.element("description").setText(ipStrategy.getIpPasswordDesc());
                }

                list.add(map);

            }
            XMLBuilder.saveDocument(doc, WRITE_XMLFILE_PATH);
            return "success";
        } catch (Exception e) {
            // e.printStackTrace();
            LOGGER.error("Exception", e);
            json.put("error", e.getCause());
        }
        return json.toString();
    }

    /**
     * 如果IP策略设置为不可用，
     * 则将配置信息保存到同名的.bak文件中。
     *
     * @param ipStrategy
     */
    @Override
    public String saveIPStrategy(IPStrategy ipStrategy) {

        /*TODO*/
        return null;
    }
}
