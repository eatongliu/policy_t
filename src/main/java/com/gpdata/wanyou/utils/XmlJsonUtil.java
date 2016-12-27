package com.gpdata.wanyou.utils;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.*;

//import org.dom4j.Document;
//import org.dom4j.Element;

/**
 * common xml conver utility
 *
 * @author viruscodecn@gmail.com
 * @version Framework 2010.10.26
 * @url http://blog.csdn.net/arjick/article/details/6251777
 */

public class XmlJsonUtil {
    /**
     * 转换一个xml格式的字符串到json格式
     *
     * @param xml xml格式的字符串
     * @return 成功返回json 格式的字符串;失败反回null
     */
    public static JSONObject xml2JSON(String xml) {
        JSONObject obj = new JSONObject();
        try {
            InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(is);
            Element root = doc.getRootElement();
            obj.put(root.getName(), iterateElement(root));
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 转换一个xml格式的字符串到json格式
     *
     * @param file java.io.File实例是一个有效的xml文件
     * @return 成功反回json 格式的字符串;失败反回null
     */
    public static JSONObject xml2JSON(File file) {
        JSONObject obj = new JSONObject();
        try {
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(file);
            Element root = doc.getRootElement();
            obj.put(root.getName(), iterateElement(root));
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 一个迭代方法
     *
     * @param element : org.jdom.Element
     * @return java.util.Map 实例
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Map iterateElement(Element element) {
        List jiedian = element.getChildren();
        Element et = null;
        Map obj = new HashMap();
        List list = null;
        for (int i = 0; i < jiedian.size(); i++) {
            list = new LinkedList();
            et = (Element) jiedian.get(i);
            if (et.getTextTrim().equals("")) {
                if (et.getChildren().size() == 0)
                    continue;
                if (obj.containsKey(et.getName())) {
                    list = (List) obj.get(et.getName());
                }
                list.add(iterateElement(et));
                obj.put(et.getName(), list);
            } else {
                if (obj.containsKey(et.getName())) {
                    list = (List) obj.get(et.getName());
                }
                list.add(et.getTextTrim());
                obj.put(et.getName(), list);
            }
        }
        return obj;
    }

    // 测试  
    public static void main(String[] args) {
        System.out.println(XmlJsonUtil.xml2JSON("<MapSet>"
                + "<MapGroup id='Sheboygan'>" + "<Map>"
                + "<Type>MapGuideddddddd</Type>"
                + "<SingleTile>true</SingleTile>" + "<Extension>"
                + "<ResourceId>ddd</ResourceId>" + "</Extension>" + "</Map>"
                + "<Map>" + "<Type>ccc</Type>" + "<SingleTile>ggg</SingleTile>"
                + "<Extension>" + "<ResourceId>aaa</ResourceId>"
                + "</Extension>" + "</Map>" + "<Extension />" + "</MapGroup>"
                + "<ddd>" + "33333333" + "</ddd>" + "<ddd>" + "444" + "</ddd>"
                + "</MapSet>"));
    }


    /**
     * map to xml xml <node><key label="key1">value1</key><key
     * label="key2">value2</key>......</node>
     *
     * @param map
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String maptoXml(Map map) {
        org.dom4j.Document document = DocumentHelper.createDocument();
        org.dom4j.Element nodeElement = document.addElement("node");
        for (Object obj : map.keySet()) {
            org.dom4j.Element keyElement = nodeElement.addElement("key");
            keyElement.addAttribute("label", String.valueOf(obj));
            keyElement.setText(String.valueOf(map.get(obj)));
        }
        return doc2String(document);
    }

    /**
     * list to xml xml <nodes><node><key label="key1">value1</key><key
     * label="key2">value2</key>......</node><node><key
     * label="key1">value1</key><key
     * label="key2">value2</key>......</node></nodes>
     *
     * @param list
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String listtoXml(List list) throws Exception {
        org.dom4j.Document document = DocumentHelper.createDocument();
        org.dom4j.Element nodesElement = document.addElement("nodes");
        int i = 0;
        for (Object o : list) {
            org.dom4j.Element nodeElement = nodesElement.addElement("node");
            if (o instanceof Map) {
                for (Object obj : ((Map) o).keySet()) {
                    org.dom4j.Element keyElement = nodeElement.addElement("key");
                    keyElement.addAttribute("label", String.valueOf(obj));
                    keyElement.setText(String.valueOf(((Map) o).get(obj)));
                }
            } else {
                org.dom4j.Element keyElement = nodeElement.addElement("key");
                keyElement.addAttribute("label", String.valueOf(i));
                keyElement.setText(String.valueOf(o));
            }
            i++;
        }
        return doc2String(document);
    }

    /**
     * json to xml {"node":{"key":{"@label":"key1","#text":"value1"}}} conver
     * <o><node class="object"><key class="object"
     * label="key1">value1</key></node></o>
     *
     * @param json
     * @return
     */
    public static String jsontoXml(String json) {
        try {
            XMLSerializer serializer = new XMLSerializer();
            JSON jsonObject = JSONSerializer.toJSON(json);
            return serializer.write(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * xml to map xml <node><key label="key1">value1</key><key
     * label="key2">value2</key>......</node>
     *
     * @param xml
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map xmltoMap(String xml) {
        try {
            Map map = new HashMap();
            org.dom4j.Document document = DocumentHelper.parseText(xml);
            org.dom4j.Element nodeElement = document.getRootElement();
            List node = nodeElement.elements();
            for (Iterator it = node.iterator(); it.hasNext(); ) {
                Element elm = (Element) it.next();
                map.put(elm.getAttributeValue("label"), elm.getText());
                elm = null;
            }
            node = null;
            nodeElement = null;
            document = null;
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * xml to list xml <nodes><node><key label="key1">value1</key><key
     * label="key2">value2</key>......</node><node><key
     * label="key1">value1</key><key
     * label="key2">value2</key>......</node></nodes>
     *
     * @param xml
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List xmltoList(String xml) {
        try {
            List<Map> list = new ArrayList<Map>();
            org.dom4j.Document document = DocumentHelper.parseText(xml);
            org.dom4j.Element nodesElement = document.getRootElement();
            List nodes = nodesElement.elements();
            for (Iterator its = nodes.iterator(); its.hasNext(); ) {
                Element nodeElement = (Element) its.next();
                Map map = xmltoMap(((Node) nodeElement).asXML());
                list.add(map);
                map = null;
            }
            nodes = null;
            nodesElement = null;
            document = null;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * xml to json <node><key label="key1">value1</key></node> 转化为
     * {"key":{"@label":"key1","#text":"value1"}}
     *
     * @param xml
     * @return
     */
    public static String xmltoJson(String xml) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        return xmlSerializer.read(xml).toString();
    }

    /**
     * @param document
     * @return
     */
    public static String doc2String(org.dom4j.Document document) {
        String s = "";
        try {
            // 使用输出流来进行转化  
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 使用UTF-8编码  
            OutputFormat format = new OutputFormat("   ", true, "UTF-8");
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(document);
            s = out.toString("UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return s;
    }
}  