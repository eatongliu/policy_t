/**
 *
 */
package com.gpdata.wanyou.base.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author liugang2@lenovo.com
 */
public abstract class HashUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
    private static final String DIGEST_NAME = "MD5";
    private static final String SALT = "AnyE2E=mc^2@1en0vO";

    private static final int BUFFER_SIZE = 1024 * 8;
    private static final long MAX_FILE_LEN = 1024L * 1024 * 10;
    private static final int BLOCK_COUNT = 4;
    private static MessageDigest messageDigest;

    private HashUtils() {
    }

    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        long highBites = uuid.getMostSignificantBits();
        long lowBites = uuid.getLeastSignificantBits();
        return Utils.toHex(highBites) + Utils.toHex(lowBites);
    }

    private synchronized static MessageDigest getMessageDigest() {
        if (messageDigest == null) {
            try {
                messageDigest = MessageDigest.getInstance(DIGEST_NAME);
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return messageDigest;
    }

    public static String hashToString(File file) {
        return Utils.toHex(hash(file));
    }

    public static byte[] hash(File file) {
        try {
            return newHash((MessageDigest) getMessageDigest().clone(), file);
        } catch (CloneNotSupportedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    private static byte[] hash(MessageDigest digest, File file) {
        BufferedInputStream input = null;
        try {
            long start = System.currentTimeMillis();

            input = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024 * 4];
            int r, sum = 0;
            while ((r = input.read(buffer)) != -1) {
                digest.update(buffer, 0, r);
                sum += r;
            }

            return digest.digest();
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }

        return null;
    }

    public static String hashToString(byte[] bytes) {
        return Utils.toHex(hash(bytes));
    }

    public static byte[] hash(byte[] bytes) {
        MessageDigest md;
        try {
            md = (MessageDigest) getMessageDigest().clone();
        } catch (CloneNotSupportedException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
        md.update(bytes);
        return md.digest();
    }

    public static String hash(String string) {
        try {
            return Utils.toHex(hash(string.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public static String hashPassword(String string) {
        try {
            String value = string + SALT;
            return Utils.toHex(hash(value.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    private static byte[] newHash(MessageDigest digest, File file) {
        RandomAccessFile raf = null;
        long sum = 0;
        long start = System.currentTimeMillis();
        try {
            long fileLength = file.length();
            digest.update(Utils.toBytes(fileLength));

            raf = new RandomAccessFile(file, "r");

            int count = 1;
            long blockSize = fileLength;
            long step = 0;
            if (fileLength > MAX_FILE_LEN) {
                count = BLOCK_COUNT;
                blockSize = MAX_FILE_LEN / count;
                step = (fileLength - MAX_FILE_LEN) / (count - 1);
            }

            long startIndex = 0;
            for (int i = 0; i < count; i++) {
                long r = updateHash(digest, raf, startIndex, blockSize);
                startIndex += blockSize + step;
            }
            return digest.digest();
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
            LOGGER.info(sum + " bytes file hash -> " + (System.currentTimeMillis() - start) / 1000.0 + " s.");
        }

        return null;
    }

    private static long updateHash(MessageDigest digest, RandomAccessFile raf, long start, long length) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        long sum = 0;
        raf.seek(start);
        int r, len = (int) Math.min(BUFFER_SIZE, length - sum);
        while (len > 0 && (r = raf.read(buffer, 0, len)) != -1) {
            digest.update(buffer, 0, r);

            sum += r;
            start += r;
            len = (int) Math.min(BUFFER_SIZE, length - sum);
        }
        return sum;
    }

    public static void main(String[] args) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("", "d41d8cd98f00b204e9800998ecf8427e");
        map.put("a", "0cc175b9c0f1b6a831c399e269772661");
        map.put("abc", "900150983cd24fb0d6963f7d28e17f72");
        map.put("message digest", "f96b697d7cb7938d525a2f31aaf161d0");
        map.put("abcdefghijklmnopqrstuvwxyz", "c3fcd3d76192e4007dfb496cca67e13b");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String md5 = hash(key);
            LOGGER.debug(String.format("key['%s'] md5['%s'] is %s", key, md5, value.equalsIgnoreCase(md5) ? "OK" : "NO!"));
        }
    }
}

