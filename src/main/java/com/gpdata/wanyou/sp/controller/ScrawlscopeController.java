package com.gpdata.wanyou.sp.controller;

import com.gpdata.wanyou.sp.service.SpiderBaseinfoService;
import com.gpdata.wanyou.utils.XmlJsonUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping()
public class ScrawlscopeController {
    /**
     * 爬取源文件存放绝对路径
     */
    private static String SCRAWLSCOPE_ABSOLUTE_DIR = "E:/workspace-qyl/gp-spring-jpa-wanyou/src/main/webapp/files/";//爬取源文件存放绝对路径
    @Autowired
    private SpiderBaseinfoService spiderBaseinfoService;

    /**
     * /sp/rs/u
     * 说明：更新爬取源XML
     * 爬取源列表以XML的形式保存在/config/{爬虫标识}/scrawlscope.xml中。
     * 参数1：spiderid爬虫id（必填）
     * 参数2：scrawlscope内容（必填），传递格式为JSON，保存格式为XML，格式请参看《爬虫处理流程及界面.vsd》中“爬取源”
     * 成功：success
     * 失败：[“error”:”错误原因”]
     *
     * @return
     */
    @SuppressWarnings("resource")
    @RequestMapping(value = "/sp/rs/u", consumes = "application/json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String updateScrawlscope(@RequestBody Map<String, String> map, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            String url = SCRAWLSCOPE_ABSOLUTE_DIR + "config/" + map.get("spiderid");
            String filename = "scrawlscope.xml";
            File file = new File(url + filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            String xml = XmlJsonUtil.jsontoXml(map.get("scrawlscope"));// 将scrawlscope转换为XML
            byte b[] = xml.getBytes();
            try {
                fos.write(b);// 将byte数组写入到文件之中
            } catch (IOException e) {
                e.printStackTrace();
                json.put("error", e.getCause());
                return json.toString();
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            json.put("error", e.getCause());
            return json.toString();
        }

    }


    /**
     * /sp/rs/g
     * 说明：获取爬取源JSON
     * 爬取源列表以XML的形式保存在/config/{爬虫标识}/scrawlscope.xml中。
     * 参数1：spiderid爬虫id（必填）
     * 成功：爬取源的JSON内容
     * 失败：[“error”:”错误原因”]
     *
     * @return
     */
    @RequestMapping(value = "/sp/rs/g", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getScrawlscope(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            String url = SCRAWLSCOPE_ABSOLUTE_DIR + "config/" + request.getParameter("spiderid") + "/scrawlscope.xml";
            File xml = new File(url);
            json = XmlJsonUtil.xml2JSON(xml);// 将xml转换为JSON
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            json.put("error", e.getCause());
            return json.toString();
        }
    }


}
