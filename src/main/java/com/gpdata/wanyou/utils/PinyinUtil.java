package com.gpdata.wanyou.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by guoxy on 2016/10/31.
 */
public class PinyinUtil {
    private static final Logger logger = LoggerFactory.getLogger(PinyinUtil.class);

    /**
     * 转拼音
     *
     * @param args
     */

    public static String getPin(String src) {
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        // 设置汉语拼音输入格式
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // 判断能否为汉字字符
                // System.out.println(t1[i]);
                if (Character.toString(t1[i]).matches("[\u4e00-\u9fa5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// 将汉字的几种拼音都存到t2数组中
                    t4 += t2[0];// 取出该汉字全拼的第一种读音并连接到字符串t4后
                } else {
                    // 如果不是汉字字符，间接取出字符并连接到字符串t4后
                    t4 += Character.toString(t1[i]);

                }
            }
        } catch (Exception e) {
            logger.error("Exception", e);
            // TODO: handle exception
        }
        return t4;

    }

    /**
     * 提取每个汉字的首字母
     *
     * @param str
     * @return String
     */
    public static String getPinHead(String str) {
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            // 提取汉字的首字符
            String[] pinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinArray != null) {
                convert += pinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }

    /**
     * 提取每个名字的 大写首字母
     *
     * @param str
     * @return String
     */
    public static String getNamePinHead(String str) {
        String convert = "";
        // 设置汉语拼音输入格式
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        // for(int j=0;j<str.length();j++){
        char word = str.charAt(0);
        // 提取名字的首字符
        String[] pinArray;
        try {
            pinArray = PinyinHelper.toHanyuPinyinStringArray(word, t3);
            if (pinArray != null) {
                convert += pinArray[0].charAt(0);
            } else {
                convert += word;
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            logger.error("Exception", e);
        }

        // }
        return convert;
    }

    /**
     * 将字符串转换成ASCII码
     *
     * @param cnStr
     * @return String
     */
    public static String getASCII(String cnStr) {
        StringBuffer strb = new StringBuffer();
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            // 将每个字符转换成ASCII
            strb.append(Integer.toHexString(bGBK[i] & 0xff) + " ");
        }
        return strb.toString();
    }
}
