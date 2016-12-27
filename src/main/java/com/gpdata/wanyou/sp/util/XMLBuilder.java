package com.gpdata.wanyou.sp.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.Properties;

/**
 * Created by guoxy on 2016/10/14.
 */
public class XMLBuilder {
    public static String SAVE_XMLFILE_PATH;

    static {
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("config.properties");
        Properties p = new Properties();
        try {
            p.load(is);
            SAVE_XMLFILE_PATH = p.getProperty("ipstgy.writepath");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public XMLBuilder() {

    }

    /**
     * 解析XML文档
     *
     * @param xmlFile
     * @return XML文档
     * @throws DocumentException
     * @throws FileNotFoundException
     */
    public static Document parse(String xmlFile) throws DocumentException, FileNotFoundException {
        SAXReader reader = new SAXReader();
        reader.setEncoding("GBK");
        Document doc = reader.read(new File(xmlFile));
        doc.setXMLEncoding("UTF8");    //默认utf-8
        return doc;
    }

    /***
     * 将XML文档输出到控制台
     *
     * @param doc
     * @throws IOException
     */
    public static void printDocument(Document doc) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new OutputStreamWriter(System.out), format);
        writer.write(doc);
        writer.close();
    }

    /**
     * 保存XML文档
     *
     * @param doc
     * @throws IOException
     */
    public static void saveDocument(Document doc, String savePath) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileOutputStream(savePath), format);
        writer.write(doc);
        writer.close();
    }
}
