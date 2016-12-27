package com.gpdata.wanyou.ansj.util;

import com.gpdata.wanyou.ansj.entity.DefineLibrary;
import com.gpdata.wanyou.ansj.entity.StopLibrary;
import com.gpdata.wanyou.ansj.service.DefineLibraryService;
import com.gpdata.wanyou.ansj.service.StopLibraryService;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.recognition.impl.FilterRecognition;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.util.MyStaticValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * ansj分词工具类
 *
 * @author yaz
 * @create 2016-12-12 16:38
 */
public class AnsjUtil {
    private static final Logger logger = LoggerFactory.getLogger(AnsjUtil.class);

    private static List<String> stopLibrary = new ArrayList<>(2048);
    private static List<String> defineLibrary = new ArrayList<>(32);

    @Autowired
    private StopLibraryService stopLibraryService;
    @Autowired
    private DefineLibraryService defineLibraryService;

    /**
     * ansj初始化，由Spring管理
     *
     * @Author yaz
     * @Date 2016/12/13 11:51
     */
    @PostConstruct
    public void initAnsj() {
        logger.info("==========================ansj分词初始化_开始==========================");
        MyStaticValue.isRealName = false;
        stopLibrary.addAll(stopLibraryService.getStopWordList());
        //数据量较大，开发时 可先注释
        defineLibrary.addAll(defineLibraryService.getDefineWordList());
        if (!defineLibrary.isEmpty()) {
            for (String bean : defineLibrary) {
                UserDefineLibrary.insertWord(bean);
            }
        }
        logger.info("==========================ansj分词初始化_结束，共添加了{}个停用词，{}个用户自定义词=========================="
                , stopLibrary.size(), defineLibrary.size());
    }

    private AnsjUtil() {
    }

    private static class AnsjUtilHolder {
        private static final AnsjUtil INSTANCE = new AnsjUtil();
    }

    public static final AnsjUtil getInstance() {
        return AnsjUtilHolder.INSTANCE;
    }

    /**
     * 解析词语，返回关键词
     * str：需要解析的字符串
     * isUseStopLibaray：是否使用停用词表
     *
     * @Author yaz
     * @Date 2016/12/13 10:37
     */
    public List<String> parseString(String str, boolean isUseStopLibrary) {
        Result r = NlpAnalysis.parse(str);
        if (isUseStopLibrary) {
            FilterRecognition filterRecognition = new FilterRecognition();
            filterRecognition.insertStopWords(stopLibrary);
            filterRecognition.recognition(r);
        }
        List<Term> parse = r.getTerms();
        ArrayList<String> rs = new ArrayList<>();
        for (Term bean : parse) {
            if (!"".equals(bean.getRealName())) {
                rs.add(bean.getRealName());
            }
        }
        return rs;
    }

    /**
     * 解析文章，返回词频
     *
     * @Author yaz
     * @Date 2016/12/16 17:21
     */
    public Map<String, Integer> parseFile(String str) {
        Result r = NlpAnalysis.parse(str);
        FilterRecognition filterRecognition = new FilterRecognition();
        filterRecognition.insertStopWords(stopLibrary);
        filterRecognition.recognition(r);
        List<Term> parse = r.getTerms();

        HashMap<String, Integer> rs = new HashMap<>(2048);
        String temp;
        Integer total;

        for (Term bean : parse) {
            temp = bean.getRealName().trim();
            if (temp.equals("")) {
                continue;
            }
            if (rs.containsKey(temp)) {
                total = rs.get(temp);
                rs.put(temp, ++total);
            } else {
                rs.put(temp, 1);
            }
        }
        return rs;
    }

    /**
     * 增加停用词
     *
     * @Author yaz
     * @Date 2016/12/13 12:06
     */
    public void addStopWord(String str) {
        stopLibrary.add(str);
        //持久化
        StopLibrary bean = new StopLibrary();
        bean.setWord(str);
        bean.setCreateDate(new Date());
        stopLibraryService.saveEntity(bean);
    }

    /**
     * 增加停用词
     *
     * @Author yaz
     * @Date 2016/12/13 12:06
     */
    public void addStopWord(List<String> words) {
        stopLibrary.addAll(words);
        //持久化
        for (String bean : words) {
            StopLibrary s = new StopLibrary();
            s.setWord(bean);
            s.setCreateDate(new Date());
            stopLibraryService.saveEntity(s);
        }
    }

    /**
     * 删除停用词
     *
     * @Author yaz
     * @Date 2016/12/13 12:13
     */
    public void deleteStopWord(StopLibrary stopWord) {
        stopLibrary.remove(stopWord.getWord());
        stopLibraryService.deleteEntity(stopWord);
    }

    /**
     * 增加用户自定义词
     *
     * @Author yaz
     * @Date 2016/12/13 12:16
     */
    public void addDefineWord(String str) {
        UserDefineLibrary.insertWord(str);
        DefineLibrary bean = new DefineLibrary();
        bean.setCreateDate(new Date());
        bean.setWord(str);
        defineLibraryService.saveEntity(bean);
    }

    /**
     * 增加用户自定义词
     *
     * @Author yaz
     * @Date 2016/12/13 12:16
     */
    public void addDefineWord(List<String> definWords) {
        for (String str : definWords) {
            addDefineWord(str);
        }
    }

    public void deleteDefineWord(DefineLibrary defineWord) {
        defineLibrary.remove(defineWord);
        UserDefineLibrary.removeWord(defineWord.getWord());
    }

}
