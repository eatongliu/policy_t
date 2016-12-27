package com.gpdata.wanyou.azkaban;

import net.sf.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Azkaban 上传文件
 * Created by yangjing on 2016/10/28.
 */
public class FileUploadZip {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadZip.class);

    /**
     * 上传文件
     *
     * @param url
     * @param projectname
     * @param filename
     * @param sessionid
     */
    public static String uploadFile(String url, String projectname, String filename, String sessionid) {
        HttpClient httpclient = new DefaultHttpClient();
        String result = "";
        try {
            //请求处理页面
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("Cache-Control", "max-age=0");

            //创建待处理的文件
            FileBody file = new FileBody(new File(filename));
            //创建待处理的表单域内容文本
            StringBody sessionIdStr = new StringBody(sessionid);
            StringBody ajaxStr = new StringBody("upload");
            StringBody projectStr = new StringBody(projectname);

            FileBody fileBody = new FileBody(new File(filename));

            //对请求的表单域进行填充
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .setCharset(CharsetUtils.get("iso-8859-1"))
                    .addPart("session.id", sessionIdStr)
                    .addPart("ajax", ajaxStr)
                    .addPart("project", projectStr)
                    .addPart("file", fileBody)
                    .build();

            //设置请求
            httppost.setEntity(reqEntity);
            //执行
            HttpResponse response = httpclient.execute(httppost);
            Header[] headers = response.getAllHeaders();
            System.out.println("==============header============");
            for (Header header : headers) {
                System.out.println(header.toString());
            }

            System.out.println("statusCode：" + response.getStatusLine().getStatusCode());

            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity entity = response.getEntity();
                //显示内容
                if (entity != null) {
                    System.out.println("显示内容");
                    result = EntityUtils.toString(entity);
                    System.out.println(result);

                }
                if (entity != null) {
                    System.out.println("非空");
                    entity.consumeContent();

                }

            }
            return result;
        } catch (Exception e) {
            logger.error("Exception:{}", e);
        }
        return null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        //注册，获得sessionid
        String loginUrl = "http://192.168.1.106:8081";
        String params = "action=login&username=azkaban&password=azkaban";
//        String sessionid = HttpURLConnectionPost.readContentFromPost(loginUrl, params);

        try {
            AzkabanOperator azkabanOperator = new AzkabanOperator();
            JSONObject json = azkabanOperator.login();
            String sessionid = json.get("session.id").toString();
            System.out.println("sessionid：" + sessionid);
            // JSONObject json2 = azkabanOperator.creatProject(sessionid, "demo" + sessionid.substring(0, 3), "demo1");

            //上传
            String filename = "D:\\dtsupload\\azfile\\baby.zip";
            String uploadUrl = "http://192.168.1.106:8081/manager";
            String projectname = "demo749";
            FileUploadZip.uploadFile(uploadUrl, projectname, filename, sessionid);
        } catch (Exception e) {
            logger.error("Exception:{}", e);
        }

    }

}
