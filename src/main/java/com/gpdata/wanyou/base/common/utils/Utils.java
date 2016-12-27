/**
 * Project: traffic
 * Date: 2011-12-13
 */
package com.gpdata.wanyou.base.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * @author Alex Liu
 */
public class Utils {

    public static final String SALT = "iLoveAnySHARE2012@LEN0V0,C0M";
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
    private static char[] chars;
    private static int TO_UPPER = 'a' - 'A';

    static {
        StringBuilder builder = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ch++) {
            builder.append(ch);
        }
        for (char ch = 'a'; ch <= 'z'; ch++) {
            builder.append(ch);
        }
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            builder.append(ch);
        }
        chars = builder.toString().toCharArray();
    }

    public static String toHex(byte value) {
        int unsignedInt = value < 0 ? 256 + value : value;
        return StringUtils.leftPad(Integer.toHexString(unsignedInt), Byte.SIZE / 4, '0');
    }

    public static String toHex(int value) {
        return StringUtils.leftPad(Integer.toHexString(value), Integer.SIZE / 4, '0');
    }

    public static String toHex(long value) {
        return StringUtils.leftPad(Long.toHexString(value), Long.SIZE / 4, '0');
    }

    public static String toHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(toHex(b));
        }
        return builder.toString();
    }

    public static String underlineToCamelString(String string) {
        if (StringUtils.isBlank(string) || !string.contains("_"))
            return string;

        String[] strings = StringUtils.split(string, "_");
        StringBuilder builder = new StringBuilder();
        for (String str : strings) {
            if (StringUtils.isBlank(str))
                continue;
            String value;
            if (builder.length() == 0) {
                value = str.toLowerCase();
            } else {
                value = WordUtils.capitalizeFully(str);
            }
            builder.append(value);
        }

        return builder.toString();
    }

    public static String underlineToCapitalizeFully(String string) {
        if (StringUtils.isBlank(string) || !string.contains("_"))
            return WordUtils.capitalizeFully(string);

        String[] strings = StringUtils.split(string, "_");
        StringBuilder builder = new StringBuilder();
        for (String str : strings) {
            if (StringUtils.isBlank(str))
                continue;
            String value = WordUtils.capitalizeFully(str);
            builder.append(value);
        }

        return builder.toString();
    }

    public static String toUnderline(String string) {
        if (string == null)
            return null;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (CharUtils.isAsciiAlphaUpper(ch)) {
                ch = (char) (ch + TO_UPPER);
                if (i > 0) {
                    builder.append("_");
                }
            }
            builder.append(ch);
        }
        return builder.toString();
    }


    public static String randomString(int len) {
        return RandomStringUtils.random(len, chars);
    }

    public static String pahtConcat(String... paths) {
        StringBuilder builder = new StringBuilder();
        String separator = File.separator;
        String searchSeparator = File.separatorChar == '/' ? "\\" : "/";
        boolean lastWithSeparator = false;
        for (int i = 0, len = paths.length; i < len; i++) {
            String path = StringUtils.replace(paths[i], searchSeparator, separator);
            if (i > 0) {
                boolean firstWithSeparator = StringUtils.startsWith(path, separator);
                if (lastWithSeparator) {
                    if (firstWithSeparator) {
                        path = StringUtils.removeStart(path, separator);
                    }
                } else {
                    if (!firstWithSeparator) {
                        path = separator + path;
                    }
                }
            }
            builder.append(path);
            lastWithSeparator = StringUtils.endsWith(path, separator);
        }
        return builder.toString();
    }

    public static String urlConcat(String... urlPaths) {
        StringBuilder builder = new StringBuilder();
        boolean lastWithSeparator = false;
        String separator = "/";
        for (int i = 0, len = urlPaths.length; i < len; i++) {
            String path = StringUtils.replace(urlPaths[i], "\\", separator);
            if (i > 0) {
                boolean firstWithSeparator = StringUtils.startsWith(path, separator);
                if (lastWithSeparator) {
                    if (firstWithSeparator) {
                        path = StringUtils.removeStart(path, separator);
                    }
                } else {
                    if (!firstWithSeparator) {
                        path = separator + path;
                    }
                }
            }
            builder.append(path);
            lastWithSeparator = StringUtils.endsWith(path, separator);
        }
        return builder.toString();
    }

    public static final String getLocalRootPath() {
        String root = Utils.class.getResource("/").getFile();
        try {
            root = new File(root).getParentFile().getParentFile().getCanonicalPath();
            root += File.separator;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return root;
    }

    public static final boolean verifyAuth(HttpServletRequest request) {
        String value = request.getHeader("Authorization");
        if (value == null || !value.startsWith("BASIC ")) {
            return false;
        }

        value = value.replace("BASIC ", "");
        byte[] bytes = Base64.decodeBase64(value);
        value = new String(bytes);
        String[] fields = StringUtils.split(value, ':');
        if (fields == null || fields.length != 2) {
            return false;
        }

        String hash = HashUtils.hash(fields[0] + SALT);
        return hash == null ? false : hash.equals(fields[1]);
    }

    public static byte[] toBytes(long value) {
        byte[] result = new byte[Long.SIZE / Byte.SIZE];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) value;
            value >>= Byte.SIZE;
        }
        return result;
    }

    public static String displayDuration(int second) {
        int hour = second / 60 / 60;
        int minute = (second / 60) % 60;
        second = second % 60;
        return String.format("%d:%02d:%02d", hour, minute, second);
    }

    public static void main(String[] args) {
        LOGGER.debug(pahtConcat("/Data/SRC/", "\\d31p\\333", "file.txt"));
        LOGGER.debug(urlConcat("/Data/SRC/", "\\d31p\\333", "file.txt"));
        LOGGER.debug("root path: " + getLocalRootPath());
    }
}

