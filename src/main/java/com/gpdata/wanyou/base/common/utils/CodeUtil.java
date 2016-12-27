package com.gpdata.wanyou.base.common.utils;

import java.util.Random;

public class CodeUtil {
    /**
     * 生成随机字符
     *
     * @param length 随机字符长度
     * @return
     */
    public static String getCode(int length) { // length表示生成字符串的长度
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
