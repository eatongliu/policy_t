package com.gpdata.wanyou.ds.dao;

import com.gpdata.wanyou.ds.entity.DataSourceResource;

import java.util.List;
import java.util.Map;

public interface ResourceDao {

    DataSourceResource getDataSourceById(int id);

    void updateDataSource(DataSourceResource dataSource);

    int addDataSource(DataSourceResource dataSource);

    /**
     * 获取实体集合
     */
    List<DataSourceResource> getDataSourceList(Map<String, String> params);

    /**
     * 获取实体集合，不分页，不分条件
     * 在更新所有数据源时调用，此外禁止使用此方法
     */
    List<DataSourceResource> getAllDataSourceWithoutLimit();

    /**
     * 获取实体集合个数
     */
    int getTotalDataSource(Map<String, String> params);

    /**
     * 获取数据源列表，只获取id和名字，不分页
     */
    List<Map<String, Object>> getAllDataSourceIdAndNameList(String resourceType);


    /**
     * 查一个 entityId 所对应的数据源
     * @param entityId
     * @return
     */
    DataSourceResource getDataSourceByEntityId(Integer entityId);
}
