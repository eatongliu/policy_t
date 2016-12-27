package com.gpdata.wanyou.sp.controller;

import com.alibaba.fastjson.JSONObject;
import com.gpdata.wanyou.sp.entity.ParsePageConfig;
import com.gpdata.wanyou.sp.util.ParseNutchXmlUtil;
import com.gpdata.wanyou.utils.beans.SimpleInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 4.4.1页面配置
 * <p>
 * Created by chengchao on 16-10-13.
 */
@Controller
@RequestMapping
public class ParsePageConfigController {


    /**
     * page : /sp/ps/gpg
     * 说明：获取页面解析配置XML文件的内容
     * 页面解析配置列表以XML的形式保存在/config/{爬虫标识}/parse.xml中。
     * * 参数1：spiderid 爬虫id（必填）
     * * 参数2：id 页面解析配置id（必填）
     * 成功：获取指定节中id、caption、designurl、parseurl、tableid值，以JSON形式返回
     * 失败：[“error”:”错误原因”]
     *
     * @return
     */
    @RequestMapping(value = "/sp/ps/gpg/{spiderid}/{id}"
            , method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String retrievePageConfig(HttpServletRequest request
            , @PathVariable(value = "spiderid") String spiderid
            , @PathVariable(value = "id") String id) {


        ParsePageConfig parsePageConfig = new ParsePageConfig();

        parsePageConfig.setSpiderid(spiderid);
        parsePageConfig.setId(id);

        return retrievePageConfig(request, parsePageConfig);
    }

    /**
     * 另一个调用方法 ...
     *
     * @param parsePageConfig
     * @return
     */
    @RequestMapping(value = "/sp/ps/gpg"
            , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String retrievePageConfig(HttpServletRequest request
            , @RequestBody ParsePageConfig parsePageConfig) {

        String spiderid = parsePageConfig.getSpiderid();
        String id = parsePageConfig.getId();

        if (StringUtils.isAnyBlank(spiderid, id)) {
            return SimpleInfo
                    .errorOf("参数不完整(需要 spiderid 和 id )");
        }

        Map<String, String> result = ParseNutchXmlUtil.retrieveParsePageConfig(spiderid, id);
        if (result != null) {
            result.put("spiderid", spiderid);
        }

        return JSONObject.toJSONString(result);
    }

    /**
     * page : /sp/ps/apg
     * 说明：新增页面解析配置XML文件的内容
     * * 参数1：spiderid 爬虫id（必填）
     * 参数2：caption 标题
     * 参数3：designurl 设计时URL
     * 参数4：parseurl 页面解析匹配
     * 参数5：tableid 目标数据源表ID
     * 成功：[“id”:新增页面解析的id]
     * 失败：[“error”:”错误原因”]
     *
     * @return
     */
    @RequestMapping(value = "/sp/ps/apg"
            , method = RequestMethod.POST
            , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String savePageConfig(@RequestBody ParsePageConfig parsePageConfig) {


        try {
            String id = ParseNutchXmlUtil.addPageConfig(parsePageConfig);
            return SimpleInfo.idOf(id);
        } catch (Exception e) {
            return SimpleInfo.errorOf(e.getMessage());
        }


    }


    /**
     * path : /sp/ps/upg
     * 说明：修改页面解析配置XML文件的内容
     * * 参数1：spiderid 爬虫id（必填）
     * * 参数2：id 页面解析配置id（必填）
     * 参数3：caption 标题
     * 参数4：designurl 设计时URL
     * 参数5：parseurl 页面解析匹配
     * 参数6：tableid 目标数据源表ID
     * 成功：success
     * 失败：[“error”:”错误原因”]
     *
     * @return
     */
    @RequestMapping(value = "/sp/ps/upg"
            , method = RequestMethod.POST
            , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String updatePageConfig(@RequestBody ParsePageConfig parsePageConfig) {

        try {
            ParseNutchXmlUtil.updatePageConfig(parsePageConfig);
        } catch (Exception e) {
            return SimpleInfo.errorOf(e.getMessage());
        }
        return "success";

    }

    /**
     * path : /sp/ps/dpg
     * 说明：删除页面解析配置XML文件的内容
     * * 参数1：spiderid 爬虫id（必填）
     * * 参数2：id 页面解析配置id（必填）
     * 成功：success
     * 失败：[“error”:”错误原因”]
     *
     * @return
     */
    @RequestMapping(value = "/sp/ps/dpg"
            , method = RequestMethod.POST
            , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deletePageConfig(ParsePageConfig parsePageConfig) {

        try {
            ParseNutchXmlUtil.deletePageConfig(parsePageConfig);
        } catch (Exception e) {
            return SimpleInfo.errorOf(e.getMessage());
        }
        return "success";

    }

    /**
     * path : /sp/ps/qpg
     * 说明：查询页面解析配置XML文件的内容
     * * 参数1：spiderid 爬虫id（必填）
     * 参数2：caption 标题
     * 成功：根据spiderid和caption匹配parse.xml内容，拼接id、caption、designurl、parseurl、tableid的值，并拼接为JSON
     * 失败：[“error”:”错误原因”]
     *
     * @return
     */
    @RequestMapping(value = "/sp/ps/qpg"
            , method = RequestMethod.POST
            , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String queryPageConfig(@RequestBody ParsePageConfig parsePageConfig) {

        String spiderid = parsePageConfig.getSpiderid();

        if (StringUtils.isAnyBlank(spiderid)) {
            return SimpleInfo.errorOf("spiderid 不存在!");
        }

        try {
            List<ParsePageConfig> resultList = ParseNutchXmlUtil.queryPageConfig(parsePageConfig);
            return JSONObject.toJSONString(resultList);

        } catch (Exception e) {
            return SimpleInfo.errorOf(e.getMessage());
        }
    }


    /**
     * path : /sp/ps/qpg
     * 说明：查询页面解析配置XML文件的内容
     * * 参数1：spiderid 爬虫id（必填）
     * 参数2：caption 标题
     * 成功：根据spiderid和caption匹配parse.xml内容，拼接id、caption、designurl、parseurl、tableid的值，并拼接为JSON
     * 失败：[“error”:”错误原因”]
     *
     * @return
     */
    @RequestMapping(value = "/sp/ps/qpg"
            , method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String queryPageConfig(String spiderid, String caption) {


        ParsePageConfig parsePageConfig = new ParsePageConfig();
        parsePageConfig.setCaption(caption);
        parsePageConfig.setSpiderid(spiderid);

        return this.queryPageConfig(parsePageConfig);
    }
}
