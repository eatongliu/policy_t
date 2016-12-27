package com.gpdata.wanyou.base.flexible;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public interface FlexibleFileUtil {


    public static final Pattern BACK_SLANT = Pattern.compile("\\\\");


    /**
     * 除了将出现的反斜杠 {@code '\\'} 替换成正斜杠 {@code '/'} <br />
     * 还把左右两部分拼成一个字符串并返回
     *
     * @param leftPartOfPath
     * @param rightPartOfPath
     * @return
     */
    public static String replaceToSlantAndConcat(String leftPartOfPath, String rightPartOfPath) {

        if (StringUtils.isBlank(leftPartOfPath) && StringUtils.isBlank(rightPartOfPath)) {
            return "";
        }
        int leftSize = leftPartOfPath != null ? leftPartOfPath.length() : 0;
        int rightSize = rightPartOfPath != null ? rightPartOfPath.length() : 0;

        StringBuilder buff = new StringBuilder(leftSize + rightSize)
                .append(leftPartOfPath)
                .append(rightPartOfPath);

        return BACK_SLANT.matcher(buff).replaceAll("/");
    }

    /**
     * 将出现的反斜杠 {@code '\\'} 替换成正斜杠 {@code '/'}
     * 返回替换后的字符串
     *
     * @param path
     * @return
     */
    public static String replaceToSlant(String path) {
        return replaceToSlantAndConcat(path, "");
    }

    /**
     * @param source
     * @param rightPartOfPath
     * @return
     */
    public void saveUploadFile(File source, String rightPartOfPath, FlexibleFileType type) throws IOException;

    /**
     * @param source
     * @param rightPartOfPath
     * @return
     */
    public void saveUploadFile(InputStream source, String rightPartOfPath, FlexibleFileType type) throws IOException;

    /**
     * @param key
     * @return
     */
    public void downloadResFile(String key, String localTargetPath) throws IOException;
}
