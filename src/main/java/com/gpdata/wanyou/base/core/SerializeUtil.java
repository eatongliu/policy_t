package com.gpdata.wanyou.base.core;

import java.io.*;


/**
 * 将一个对象序列化成 byte 数组;
 * 返过来,
 * 将一个已经被序列化成 byte 数组的对象转换成对象.
 */
public class SerializeUtil {

    public static byte[] serialize(Serializable object) {
        byte[] result = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
            result = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T unserialize(byte[] bytes) {
        T result = null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            result = (T) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
