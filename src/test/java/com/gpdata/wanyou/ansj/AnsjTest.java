package com.gpdata.wanyou.ansj;

import com.gpdata.wanyou.ansj.entity.StopLibrary;
import com.gpdata.wanyou.ansj.service.StopLibraryService;
import com.gpdata.wanyou.base.cache.RedisCache;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.recognition.impl.FilterRecognition;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.MyStaticValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.util.*;

/**
 * Created by guoxy on 2016/11/28.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {
//        "classpath:applicationContext.xml"
//})
public class AnsjTest {
    @Autowired
    private StopLibraryService stopLibraryService;

    @Test
    public void testAnsj() {
        // String str = "欢迎使用ansj_seg,(ansj中文分词)在这里如果你遇到什么问题都可以联系我.我一定尽我所能.帮助大家.ansj_seg更快,更准,更自由!";
        String str2 = "我在东华合创大厦，想要2016年到2017年北京政府房地产相关的政策";
        MyStaticValue.isRealName = false;

//        HashMap<String, String> strHashMap = new HashMap<String, String>();
//        /*基本*/
//        //System.out.println(BaseAnalysis.parse(str2));
//        /*精准*/
//        //System.out.println(ToAnalysis.parse(str2).toStringWithOutNature());
//        /*nlp*/
//        System.out.println(NlpAnalysis.parse(str2));
//        /*面向索引的分词*/
//        //System.out.println(IndexAnalysis.parse(str2));
//        UserDefineLibrary.insertWord("相关的政策", "userDefine", 1000);
        UserDefineLibrary.insertWord("东华合创大厦", "userDefine", 1000);
        Result r = NlpAnalysis.parse(str2);
        List<Term> parse = r.getTerms();

        FilterRecognition filterRecognition = new FilterRecognition();
        List<String> stops = new ArrayList<>();
        stops.add("我");
        stops.add("在");
        stops.add("想");
        stops.add("要");
        filterRecognition.insertStopWords(stops);
        filterRecognition.recognition(r);
        for (Term t : parse) {
            System.out.println(t.getOffe() + ">>" + t.getRealName() + "<<" + t.getNatureStr());
        }
        System.out.println("==========================分割线===============================");
        Result r1 = NlpAnalysis.parse(str2);
        List<Term> parse1 = r1.getTerms();
        List<String> stops1 = new ArrayList<>();
        stops1.add("我");
        FilterRecognition FilterRecognition1 = new FilterRecognition();
        FilterRecognition1.insertStopWords(stops1);
        FilterRecognition1.recognition(r1);

        for (Term t : parse1) {
            System.out.println(t.getOffe() + ">>" + t.getRealName() + "<<" + t.getNatureStr());
        }


//        for (Term t : parse) {
//            if(t.getNatureStr().equals("m"))
//            System.out.println(t.getOffe()+""+t.getRealName()+""+t.getNatureStr());
//        }
//        for (Term t : parse) {
//            if(t.getNatureStr().equals("q"))
//                System.out.println(t.getOffe()+""+t.getRealName()+""+t.getNatureStr());
//        }
//        for (Term t : parse) {
//            if(t.getNatureStr().equals("w"))
//                System.out.println(t.getOffe()+""+t.getRealName()+""+t.getNatureStr());
//        }
//        for (Term t : parse) {
//            if(t.getNatureStr().equals("r"))
//                System.out.println(t.getOffe()+""+t.getRealName()+""+t.getNatureStr());
//        }
//        for (Term t : parse) {
//            if(t.getNatureStr().equals("d"))
//                System.out.println(t.getOffe()+""+t.getRealName()+""+t.getNatureStr());
//        }
//        for (Term t : parse) {
//            if(t.getNatureStr().equals("v"))
//                System.out.println(t.getOffe()+""+t.getRealName()+""+t.getNatureStr());
//        }
//        for (Term t : parse) {
//            if(t.getNatureStr().equals("nr"))
//                System.out.println(t.getOffe()+""+t.getRealName()+""+t.getNatureStr());
//        }
//        for (Term t : parse) {
//            if(t.getNatureStr().equals("ul"))
//                System.out.println(t.getOffe()+""+t.getRealName()+""+t.getNatureStr());
//        }
//        for (Term t : parse) {
//            if(t.getNatureStr().equals("nw"))
//                System.out.println(t.getOffe()+""+t.getRealName()+""+t.getNatureStr());
//        }
//        for (Term t : parse) {
//            if(t.getNatureStr().equals("uj"))
//                System.out.println(t.getOffe()+""+t.getRealName()+""+t.getNatureStr());
//        }
//        for (Term t : parse) {
//            if(t.getNatureStr().equals("p"))
//                System.out.println(t.getOffe()+""+t.getRealName()+""+t.getNatureStr());
//        }
//        for (Term t : parse) {
//            if(t.getNatureStr().equals("g"))
//                System.out.println(t.getOffe()+""+t.getRealName()+""+t.getNatureStr());
//        }
//        for (Term t : parse) {
//            if(t.getNatureStr().equals("l"))
//                System.out.println(t.getOffe()+""+t.getRealName()+""+t.getNatureStr());
//        }
//        for (Term t : parse) {
//            if(t.getNatureStr().equals("c"))
//                System.out.println(t.getOffe()+""+t.getRealName()+""+t.getNatureStr());
//        }
//        for (Term t : parse) {
//            if(t.getNatureStr().equals("f"))
//                System.out.println(t.getOffe()+""+t.getRealName()+""+t.getNatureStr());
//        }
//        for (Term t : parse) {
//            if(t.getNatureStr().equals("ns"))
//                System.out.println(t.getOffe()+""+t.getRealName()+""+t.getNatureStr());
//        }

    }

    /**
     * 动态添加删除用户自定义词典!
     *
     * @author ansj
     */
    @Test
    public void addDic() {

        // 增加新词,中间按照'\t'隔开；有用词典
        UserDefineLibrary.insertWord("ansj中文分词", "userDefine", 1000);
        List<Term> terms = ToAnalysis.parse("我觉得Ansj中文分词是一个不错的系统!我是王婆!").getTerms();
        System.out.println("增加新词例子:" + terms);
        // 删除词语,只能删除.用户自定义的词典.
//        UserDefineLibrary.removeWord("ansj中文分词");
//        terms = ToAnalysis.parse("我觉得ansj中文分词是一个不错的系统!我是王婆!").getTerms();
//        System.out.println("删除用户自定义词典例子:" + terms);


    }

    /**
     * 数据库初始化
     *
     * @Author yaz
     * @Date 2016/12/12 14:31
     */
    @Test
    public void addStopLib() {
        File file = new File("E://stop.txt");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader StopWordFileBr = new BufferedReader(new InputStreamReader(fileInputStream));
            String stopWord = null;
            for (; (stopWord = StopWordFileBr.readLine()) != null; ) {
                StopLibrary stopLibrary = new StopLibrary();
                stopLibrary.setWord(stopWord.trim());
                stopLibrary.setCreateDate(new Date());
                stopLibraryService.saveEntity(stopLibrary);
            }
            StopWordFileBr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
