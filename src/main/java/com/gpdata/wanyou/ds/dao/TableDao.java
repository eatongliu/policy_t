package com.gpdata.wanyou.ds.dao;

import com.gpdata.wanyou.ds.entity.DataSourceTable;

import java.util.List;
import java.util.Map;

/**
 * 数据表Dao层接口
 *
 * @author caichangxing
 */
@SuppressWarnings("rawtypes")
public interface TableDao {

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
    Map<String, Object> getDataTableByKeyword(Integer resourceid, String caption, Integer limit, Integer offset);


    /**
     * 获取数据表表名列表
     *
     * @param resourceId
     * @param caption
     * @return
     */
    List getDtIdAndNames(Integer resourceId, String caption);

    /**
     * 新增表
     */
    void addDataTable(DataSourceTable table);

    /**
     * 更新表
     */
    void updataDataTable(DataSourceTable table);

    /**
     * 根据数据源获取相关的表
     */
    List<DataSourceTable> getTableListByDsId(Integer dataSourceId);

    List<String> getDtNameList(Integer resourceId);

    /**
     * 获取需要自动更新数据的数据表
     *
     * @return
     */
    List<DataSourceTable> getTableListOfAutoUpdate();

    List<DataSourceTable> getDataSourceTableList(Integer resourceId);
}
