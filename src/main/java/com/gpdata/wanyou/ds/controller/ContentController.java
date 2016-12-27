package com.gpdata.wanyou.ds.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gpdata.wanyou.ds.entity.NewsDto;
import com.gpdata.wanyou.ds.service.ContentService;
import com.gpdata.wanyou.utils.beans.SimpleInfo;

@Controller
public class ContentController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ContentService contentService;

    /**
     * 向指定数据表中新增记录
     */
    @RequestMapping(value = "/ds/cn/a", method = RequestMethod.POST, consumes = "application/json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String addContent(@RequestBody NewsDto newsDto, HttpServletResponse response) {
        JSONObject json = new JSONObject();

        String tableid = newsDto.getTableid();
        if (StringUtils.isAnyBlank(tableid)) {
            return SimpleInfo.errorOf("参数不完整(需要 tableid)");
        }

        try {
            contentService.insertDB(newsDto);
            json.put("success", "success");
            return json.toString();
        } catch (Exception e) {
            logger.error("addContent(NewsDto, HttpServletResponse)", e);
            e.printStackTrace();
            json.put("error", e.getCause());
            return json.toString();
        }
    }

    /**
     * 清空表
     */
    @RequestMapping(value = "/ds/cn/d", consumes = "application/json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String clearContent(@RequestBody NewsDto newsDto, HttpServletResponse response) {
        JSONObject json = new JSONObject();

        String tableid = newsDto.getTableid();
        if (StringUtils.isAnyBlank(tableid)) {
            return SimpleInfo.errorOf("参数不完整(需要 tableid)");
        }

        try {
            contentService.clear(newsDto);
            json.put("success", "success");
            return json.toString();
        } catch (Exception e) {
            logger.error("clearContent(NewsDto, HttpServletResponse)", e);
            e.printStackTrace();
            json.put("error", e.getCause());
            return json.toString();
        }
    }
}