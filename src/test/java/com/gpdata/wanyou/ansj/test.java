package com.gpdata.wanyou.ansj;


import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
  
  
public class test {  
    public static void main(String[] args) throws IOException {  
        String filePath = "C://Users//admin//Desktop/demoansj.txt";
        String tt=new String();  
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF8"));  
        String str;  
        while ((str = in.readLine()) != null) {  
            tt+=str;  
        }  
       test1(tt);  
         
       System.out.println("*************************");  
       //filePath = "./test1.txt";
       BufferedReader in2 = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF8"));  
       String str2;  
       String tt2=new String();  
       while ((str2 = in2.readLine()) != null) {  
            tt2+=str2;  
       }  
       test1(tt2);  
         
    }  
      
    public static void test1(String content){  
        KeyWordComputer key=new KeyWordComputer(10);
        Iterator it = key.computeArticleTfidf(content).iterator() ;  
        while(it.hasNext()) {  
            Keyword key2=(Keyword)it.next();
            System.out.println(key2.toString()+key2.getScore());      
        }    
    }  
}  