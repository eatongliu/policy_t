package com.gpdata.wanyou.utils;

/**
 * 参数处理工具类
 *
 * @author qyl
 */
public class ArgumentsUtil {

    /**
     * 对未传字符型参数设置为空
     *
     * @param string
     * @return
     */
    public static String setNull(String string) {
        if (string == null)
            string = "";
        return string;
    }

    /**
     * 对未传整型参数设置为-1
     *
     * @param string
     * @return
     */
    public static String setMinus(String string) {
        if (string == null)
            string = "-1";
        return string;
    }


}
