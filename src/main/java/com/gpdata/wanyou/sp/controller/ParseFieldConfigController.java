package com.gpdata.wanyou.sp.controller;

import com.alibaba.fastjson.JSONObject;
import com.gpdata.wanyou.sp.entity.ParseFieldConfig;
import com.gpdata.wanyou.sp.util.ParseNutchXmlUtil;
import com.gpdata.wanyou.utils.beans.SimpleInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 4.4.2解析字段配置
 * <p>
 * Created by chengchao on 16-10-13.
 */
@Controller
@RequestMapping
public class ParseFieldConfigController {


    /**
     * path : /sp/ps/gfd
     * 说明：获取页面解析字段配置XML文件的内容
     * 页面解析字段配置列表以XML的形式保存在/config/{爬虫标识}/parse.xml中。
     * * 参数1：spiderid 爬虫id（必填）
     * * 参数2：id 页面解析配置id（必填）
     * * 参数3：caption 标题（必填）
     * 成功：获取指定节中 caption、name、xpath、type、remark 值，以JSON形式返回
     * 失败：[“error”:”错误原因”]
     *
     * @return
     */
    @RequestMapping(value = ""
            , consumes = "application/json"
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String retrieveFieldConfig(@RequestBody ParseFieldConfig parseFieldConfig) {

        if (StringUtils.isAnyBlank(parseFieldConfig.getSpiderid()
                , parseFieldConfig.getId(), parseFieldConfig.getCaption())) {

            return SimpleInfo.errorOf("参数不完整, spiderid, id, caption 缺一不可");
        }

        String result = "{}";
        try {

            ParseFieldConfig bean = ParseNutchXmlUtil.retrieveParseFieldConfig(parseFieldConfig);
            result = JSONObject.toJSONString(bean);

        } catch (Exception e) {
            e.printStackTrace();
            result = SimpleInfo.errorOf(e.getMessage());
        }

        return result;
    }


    /**
     * path : /sp/ps/afd
     * 说明：新增页面解析字段配置XML文件的内容
     * * 参数1：spiderid 爬虫id（必填）
     * * 参数2：id 页面解析配置id（必填）
     * * 参数3：caption 标题（必填）
     * 参数4：name 字段名
     * 参数5：xpath  XPATH路径
     * 参数6：type 字段类型
     * 参数7：remark 备注
     * 成功：success
     * 失败：[“error”:”错误原因”]
     *
     * @return
     */
    public String addFieldConfig() {

        return null;
    }


    /**
     * path : /sp/ps/ufd
     * 说明：修改页面解析字段配置XML文件的内容
     * * 参数1：spiderid 爬虫id（必填）
     * * 参数2：id 页面解析配置id（必填）
     * * 参数3：caption 标题（必填）
     * 参数4：name字段名
     * 参数5：xpath  XPATH路径
     * 参数6：type 字段类型
     * 参数6：remark 备注
     * 成功：success
     * 失败：[“error”:”错误原因”]
     *
     * @return
     */
    public String updateFieldConfig() {

        return null;
    }


    /**
     * path : /sp/ps/dfd
     * 说明：删除页面解析字段配置XML文件的内容
     * * 参数1：spiderid 爬虫id（必填）
     * * 参数2：id 页面解析配置id（必填）
     * * 参数3：caption 标题（必填）
     * 成功：success
     * 失败：[“error”:”错误原因”]
     *
     * @return
     */
    public String deleteFieldConfig() {

        return null;
    }


    /**
     * path : /sp/ps/qfd
     * 说明：查询页面解析字段配置XML文件的内容
     * * 参数1：spiderid 爬虫id（必填）
     * * 参数2：id 页面解析配置id（必填）
     * 参数3：caption 标题
     * 成功：根据s piderid、id 和 caption 匹配 parse.xml内容，拼接 caption、name、xpath、type、remark 的值，并拼接为JSON
     * 失败：[“error”:”错误原因”]
     *
     * @return
     */
    public String queryFieldConfig() {

        return null;
    }
}
