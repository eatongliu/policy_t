package com.gpdata.wanyou.utils;

import org.junit.Test;

import java.io.*;

/**
 * Created by acer_liuyutong on 2016/12/12.
 */
public class IpUtilTest {
    @Test
    public void testGetAddressByIP(){
        String strIP = "210.12.46.5";
        String address = IpUtil.getAddressByIP(strIP);
        System.out.println(address);
    }

    @Test
    public void test2(){
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("1.txt")));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("2.txt")));

            String line = "";
            String temp = "";
            while((line = reader.readLine()) != null) {

                if (!temp.equals(line)){
                    writer.write(line + "\n");
                    writer.flush();
                }
                temp = line;
            }

        }catch (Exception e){

        }
    }
}