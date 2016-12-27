package com.gpdata.wanyou.azkaban;

/**
 * 以下是使用Java模拟https请求的代码：
 * Created by guoxy on 2016/10/11.
 */

import com.gpdata.wanyou.utils.ConfigUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.www.protocol.http.HttpURLConnection;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

public class AzkabanHttpsPost {
    private static final Logger logger = LoggerFactory.getLogger(AzkabanHttpsPost.class);
    private static String keystorePassword = ConfigUtil.getConfig("keystore.password");
    private static String keystore = ConfigUtil.getConfig("azkaban.keystore");
    private static String truststore = ConfigUtil.getConfig("azkaban.truststore");


    /**
     * 获得KeyStore.
     *
     * @param storePath 密钥库路径
     * @param password  密码
     * @return 密钥库
     * @throws Exception
     */
    public static KeyStore getKeyStore(String password, String storePath)
            throws Exception {
        // 实例化密钥库
        KeyStore ks = KeyStore.getInstance("JKS");
        // 获得密钥库文件流
        FileInputStream is = new FileInputStream(storePath);
        // 加载密钥库
        ks.load(is, password.toCharArray());
        // 关闭密钥库文件流
        is.close();
        return ks;
    }

    /**
     * 获得SSLSocketFactory.
     *
     * @param password       密码
     * @param keyStorePath   密钥库路径
     * @param trustStorePath 信任库路径
     * @return SSLSocketFactory
     * @throws Exception
     */
    public static SSLContext getSSLContext() throws Exception {
        // 实例化密钥库
        KeyManagerFactory keyManagerFactory = KeyManagerFactory
                .getInstance(KeyManagerFactory.getDefaultAlgorithm());
        // 获得密钥库
        KeyStore keyStore = getKeyStore(AzkabanHttpsPost.keystorePassword, AzkabanHttpsPost.keystore);
        // 初始化密钥工厂
        keyManagerFactory.init(keyStore, AzkabanHttpsPost.keystorePassword.toCharArray());
        // 实例化信任库
        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        // 获得信任库
        KeyStore trustStore = getKeyStore(AzkabanHttpsPost.keystorePassword, AzkabanHttpsPost.truststore);
        // 初始化信任库
        trustManagerFactory.init(trustStore);
        // 实例化SSL上下文
        SSLContext ctx = SSLContext.getInstance("TLS");
        // 初始化SSL上下文
        ctx.init(keyManagerFactory.getKeyManagers(),
                trustManagerFactory.getTrustManagers(), null);
        // 获得SSLSocketFactory
        return ctx;
    }

    /**
     * 初始化HttpsURLConnection.
     *
     * @param password       密码
     * @param keyStorePath   密钥库路径
     * @param trustStorePath 信任库路径
     * @throws Exception
     */
    public static void initHttpsURLConnection() throws Exception {
        // 声明SSL上下文
        SSLContext sslContext = null;
        // 实例化主机名验证接口
        HostnameVerifier hnv = new MyHostnameVerifier();
        try {
            sslContext = getSSLContext();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        if (sslContext != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
                    .getSocketFactory());
        }
        HttpsURLConnection.setDefaultHostnameVerifier(hnv);
    }

    /**
     * 发送请求.
     *
     * @param httpsUrl 请求的地址,如https://localhost:8043
     * @param xmlStr   请求的数据,如action=login&username=azkaban&password=azkaban
     * @throws Exception
     */
    public static JSONObject post(String url, String xmlStr) throws Exception {
        initHttpsURLConnection();
        JSONObject jsonObj = null;
        /*目前使用http非https*/
        //HttpsURLConnection urlCon = null;
        HttpURLConnection urlCon = null;
        try {
            urlCon = (HttpURLConnection) (new URL(url)).openConnection();
            urlCon.setDoInput(true);
            urlCon.setDoOutput(true);
            urlCon.setRequestMethod("POST");

            // 如下设置后，azkaban才能识别出是以ajax的方式访问，从而返回json格式的操作信息
            urlCon.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            urlCon.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            urlCon.setUseCaches(true);
            // 设置为gbk可以解决服务器接收时读取的数据中文乱码问题
            urlCon.getOutputStream().write(xmlStr.getBytes("gbk"));
            urlCon.getOutputStream().flush();
            urlCon.getOutputStream().close();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    urlCon.getInputStream()));
            String line = "";
            String temp;
            while ((temp = in.readLine()) != null) {
                line = line + temp;
            }
            jsonObj = JSONObject.fromObject(line);
        } catch (MalformedURLException e) {
            logger.error("Exception:{}", e);
        } catch (IOException e) {
            logger.error("Exception:{}", e);
        } catch (Exception e) {
            logger.error("Exception:{}", e);
        }
        return jsonObj;
    }

}
