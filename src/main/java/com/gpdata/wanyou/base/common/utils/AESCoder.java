package com.gpdata.wanyou.base.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * @author chengchao
 */
public final class AESCoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(AESCoder.class);
    private static final String KEY_ALGORITHM = "AES";
    //AES/CBC/PKCS5Padding
    //AES/ECB/PKCS5Padding
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    // build the initialization vector.  This example is all zeros, but it
    // could be any value or generated using a random number generator.
    private static byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final IvParameterSpec ivspec = new IvParameterSpec(iv);


    private AESCoder() {
        // nothing
    }


    /**
     * 转换秘钥
     *
     * @param key
     * @return
     */
    private static Key toKey(byte[] key) {
        //
        SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);

        return secretKey;
    }

    /**
     * 解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key) throws Exception {

        // 还原秘钥
        Key k = toKey(key);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

        cipher.init(Cipher.DECRYPT_MODE, k, ivspec);

        return cipher.doFinal(data);
    }

    /**
     * 加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {

        // 还原秘钥
        Key k = toKey(key);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

        cipher.init(Cipher.ENCRYPT_MODE, k, ivspec);

        return cipher.doFinal(data);
    }


    private static byte[] stringToKeyByte(String input) {

        if (input == null) {
            throw new IllegalArgumentException("input 不可为空！");
        }

        byte[] key = new byte[16];
        int end = key.length;
        if (input.length() <= end) {
            end = input.length();
        }

        for (int i = 0; i < end; i++) {
            key[i] = (byte) input.charAt(i);
        }

        return key;
    }

    /**
     * 加密字符串，并转换成 Base64 格式
     *
     * @param input
     * @param key
     * @return
     */
    public static final String encryptStringToBase64(String input, String key) {

        try {
            byte[] k = stringToKeyByte(key);
            byte[] result = input.getBytes();
            result = encrypt(result, k);

            return Base64.encodeBase64String(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对加密并转存成 Base64 格式的字符串。
     * 先去 Base64 再解密。
     *
     * @param base64String
     * @param key
     * @return
     */
    public static final String decryptBase64ToString(String base64String, String key) {

        try {
            byte[] k = stringToKeyByte(key);
            byte[] result = Base64.decodeBase64(base64String);
            result = decrypt(result, k);
            return new String(result);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * @return
     * @throws Exception
     */
    private static byte[] initKey() throws Exception {

        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);

        kg.init(128);

        SecretKey secretKey = kg.generateKey();

        return secretKey.getEncoded();
    }

    public static void main(String[] args) {


        try {

            String inputStr = "A爱北爱北sdfsdfs京";
            String k = "我爱北京天安门我爱北京天安门sdfsfdsf我爱北京天安门";

            String ecc = encryptStringToBase64(inputStr, k);

            LOGGER.debug(ecc);
            String ecc2 = decryptBase64ToString(ecc, k);

            LOGGER.debug(new String(ecc2));

        } catch (Exception e) {
            LOGGER.error("{}", e);
        }
    }


    private static byte[] charTyByteArray(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);

        return b;
    }

    private static char byteArrayToChar(byte[] b) {
        char c = (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));

        return c;
    }
}

