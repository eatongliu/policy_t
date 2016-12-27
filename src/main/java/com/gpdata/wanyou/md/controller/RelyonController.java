package com.gpdata.wanyou.md.controller;

import com.alibaba.fastjson.JSONObject;
import com.gpdata.wanyou.md.entity.MetadataInfo;
import com.gpdata.wanyou.md.service.MetadataInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 3.2.6元数据依赖关系（血缘分析）
 *
 * @author wenjie
 */
@Controller
@RequestMapping
public class RelyonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelyonController.class);
    @Autowired
    private MetadataInfoService metadataInfoService;

    /**
     * 说明：显示具体某个元数据的依赖关系。
     * 参数：metadataid元数据id（必填）
     * 成功：表metadata_info中字段depmetadataid的内容
     * 失败：[“error”:”错误原因”]
     *
     * @param metadataid
     * @return
     */

    @SuppressWarnings("unused")
    @RequestMapping(value = "/md/dp/g"
            , consumes = "application/json"
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getMetadataInfo(HttpServletRequest request
            , @RequestBody MetadataInfo info) {
        LOGGER.debug("info : {}", info);
        Map<String, Object> errorInfo = new HashMap<>();

        FAIL_FAST:
        {
            int metadataid = info.getMetadataid();
            if (metadataid <= 0) {
                errorInfo.put("error", "metadataid 不存在或者不合理(<=0)");
                break FAIL_FAST;
            }
            try {
                MetadataInfo data = this.metadataInfoService.getById(metadataid);
                if (data == null) {
                    errorInfo.put("error", "数据不存在！");
                    break FAIL_FAST;
                }
                return JSONObject.toJSONString(data);
            } catch (Exception e) {
                LOGGER.error("Exception : {}", e.getCause());
                errorInfo.put("error", e.getMessage());
            }
        }
        return JSONObject.toJSONString(errorInfo);
    }

    /**
     * 3.2.6.2修改
     * 说明：修改元数据依赖关系
     * 参数1：metadataid元数据id（必填）
     * 参数2：depmetadataid依赖的元数据
     * 成功：”success”
     * 失败：[“error”:”错误原因”]
     *
     * @param metadataid
     * @param depmetadataid
     * @return
     */


    @RequestMapping(value = "/md/dp/u"
            , consumes = "application/json"
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String updateMetadataInfo(HttpServletRequest request
            , @RequestBody MetadataInfo info) {
        Date date = new Date();
        LOGGER.debug("info : {}", info);
        Map<String, Object> data = new HashMap<>();

        FAIL_FAST:
        {
            if (info.getMetadataid() <= 0) {
                data.put("error", "元数据id(metadataid)不存在或值不合理(<=0)");
                break FAIL_FAST;
            }
            if (info.getDepmetadataid() == null) {
                data.put("error", "依赖的元数据不合理或者为空");
                break FAIL_FAST;
            }
            try {
                MetadataInfo bean = metadataInfoService.getById(info.getMetadataid());
                if (bean == null) {
                    data.put("error", "数据不存在!");
                    break FAIL_FAST;
                }
                bean.setRevisedate(date);
                bean.setDepmetadataid(info.getDepmetadataid());
                this.metadataInfoService.update(bean);
                //成功返回
                return "success";
            } catch (Exception e) {
                LOGGER.error("Exception : {}", e.getCause());
                data.put("error", e.getMessage());
            }
        }
        return JSONObject.toJSONString(data);

    }

}
