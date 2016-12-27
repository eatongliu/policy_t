package com.gpdata.wanyou.ds.controller;

import com.alibaba.fastjson.JSONArray;
import com.gpdata.wanyou.ds.constant.DataSourceConstant;
import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.ds.entity.DataSourceTable;
import com.gpdata.wanyou.ds.service.ResourceService;
import com.gpdata.wanyou.ds.service.TableService;
import com.gpdata.wanyou.ds.util.DataBaseMetaDataHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 数据表控制器，实现与数据表有关的功能
 *
 * @author quyili
 */
@Controller
@RequestMapping()
public class TableController {
    private static final Logger logger = LoggerFactory.getLogger(TableController.class);

    @Autowired
    private TableService tableService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private DataBaseMetaDataHelper dataBaseMetaDataHelper;


    /**
     * 说明：显示具体某个数据表的详细信息。
     * 参数：tableid 数据表ID（必填）
     * 成功：表datasource_table中指定TableID的记录
     * 失败：[“error”:”错误原因”]
     *
     * @param map
     * @return
     * @author qyl
     */
    @RequestMapping(value = "/ds/{resourceId}/{tableId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> viewDataTableInfo(@PathVariable Integer tableId) {

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            DataSourceTable dataTable = tableService.getDataTableById(tableId);
            result.put("status", "SUCCESS");
            if (dataTable != null) {
                result.put("data", dataTable);
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
     * 说明：输入关键字获取数据表名称中含有关键字的数据表的信息
     * 参数1：resourceid 数据资源ID（必填）
     * 参数2：caption数据表名
     * 参数3：limit 获取条数（必填）
     * 参数4：offset 从第几条记录开始（必填）
     * 成功：模糊匹配Caption的记录，拼接所有字段JSON
     * 失败：[“error”:”错误原因”]
     *
     * @param
     * @return
     * @author qyl
     */
    @RequestMapping(value = "/ds/{resourceId}/sch", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> searchDataTables(
            @PathVariable Integer resourceId,
            @RequestParam(required = false, defaultValue = "") String caption,
            @RequestParam(required = false, defaultValue = "20") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset) {

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> dataTableList = tableService.getDtSearchList(resourceId, caption, limit, offset);
            result.put("status", "SUCCESS");
            if (dataTableList != null) {
                result.put("data", dataTableList);
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
     * 说明：输入关键字获取数据表名称中含有关键字的数据表的简略信息
     * 参数1：resourceid 数据资源ID（必填）
     * 参数2：caption数据表名称
     * 成功：参数1精确匹配，参数2模糊匹配 的记录，拼接tableId、tableName字段JSON
     * 失败：[“error”:”错误原因”]
     *
     * @param
     * @return
     * @author qyl
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/ds/{resourceId}/ssch", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> listDataTableNames(
            @PathVariable Integer resourceId,
            @RequestParam(required = false, defaultValue = "") String caption) {

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List dataTableNameList = tableService.getDtIdAndNameList(resourceId, caption);
            result.put("status", "SUCCESS");
            if (dataTableNameList != null) {
                result.put("data", dataTableNameList);
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
     * 说明：检索指定数据表的字段，数据库类型表使用
     * 参数1：tableid数据表ID（必填）
     * 参数2：limit获取条数，默认20
     * 参数3：offset，从第几条开始，默认0
     * 成功：指定数据表的列的数据，若fieldName为空则表示所有数据
     * 失败：[“error”:”错误原因”]
     *
     * @return
     * @author qyl
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/ds/{resourceId}/{tableId}/con", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> searchDtDataByColumn(
            @PathVariable Integer resourceId,
            @PathVariable Integer tableId,
            @RequestParam(required = false, defaultValue = "") String fieldName,
            @RequestParam(required = false, defaultValue = "20") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset) {

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            DataSourceTable dataTable = tableService.getDataTableById(tableId);
            DataSourceResource dataSource = resourceService.getDataSourceById(resourceId);

            Map data = dataBaseMetaDataHelper.searchDtByColumn(
                    dataSource,
                    dataTable.getTableName(),
                    fieldName,
                    limit, offset);

            result.put("status", "SUCCESS");
            if (data != null) {
                result.put("data", data);
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
     * 更新数据表中的sql语句
     * 若querySql不为空，存储sql，并将此表中isAutoUpdate属性设为Y
     * 若querySql为空，清空sql，并将此表中isAutoUpdate属性设为N
     */
    @RequestMapping(value = "/ds/{resourceId}/{tableId}/usql", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> updateTableSql(@PathVariable Integer tableId,
                                              @RequestBody Map<String, String> params) {
        String querySql = params.get("querySql") == null ? "" : params.get("querySql");
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            DataSourceTable dataTable = tableService.getDataTableById(tableId);
            if (querySql.isEmpty()) {
                dataTable.setIsAutoUpdate(DataSourceConstant.NO);
            } else {
                dataTable.setIsAutoUpdate(DataSourceConstant.YES);
            }
            dataTable.setUpdateSql(querySql);

            tableService.updateTable(dataTable);

            result.put("status", "SUCCESS");
            return result;
        } catch (Exception e) {
            logger.error("更新异常，{}", e);
            result.put("status", "ERROR");
            if (e.getCause() != null) {
                result.put("cause", e.getCause());
            } else {
                result.put("cause", "更新失败，请联系管理员！");
            }
            return result;
        }
    }

    /**
     * 用于mysql的自定义sink
     *
     * @Author yaz
     * @Date 2016/11/17 14:32
     */
    @RequestMapping(value = "/ds/{resourceId}/{tableId}/{sinkListId}/sinkdata", consumes = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public void insertDataToTable(@PathVariable Integer resourceId,
                                  @PathVariable Integer tableId,
                                  @PathVariable Integer sinkListId,
                                  @RequestBody Map<String, Object[]> params) {
        logger.debug("开始更新数据源id为{}，表id为{}的第{}组数据！", resourceId, tableId, sinkListId);
        DataSourceResource resource = resourceService.getDataSourceById(resourceId);
        DataSourceTable table = tableService.getDataTableById(tableId);
        if (null == resource || null == table) {
            logger.debug("错误的tableId！{}", tableId);
        }
        Object[] array = null;
        try {
            array = params.get("data");
            //大小为1说明只是传了一组标题，没有真实数据
            if (null == array || array.length == 1) {
                logger.debug("无效的数据");
                return;
            }
            ArrayList<String[]> dataList = new ArrayList<>();
            for (Object bean : array) {
                String temp = bean.toString().substring(1, bean.toString().length() - 1);
                String[] res = temp.split(",");
                dataList.add(res);
            }
            if (tableService.insertDataToTable(resource, table, dataList)) {
                logger.debug("更新成功！");
            } else {
                logger.error("数据源id为{}，表id为{}的第{}组数据解析失败！数据为{}，未出现异常！", resourceId, tableId, sinkListId, array);
            }
        } catch (Exception e) {
            logger.error("数据源id为{}，表id为{}的第{}组数据解析失败！数据为{},异常原因{}", resourceId, tableId, sinkListId, array, e);
            return;
        }
    }
}
