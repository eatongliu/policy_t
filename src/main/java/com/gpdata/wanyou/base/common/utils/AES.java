package com.gpdata.wanyou.base.common.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private static final Logger LOGGER = LoggerFactory.getLogger(AES.class);

    /*
     * 将字符串AES加密
     * 加密顺序如下：
     * 先AES加密
     * 然后在base64编码
     * 然后去掉base64编码中的空格。
     */
    public static String AESEncrypt(String src, final String seed) {
        try {
            byte[] key = seed.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(key));
            byte[] encrypted = cipher.doFinal(src.getBytes("UTF-8"));
            String srcEncode = Base64.encode(encrypted);
            srcEncode = srcEncode.replaceAll("\\s", ""); //\s   A whitespace character: [ \t\n\x0B\f\r]
            //srcEncode = URLEncoder.encode(srcEncode,"UTF-8");
            return srcEncode;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * 将字符串AES解密
     * 解密顺序如下：
     * 先AES解密
     * 转换成Strng 返回
     */
    public static String AESDecrypt(String src, final String seed) {
        try {
            byte[] srcDecode = Base64.decode(src);
            byte[] key = seed.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(key));
            byte[] dencrypted = cipher.doFinal(srcDecode);
            return new String(dencrypted, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * 将字符串AES加密 加密顺序如下： 先编码seed为256为密钥 先AES加密 然后在base64编码 然后去掉base64编码中的空格。
	 */
    public static String AESToEncrypt(String src, final String seed) {
        try {
            byte[] key = asBin(seed);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(key));
            byte[] encrypted = cipher.doFinal(src.getBytes("UTF-8"));
            String srcEncode = Base64.encode(encrypted);
            srcEncode = srcEncode.replaceAll("\\s", ""); // \s A whitespace
            // character: [
            // \t\n\x0B\f\r]
            // srcEncode = URLEncoder.encode(srcEncode,"UTF-8");
            return srcEncode;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * 将字符串AES解密 解密顺序如下： 先AES解密 转换成Strng 返回
     */
    public static String AESToDecrypt(String src, final String seed) {
        try {
            byte[] srcDecode = Base64.decode(src);
            byte[] key = asBin(seed);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(key));
            byte[] dencrypted = cipher.doFinal(srcDecode);
            return new String(dencrypted, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * 转为十六进制
     */
    @SuppressWarnings("unused")
    private static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10)
                strbuf.append("0");
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        // LOGGER.debug(strbuf.toString());
        return strbuf.toString();
    }

    /*
     * 转为二进制
     */
    @SuppressWarnings("unused")
    private static byte[] asBin(String src) {
        if (src.length() < 1)
            return null;
        byte[] encrypted = new byte[src.length() / 2];
        for (int i = 0; i < src.length() / 2; i++) {
            int high = Integer.parseInt(src.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(src.substring(i * 2 + 1, i * 2 + 2), 16);
            encrypted[i] = (byte) (high * 16 + low);
        }
        return encrypted;
    }

    public static byte[] HexString2Bytes(String src) {
        byte[] ret = new byte[8];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < 8; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}))
                .byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}))
                .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    public static void main(String[] args) {
        try {
            String seed = "ccbde" + "18612981628" + "ccbde"
                    + new StringBuffer("18612981628").reverse().toString();
            LOGGER.debug(seed);
            String before = AES.AESToEncrypt("admin", seed);// 32位
            LOGGER.debug(before);// 加密
            String after = AES.AESToDecrypt(before, seed);
            LOGGER.debug(after);// 解密

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}