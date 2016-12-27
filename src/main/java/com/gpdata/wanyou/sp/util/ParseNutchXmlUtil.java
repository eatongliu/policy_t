package com.gpdata.wanyou.sp.util;

import com.gpdata.wanyou.sp.entity.ParseFieldConfig;
import com.gpdata.wanyou.sp.entity.ParsePageConfig;
import com.gpdata.wanyou.utils.ConfigUtil;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by chengchao on 16-10-14.
 */
public class ParseNutchXmlUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParseNutchXmlUtil.class);

    private static final String PARSE_FILE_NAME = "parse.xml";


    /**
     * 使用爬虫id (spiderid) 获取 parse.xml 文件的位置
     *
     * @param spiderid
     * @return
     */
    private static final String retrieveFilePath(String spiderid) {


        String projectRootPath = ConfigUtil.getConfig("wanyou.project.root");
        String defaultPath = projectRootPath + "/WEB-INF/temp/nutch";

        String nutchRootPath = ConfigUtil.getConfig("wanyou.nutch.root");
        LOGGER.debug("nutchRootPath : {}", nutchRootPath);
        if (StringUtils.isBlank(nutchRootPath)) {
            nutchRootPath = defaultPath;
        }
        String filePath = nutchRootPath + "/" + spiderid + "/" + PARSE_FILE_NAME;
        LOGGER.debug("filePath : {}", filePath);

        return filePath;
    }


    /**
     * 使用路径 (filePath) 获取 parse.xml 文件,
     * 并读出 Document 返回.
     * 如果文件不存在或者不能读取成 Document 对象, 返回 null.
     *
     * @param filePath
     * @return
     */
    private static final Document retrieveDocument(String filePath) {

        // 类似 : /home/chengchao/temp/nutchconfig

        File file = new File(filePath);

        SAXReader reader = new SAXReader();
        Document document = null;

        if (file.exists()) {
            try {
                document = reader.read(file);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }

        return document;
    }

    /**
     * 保存文档
     *
     * @param filePath
     * @param document
     */
    private static final void saveDocument(String filePath, Document document) {

        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter writer = null;
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        try (FileWriter fileWriter = new FileWriter(file)) {
            writer = new XMLWriter(fileWriter, format);
            writer.write(document);
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 使用 查找 /parse/page/id 值为指定值的 page Element 元素
     * <p>
     * 如果找不到, 返回 null
     *
     * @param document
     * @param id
     * @return
     */
    public static Element findParentPageElement(Document document, String id) {

        if (document == null) {
            return null;
        }

        List<Element> list = document.selectNodes("/parse/page/id");

        Optional<Element> optional = list.stream()
                .filter(element -> element.getStringValue().equalsIgnoreCase(id))
                .findFirst();

        return optional.isPresent() ? optional.get().getParent() : null;


    }


    /**
     * 创建新 Document
     *
     * @return
     */
    private static final Document createNewDocument() {


        Document document = DocumentHelper.createDocument();
        document.addElement("parse");

        return document;
    }

    /**
     * 创建 page Element
     *
     * @param parent
     * @return
     */
    private static final Element createPageElement(Element parent
            , ParsePageConfig parsePageConfig) {

        Element page = parent.addElement("page");
        page.addElement("id").setText(parsePageConfig.getId());


        page.addElement("caption").setText(parsePageConfig.getCaption());
        page.addElement("designurl").setText(parsePageConfig.getDesignurl());
        page.addElement("parseurl").setText(parsePageConfig.getParseurl());
        page.addElement("tableid").setText(parsePageConfig.getTableid());

        return page;
    }


    private static String getNextId(String spiderid) {

        String filePath = retrieveFilePath(spiderid);
        Document document = retrieveDocument(filePath);

        if (document != null) {

            List<Element> list = document.selectNodes("/parse/page/id");

            OptionalInt optional = list.stream()
                    .map(element -> element.getStringValue())
                    .filter(StringUtils::isNotBlank)
                    .map(Integer::valueOf)
                    .mapToInt(Integer::intValue)
                    .max();

            if (optional.isPresent()) {
                return optional.getAsInt() + 1 + "";
            }
        }

        return "1";

    }


    /**
     * 创建 PageConfig 并保存
     * 如果文件不存在, 创建一个新的文件.
     *
     * @param parsePageConfig
     * @return
     */
    public static final String addPageConfig(ParsePageConfig parsePageConfig) {

        String spiderid = parsePageConfig.getSpiderid();

        if (StringUtils.isAnyBlank(spiderid)) {
            throw new RuntimeException(
                    String.format("spiderid 值不存在"));
        }

        String id = getNextId(spiderid);
        parsePageConfig.setId(id);

        String filePath = retrieveFilePath(spiderid);
        Document document = retrieveDocument(filePath);

        if (document == null) {
            // 创建新的文档
            File parentDir = new File(filePath).getParentFile();
            parentDir.mkdirs();
            document = createNewDocument();

        }

        Element pageElement = findParentPageElement(document, id);

        if (pageElement != null) {
            throw new RuntimeException(
                    String.format("指定的 page 的 id 值已经存在 (id = %s)", id));
        }

        Element root = document.getRootElement();
        createPageElement(root, parsePageConfig);
        saveDocument(filePath, document);

        return id;

    }


    /**
     * 根据爬虫ID 和 page 中的 id 获取 pageConfig 数据
     *
     * @param spiderid
     * @param id
     * @return
     */
    public static Map<String, String> retrieveParsePageConfig(String spiderid, String id) {


        String filePath = retrieveFilePath(spiderid);
        Document document = retrieveDocument(filePath);

        Map<String, String> result = new HashMap<>();

        if (document == null) {
            // 文档不存在返回空 Map
            return result;
        }

        Element pageElement = findParentPageElement(document, id);
        if (pageElement == null) {
            // 找不到对应的元素, 返回空 Map
            return result;
        }

        Iterator<Element> iterator = pageElement.elementIterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            String name = element.getName();
            String value = element.getStringValue();
            result.put(name, value);
        }

        return result;
    }


    /**
     * 修改 Nutch 配置的 Page 部分
     * <p>
     * 找到爬虫 id 和 page id 所对应的配置 数据段, 修改配置并保存
     *
     * @param parsePageConfig
     */
    public static void updatePageConfig(ParsePageConfig parsePageConfig) {


        String spiderid = parsePageConfig.getSpiderid();
        String id = parsePageConfig.getId();

        if (StringUtils.isAnyBlank(spiderid, id)) {
            throw new RuntimeException("spiderid 和 id 缺一不可!");
        }

        String filePath = retrieveFilePath(spiderid);
        Document document = retrieveDocument(filePath);

        if (document == null) {
            throw new RuntimeException(String.format("文件不存在 (%s)", filePath));
        }

        Element pageElement = findParentPageElement(document, id);

        if (pageElement == null) {
            throw new RuntimeException(String.format("page 元素不存在 (id = %s)", id));
        }

        for (String key : parsePageConfig.keyList()) {

            Element element = pageElement.element(key);
            if (element != null) {
                element.setText(parsePageConfig.getText(key));
            } else {
                pageElement.addElement(key).setText(parsePageConfig.getText(key));
            }
        }

        saveDocument(filePath, document);

    }


    /**
     * 根据爬虫ID 和 page 中的 id 删除 pageConfig 数据
     *
     * @param parsePageConfig
     */
    public static void deletePageConfig(ParsePageConfig parsePageConfig) {


        String spiderid = parsePageConfig.getSpiderid();
        String id = parsePageConfig.getId();

        if (StringUtils.isAnyBlank(spiderid, id)) {
            throw new RuntimeException("spiderid 和 Page id 缺一不可");
        }

        String filePath = retrieveFilePath(spiderid);
        Document document = retrieveDocument(filePath);

        if (document != null) {
            Element pageElement = findParentPageElement(document, id);
            if (pageElement != null) {
                pageElement.getParent().remove(pageElement);
                saveDocument(filePath, document);
            }
        }

    }


    /**
     * 查询 page config
     * 参数 parsePageConfig 对象中必须有 spiderid (用来确定是哪个爬虫)
     * 也可以有 cpation (模糊搜索)
     *
     * @param parsePageConfig
     * @return
     */
    public static List<ParsePageConfig> queryPageConfig(ParsePageConfig parsePageConfig) {


        String spiderid = parsePageConfig.getSpiderid();
        final String caption = parsePageConfig.getCaption();

        if (StringUtils.isAnyBlank(spiderid)) {

            return Arrays.asList();
        }


        String filePath = retrieveFilePath(spiderid);
        Document document = retrieveDocument(filePath);


        if (document != null) {

            List<Element> list = document.selectNodes("/parse/page");

            /* 没指定 caption 时, 返回全部数据还是全部不返回呢? */
            if (StringUtils.isBlank(caption)) {

            }

            List<ParsePageConfig> result = list.stream()
                    .filter(pageElement -> {

                        String cap = (caption == null) ? "" : caption;
                        Element captionElement = pageElement.element("caption");
                        if (captionElement != null) {
                            String captionValue = captionElement.getText();
                            if (captionValue != null) {
                                return captionValue.contains(cap);
                            }
                        }
                        return false;
                    })
                    .map(ParseNutchXmlUtil::pageElement2ParsePageConfig)
                    .collect(Collectors.toList());

            return result;

        }

        return Arrays.asList();

    }


    /**
     * 对于传入的 xml 元素, 获取其子元素名称为 name 的文本值
     *
     * @param element
     * @param name
     * @return
     */
    private static final String getSubElementText(Element element, String name) {

        Element sub = element.element(name);

        if (sub != null) {

            String value = sub.getText();
            return (value == null) ? "" : toEmptyString(value.toString().trim());
        }

        return "";

    }

    /**
     * 获取对象的 toSting 值, 如果是 null , 返回空串 ""
     *
     * @param value
     * @return
     */
    private static final String toEmptyString(Object value) {

        return (value == null) ? "" : value.toString();

    }

    /**
     * 对特定的 page 元素, 转换成 ParsePageConfig 对象
     *
     * @param element
     * @return
     */
    private static final ParsePageConfig pageElement2ParsePageConfig(Element element) {

        ParsePageConfig bean = new ParsePageConfig();
        bean.setId(getSubElementText(element, "id"));
        bean.setCaption(getSubElementText(element, "caption"));
        bean.setDesignurl(getSubElementText(element, "designurl"));
        bean.setParseurl(getSubElementText(element, "parseurl"));
        bean.setSpiderid(getSubElementText(element, "spiderid"));
        bean.setTableid(getSubElementText(element, "tableid"));

        return bean;
    }


    /**
     * 获取 Nutch 配置中的 field, 根据 Page: id, field : caption
     * 返回第一个找到的
     *
     * @param parseFieldConfig
     * @return
     */
    public static ParseFieldConfig retrieveParseFieldConfig(ParseFieldConfig parseFieldConfig) {


        String spiderid = parseFieldConfig.getSpiderid();
        String id = parseFieldConfig.getId();
        String caption = parseFieldConfig.getCaption();

        if (StringUtils.isAnyBlank(spiderid, id, caption)) {

            return new ParseFieldConfig();
        }


        String filePath = retrieveFilePath(spiderid);
        Document document = retrieveDocument(filePath);
        List<Element> list = document.selectNodes("/parse/page");

        Optional<Element> optional = list.stream()
                .filter(Objects::nonNull)
                .filter(element ->
                        Objects.nonNull(element.getText()) && element.getText().trim().equals(id))
                .findFirst();

        if (optional.isPresent()) {
            Element pageElement = optional.get();
            Iterator iter = pageElement.elementIterator("field");
            Element fieldElement = null;
            for (; iter.hasNext(); ) {
                //Element value= iter.next();

            }

        }


        return null;

    }
}
