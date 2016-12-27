package com.gpdata.wanyou.ds.util;

import au.com.bytecode.opencsv.CSVReader;
import com.alibaba.fastjson.JSON;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.util.*;

/**
 * 全文检索工具类
 *
 * @author quyili xinghl
 */
public class TextSearchUtil {
    private static TextSearchUtil indexManager;
    private static String content = "";
    private static Analyzer analyzer = null;//词法分析器
    private static Directory directory = null;//索引文件存储目录
    private static IndexWriter indexWriter = null;//索引文件的写入

    /**
     * 创建当前文件目录的索引
     *
     * @param path 当前文件目录
     * @return 是否成功
     */
    @SuppressWarnings("deprecation")
    public static boolean createIndex(String path, String INDEX_DIR) {
        List<File> fileList = getFileList(path);
        if (fileList.size() == 1) {
            System.out.println("in");
            File file = fileList.get(0);
            content = "";
            //获取文件后缀
            String type = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            if ("txt".equalsIgnoreCase(type)) {
                content += txt2String(file);
            } else if ("xls".equalsIgnoreCase(type) || "xlsx".equalsIgnoreCase(type)) {
                content += xls2String(file);
            } else if ("csv".equalsIgnoreCase(type)) {
                content += csv2String(file);
            } else if ("doc".equalsIgnoreCase(type) || "docx".equalsIgnoreCase(type)) {
                content += doc2String(file);
            }
            try {
                //定义一个词法分析器
                analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
                //第二步，确定索引文件存储的位置
                directory = FSDirectory.open(new File(INDEX_DIR));
                File indexFile = new File(INDEX_DIR);
                if (!indexFile.exists()) {
                    indexFile.mkdirs();
                }
                IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
                //第三步，创建IndexWriter，进行索引文件的写入
                indexWriter = new IndexWriter(directory, config);
                //第四步，内容提取，进行索引的存储
                //申请了一个document对象
                Document document = new Document();
                //添加即将索引的字符串，把字符串存储起来
                document.add(new TextField("filename", file.getName(), Store.YES));
                document.add(new TextField("content", content, Store.YES));
                document.add(new TextField("path", file.getPath(), Store.YES));
                //把doc对象加入到索引创建中
                indexWriter.addDocument(document);
                //关闭IndexWriter,提交创建内容
                indexWriter.commit();
                closeWriter();
            } catch (Exception e) {
                e.printStackTrace();
            }
            content = "";
        } else {
            for (File file : fileList) {
                content = "";
                //获取文件后缀
                String type = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                if ("txt".equalsIgnoreCase(type)) {
                    content += txt2String(file);
                } else if ("xls".equalsIgnoreCase(type) || "xlsx".equalsIgnoreCase(type)) {
                    content += xls2String(file);
                } else if ("csv".equalsIgnoreCase(type)) {
                    content += csv2String(file);
                } else if ("doc".equalsIgnoreCase(type) || "docx".equalsIgnoreCase(type)) {
                    content += doc2String(file);
                }
                try {
                    //定义一个词法分析器
                    analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
                    //第二步，确定索引文件存储的位置
                    directory = FSDirectory.open(new File(INDEX_DIR));
                    File indexFile = new File(INDEX_DIR);
                    if (!indexFile.exists()) {
                        indexFile.mkdirs();
                    }
                    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
                    //第三步，创建IndexWriter，进行索引文件的写入
                    indexWriter = new IndexWriter(directory, config);
                    //第四步，内容提取，进行索引的存储
                    //申请了一个document对象
                    Document document = new Document();
                    //添加即将索引的字符串，把字符串存储起来
                    document.add(new TextField("filename", file.getName(), Store.YES));
                    document.add(new TextField("content", content, Store.YES));
                    document.add(new TextField("path", file.getPath(), Store.YES));
                    //把doc对象加入到索引创建中
                    indexWriter.addDocument(document);
                    //关闭IndexWriter,提交创建内容
                    indexWriter.commit();
                    closeWriter();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                content = "";
            }
        }
        return true;
    }

    /**
     * 读取txt文件的内容
     *
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static String txt2String(File file) {
        String result = "";
        try {
            if (file.length() > 0) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
                String s = null;
                while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                    s = s.replace(',', '\t').replace('，', '\t');//根据需要正则匹配分词
                    result += s + "\n";
                }
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取csv文件的内容
     *
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static String csv2String(File file) {
        String result = "";
        try {
            if (file.length() > 0) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
                CSVReader csvReader = new CSVReader(br, ',');
                List<String[]> list = csvReader.readAll();
                for (String[] ss : list) {
                    for (String s : ss)
                        if (null != s && !s.equals("")) {
                            s = s.replace(',', '\t').replace('，', '\t');//根据需要正则匹配分词
                            result += s + "\n";
                        }
                }
                csvReader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取doc文件内容
     *
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    @SuppressWarnings("resource")
    public static String doc2String(File file) {
        String result = "";
        String suffix = file.toString().substring(file.toString().lastIndexOf(".") + 1);
        try {
            if (file.length() > 0) {
                InputStream is = new FileInputStream(file);
                if (suffix.equals("doc")) {
                    // word 2003： 图片不会被读取
                    WordExtractor ex = new WordExtractor(is);// is是WORD文件的InputStream
                    result = ex.getText().trim().replace(',', '\t').replace('，', '\t');//根据需要正则匹配分词
                } else if (suffix.equals("docx")) {
                    // word 2007 图片不会被读取， 表格中的数据会被放在字符串的最后
                    /**
                     * 另一种方法
                     * OPCPackage opcPackage = POIXMLDocument.openPackage(file.toString());
                     * POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
                     * result = extractor.getText().trim();
                     */
                    XWPFDocument doc = new XWPFDocument(is);
                    XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
                    result = extractor.getText().trim().replace(',', '\t').replace('，', '\t');//根据需要正则匹配分词
                }
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取xls文件内容
     *
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static String xls2String(File file) {
        String suffix = file.toString().substring(file.toString().lastIndexOf(".") + 1);
        String result = "";
        try {
            if (file.length() > 0) {
                InputStream is = new FileInputStream(file);
                StringBuilder sb = new StringBuilder();
                if (suffix.equals("xls")) {
                    Workbook rwb = Workbook.getWorkbook(is);
                    Sheet[] sheet = rwb.getSheets();
                    for (int i = 0; i < sheet.length; i++) {
                        Sheet rs = rwb.getSheet(i);
                        for (int j = 0; j < rs.getRows(); j++) {
                            Cell[] cells = rs.getRow(j);
                            for (int k = 0; k < cells.length; k++)
                                sb.append(cells[k].getContents());
                        }
                    }
                } else if (suffix.equals("xlsx")) {
                    XSSFWorkbook rwb = new XSSFWorkbook(is);
                    int sheetNum = rwb.getNumberOfSheets();
                    XSSFSheet sheet = null;
                    for (int sheetIndex = 0; sheetIndex < sheetNum; sheetIndex++) {//遍历sheet(index 0开始)
                        sheet = rwb.getSheetAt(sheetIndex);
                        Row row = null;
                        int firstRowNum = sheet.getFirstRowNum();
                        int lastRowNum = sheet.getLastRowNum();
                        for (int rowIndex = firstRowNum; rowIndex <= lastRowNum; rowIndex++) {//遍历row(行 0开始)
                            row = sheet.getRow(rowIndex);
                            if (null != row) {
                                int firstCellNum = row.getFirstCellNum();
                                int lastCellNum = row.getLastCellNum();
                                for (int cellIndex = firstCellNum; cellIndex < lastCellNum; cellIndex++) {//遍历cell（列 0开始）
                                    org.apache.poi.ss.usermodel.Cell cell = row.getCell(cellIndex, Row.RETURN_BLANK_AS_NULL);
                                    if (null != cell) {
                                        Object cellValue = null;//cellValue的值
                                        switch (cell.getCellType()) {
                                            case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
                                                cellValue = cell.getRichStringCellValue()
                                                        .getString();
                                                break;
                                            case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
                                                if (DateUtil.isCellDateFormatted(cell)) {
                                                    cellValue = cell.getDateCellValue();
                                                } else {
                                                    cellValue = cell.getNumericCellValue();
                                                }
                                                break;
                                            case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN:
                                                cellValue = cell.getBooleanCellValue();
                                                break;
                                            case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA:
                                                cellValue = cell.getCellFormula();
                                                break;
                                            default:
                                                System.out.println("not find match type.");
                                        }
                                        sb.append(cellValue);
                                    }
                                }//end cell
                            }
                        }//end row
                    }//end sheet
                }
                is.close();
                result += sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 查找索引，返回符合条件的文件
     *
     * @param text 查找的字符串
     * @return 符合条件的文件List
     */
    @SuppressWarnings("deprecation")
    public static String searchIndex(String text, String INDEX_DIR) {
        HashSet<Map<String, String>> res = new HashSet<Map<String, String>>();
        ;
        try {
            //确定索引文件存储的位置
            //内存存储
            //Directory directory = new RAMDirectory();
            //本地文件存储
            directory = FSDirectory.open(new File(INDEX_DIR));
            //定义一个词法分析器,此处采用标准词法分析器，如果专门针对汉语，还可以搭配paoding
            //参数中的Version.LUCENE_CURRENT，代表使用当前的Lucene版本，也可以写成Version.LUCENE_40
            analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
            //第一步，打开存储位置
            DirectoryReader ireader = DirectoryReader.open(directory);
            //第二步，创建搜索器
            IndexSearcher isearcher = new IndexSearcher(ireader);
            //第三步，进行关键字查询
            QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, "content", analyzer);
            Query query = parser.parse(text);
            ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
            for (int i = 0; i < hits.length; i++) {
                Document hitDoc = isearcher.doc(hits[i].doc);
                Map<String, String> map = new HashMap<String, String>();
                map.put("content", hitDoc.get("content"));
                map.put("path", hitDoc.get("path"));
                map.put("filename", hitDoc.get("filename"));
                res.add(map);
            }
            //第四步，关闭查询器等
            ireader.close();
            directory.close();
            return JSON.toJSONString(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 过滤目录下的文件
     *
     * @param dirPath 想要获取文件的目录
     * @return 返回文件list
     */
    public static List<File> getFileList(String dirPath) {
        File f = new File(dirPath);
        List<File> fileList = new ArrayList<File>();
        if (f.isDirectory()) {
            File[] files = new File(dirPath).listFiles();
            for (File file : files) {
                if (isTxtFile(file.getName())) {
                    fileList.add(file);
                }
            }
        } else {
            if (isTxtFile(f.getName())) {
                fileList.add(f);
            }
        }
        return fileList;
    }

    /**
     * 判断是否为目标文件，目前支持txt xls doc格式
     *
     * @param fileName 文件名称
     * @return 如果是文件类型满足过滤条件，返回true；否则返回false
     */
    public static boolean isTxtFile(String fileName) {
        if (fileName.lastIndexOf(".txt") > 0) {
            return true;
        } else if (fileName.lastIndexOf(".xls") > 0 || fileName.lastIndexOf(".xlsx") > 0) {
            return true;
        } else if (fileName.lastIndexOf(".csv") > 0) {
            return true;
        } else if (fileName.lastIndexOf(".doc") > 0 || fileName.lastIndexOf(".docx") > 0) {
            return true;
        }
        return false;
    }

    /**
     * 关闭资源
     *
     * @throws Exception
     */
    public static void closeWriter() throws Exception {
        if (indexWriter != null) {
            indexWriter.close();
        }
    }

    /**
     * 删除文件目录下的所有文件
     *
     * @param file 要删除的文件目录
     * @return 如果成功，返回true.
     */
    public static boolean deleteDir(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteDir(files[i]);
            }
        }
        file.delete();
        return true;
    }

    /**
     * 创建索引管理器
     *
     * @return 返回索引管理器对象
     */
    @SuppressWarnings("static-access")
    public TextSearchUtil getManager() {
        if (indexManager == null) {
            this.indexManager = new TextSearchUtil();
        }
        return indexManager;
    }
}