package com.gpdata.wanyou.sp.controller;

import com.alibaba.fastjson.JSON;
import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.flexible.FlexibleFileUtil;
import com.gpdata.wanyou.sp.entity.Spider;
import com.gpdata.wanyou.sp.service.SpiderBaseinfoService;
import com.gpdata.wanyou.utils.ArgumentsUtil;
import com.gpdata.wanyou.utils.ConfigUtil;
import com.gpdata.wanyou.utils.SimpleBeanPropertiesUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping()
public class SpiderBaseinfoController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpiderBaseinfoController.class);
    private static final String HDFS_URL = ConfigUtil.getConfig("HDFS.url");
    @Autowired
    private SpiderBaseinfoService spiderBaseinfoService;
    @Autowired
    private FlexibleFileUtil flexibleFileUtil;

    /**
     * 说明：在spider_baseinfo表中创建爬虫基本信息。
     * 参数1：taskid任务id（必填）
     * 参数2：caption标题
     * 参数3：name 标识
     * 参数4：creator创建人
     * 参数5：depth爬取深度
     * 参数6：threads线程数
     * 参数7：topn topn限制
     * 参数8：remark备注
     * 成功：[“id”:新增爬虫的id]
     * 失败：[“error”:”错误原因”]
     *
     * @return
     */
    @RequestMapping(value = "/sp/ba/a", consumes = "application/json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String addSpider(@RequestBody Spider spider, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            int spiderId = spiderBaseinfoService.addSpider(spider);
            json.put("id", spiderId);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            json.put("error", e.getCause());
            return json.toString();
        }

    }

    /**
     * 说明：修改spider_baseinfo表的爬虫基本信息
     * 参数1：spiderid爬虫id（必填）
     * 参数2：caption标题
     * 参数3：name标识
     * 参数4：depth爬取深度
     * 参数5：threads线程数
     * 参数6：topn topn限制
     * 参数7：remark备注
     * 成功：”success”
     * 失败：[“error”:”错误原因”]
     *
     * @return
     */
    @RequestMapping(value = "/sp/ba/u", consumes = "application/json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String updateSpider(@RequestBody Spider spider, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            Spider oldspider = (Spider) spiderBaseinfoService.getSpider(spider.getSpiderid(), 0).get(0);
            SimpleBeanPropertiesUtil.copyNotNullProperties(spider, oldspider);
            spiderBaseinfoService.updateSpider(oldspider);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            json.put("error", e.getCause());
            return json.toString();
        }

    }

    /**
     * 说明：删除某个爬虫信息
     * 参数1：spiderid爬虫id（必填）
     * 成功：删除表spider_baseinfo中指定spiderid的记录
     * 失败：[“error”:”错误原因”]
     *
     * @return
     */
    @RequestMapping(value = "/sp/ba/d", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteSpider(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            spiderBaseinfoService.deleteSpider(Integer.parseInt(request.getParameter("spiderid")));
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            json.put("error", e.getCause());
            return json.toString();
        }
    }


    /**
     * 说明：显示具体某个爬虫的详细信息。
     * 参数1：spiderid 爬虫id
     * 参数2：taskid 任务id
     * 成功：表spider_baseinfo中指定的记录
     * 如果taskid 有值而spiderid为空，表示获取当前任务的所有爬虫
     * 如果spiderid有值，表示获取该爬虫的详细信息
     * 失败：[“error”:”错误原因”]
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/sp/ba/g", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getSpider(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            List data = spiderBaseinfoService.getSpider(
                    Integer.parseInt(ArgumentsUtil.setMinus(request.getParameter("spiderid"))),
                    Integer.parseInt(request.getParameter("taskid")));
            String result = JSON.toJSONString(data);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            json.put("error", e.getCause());
            return json.toString();
        }
    }


    /**
     * 说明：查询指定任务的爬虫信息
     * 参数1：taskid任务ID
     * 参数2：caption标题
     * 成功：参数1精确匹配，参数2模糊匹配 的记录，拼接FieldID、FieldName字段JSON
     * 失败：[“error”:”错误原因”]
     *
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    @GetMapping(value = "/sp/ba/q", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String searchSpider(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            List result = spiderBaseinfoService.searchSpider(
                    Integer.parseInt(request.getParameter("taskid")),
                    ArgumentsUtil.setNull(request.getParameter("caption")));
            String resultJson = JSON.toJSONString(result);
            return resultJson;
        } catch (Exception e) {
            e.printStackTrace();
            return json.toString();
        }
    }


}
