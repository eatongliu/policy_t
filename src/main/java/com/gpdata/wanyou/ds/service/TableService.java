package com.gpdata.wanyou.ds.service;

import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.ds.entity.DataSourceTable;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface TableService {

    /**
     * 获取数据表表名列表
     *
     * @param resourceId
     * @param caption
     * @return
     */
    List getDtIdAndNameList(Integer resourceId, String caption);

    /**
     * 通过ID查询数据表
     *
     * @param tableId
     * @return
     */
    DataSourceTable getDataTableById(Integer tableId);

    /**
     * 关键字查询数据表
     *
     * @param resourceid
     * @param caption
     * @param limit
     * @param offset
     * @return
     */
    Map<String, Object> getDtSearchList(Integer resourceid, String caption, Integer limit, Integer offset);

    List<String> getDtNameList(Integer resourceId);

    void updateTable(DataSourceTable dataTable);

    /**
     * mysql自定义sink调用fangfa
     *
     * @Author yaz
     * @Date 2016/11/17 16:14
     */
    boolean insertDataToTable(DataSourceResource resource, DataSourceTable table, List<String[]> datas);

    List<DataSourceTable> getDataSourceTableList(Integer resourceId);
}
