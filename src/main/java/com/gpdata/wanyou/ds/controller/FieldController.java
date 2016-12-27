package com.gpdata.wanyou.ds.controller;


import com.gpdata.wanyou.ds.entity.DataSourceField;
import com.gpdata.wanyou.ds.service.FieldService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据字段控制器，实现与数据字段有关的功能
 * @author quyili
 */
@Controller
@RequestMapping()
public class FieldController {

    private static final Logger logger = Logger.getLogger(FieldController.class);
    @Autowired
    private FieldService fieldService;


    /**
     * 查看选定数据字段详细信息
     * 说明：显示具体某个数据字段的详细信息。
     * 参数：fieldid字段ID（必填）
     * 成功：表datasource_field中指定FieldID的记录
     * 失败：[“error”:”错误原因”]
     *
     * @author quyili
     * @param map
     * @return
     */
    @RequestMapping(value = "/ds/{resourceId}/{tableId}/{fieldId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public  Map<String, Object> viewDataField(@PathVariable Integer fieldId) {

    	Map<String, Object> result = new HashMap<String, Object>();
        try {
            DataSourceField dataField = fieldService.getDataFieldById(fieldId);

            result.put("status", "SUCCESS");
            if (dataField != null) {
                result.put("data", dataField);
            } else {
                result.put("data", "查询结果为空");
            }
            return result;
        } catch (Exception e) {
            logger.error("查询异常，{}", e);
            result.put("status", "ERROR");
            if (e.getCause() != null) {
            	result.put("cause", e.getCause());
            } else {
            	result.put("cause", "查询异常，请联系管理员！");
            }
            return result;
        }
    }


    /**
     * 说明：查询检索该表的详细字段信息
     * 参数1：tableid数据表ID（必填）
     * 参数2：caption字段中文名
     * 参数3：fieldname字段名
     * 参数4：limit获取条数，默认20
     * 参数5：offset，从第几条开始，默认0
     * 成功：模糊匹配caption和fieldname的记录，拼接所有字段JSON
     * 失败：[“error”:”错误原因”]]
     *
     * @author quyili
     * @return
     */
    @RequestMapping(value = "/ds/{resourceId}/{tableId}/sch", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> getDataTableColumns(
            @PathVariable Integer tableId,
            @RequestParam(required = false, defaultValue = "") String caption,
            @RequestParam(required = false, defaultValue = "") String fieldName,
            @RequestParam(required = false, defaultValue = "20") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset) {

    	Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> tableColumns = fieldService.getDataTableColumns(tableId
                    , caption, fieldName, limit, offset);
            result.put("status", "SUCCESS");
            if (tableColumns != null) {
            	result.put("data", tableColumns);
            } else {
            	result.put("data", "查询结果为空");
            }
            return result;
        } catch (Exception e) {
            logger.error("查询异常，{}", e);
            result.put("status", "ERROR");
            if (e.getCause() != null) {
            	result.put("cause", e.getCause());
            } else {
            	result.put("cause", "查询异常，请联系管理员！");
            }
            return result;
        }
    }

}
