package com.gpdata.wanyou.sp.controller;

import com.alibaba.fastjson.JSON;
import com.gpdata.wanyou.sp.entity.IPStrategy;
import com.gpdata.wanyou.sp.service.IPStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by guoxy on 2016/10/14.
 */
@Controller
@RequestMapping
public class IPStrategyController {
    @Autowired
    private IPStrategyService ipStrategyService;

    /**
     * 4.3.3.1浏览 /sp/ip/g
     * 说明：获取IP策略
     * 读取Nutch配置文件的内容，包括 代理服务器地址，代理服务器端口，代理服务器用户名，代理服务器密码；如果IP策略设置为不可用，
     * 则将配置信息保存到同名的.bak文件中。
     * 参数1：spiderid爬虫id（必填）
     * 成功：IP策略配置文件内容的JSON串
     * 失败：[“error”:”错误原因”]
     *
     * @param ipStrategy
     * @return
     */
    @RequestMapping(value = "/sp/ip/g"
            , consumes = "application/json"
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getIPStrategy(@RequestBody IPStrategy ipStrategy, HttpServletResponse response) {
        String result = ipStrategyService.getIPStrategy(ipStrategy);
        return JSON.toJSONString(result);
    }
    /**
     * 4.3.3.2更新 /sp/ip/u
     * 说明：更新IP策略XML
     * IP策略配置XML文件，保存在/config/{爬虫标识}文件夹中。
     * 参数1：spiderid爬虫id（必填）
     * 参数2：ipconfig内容（必填），传递格式为JSON，保存格式为XML，格式请参看《爬虫处理流程及界面.vsd》中“爬取源”
     * 成功：success
     * 失败：[“error”:”错误原因”]
     */
    /**
     * 如果IP策略设置为不可用，
     * 则将配置信息保存到同名的.bak文件中。
     */
    @RequestMapping(value = "/sp/ip/u"
            , consumes = "application/json"
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String setIPStrategy(@RequestBody IPStrategy ipStrategy, HttpServletResponse response) {
        String result = ipStrategyService.setIPStrategy(ipStrategy);
        return JSON.toJSONString(result);
    }

    /**
     * 如果IP策略设置为不可用，
     * 则将配置信息保存到同名的.bak文件中。
     * TODO
     *
     * @param ipStrategy
     */
    @RequestMapping(value = "/sp/ip/bk"
            , consumes = "application/json"
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String saveIPStrategy(IPStrategy ipStrategy, HttpServletResponse response) {

        /*TODO*/
        return null;
    }
}
