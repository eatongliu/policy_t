package com.gpdata.wanyou.ds.service;

import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.ds.entity.DataSourceTable;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface ResourceService {

    /**
     * 根据主键获取DataSourceResource实体
     */
    DataSourceResource getDataSourceById(int resourceId);

    /**
     * 根据条件获取DataSourceResource列表
     * 规定：
     * 总数：total
     * 数据：dataList
     */
    BeanResult getDataSourceList(Map<String, String> params);

    /**
     * 获取所有所有数据源，不分类型，不分页；
     * 除更新所有数据源外，禁止调用！
     */
    List<DataSourceResource> getAllDataSourceWithoutLimit();

    /**
     * 获取数据源列表，只获取id和名字，不分页
     */
    BeanResult getAllDataSourceIdAndNameList(String resourceType);

    /**
     * 增加实体
     */
    void addDataSourceResource(DataSourceResource dataSourceResource);

    /**
     * 根据数据源新增表结构
     * 新增数据源时调用
     * 返回：
     * 共新增的表数量："count" : int
     * 总大小："size" : long
     */
    Map<String, String> addDataSourceTable(DataSourceResource dataSourceResource, Connection conn);

    /**
     * 根据数据、数据表增加字段信息
     * 新增数据源时调用
     * 返回：
     * 共新增的表数量："count" : int
     */
    Map<String, String> addDataSourceField(DataSourceResource dataSourceResource, DataSourceTable datasourceTable, Connection conn);

    /**
     * 更新数据源以及相关表、字段
     * 适用正常可达的数据源
     */
    void updateDataSourceResource(DataSourceResource dataSourceResource);

    /**
     * 更新数据源以及相关表、字段
     * 使用所有所有数据源
     * 重载版本
     */
    void updateDataSourceResource(DataSourceResource dataSourceResource, Connection conn);

    /**
     * 修改不可达的数据源、以及相关的表、字段状态
     * 即：自动更新时，测试失败的数据源
     */
    void updateDataSourceResourceWithoutConn(DataSourceResource dataSourceResource);

    /**
     * 根据数据源修改表结构
     * 修改数据源时调用
     * 返回：
     * 表数量："count" : int
     * 总大小："size" : long
     */
    Map<String, String> updateDataSourceTable(DataSourceResource dataSourceResource, Connection conn);

    /**
     * 根据数据、数据表修改字段信息
     * 修改数据源时调用
     * 返回：
     * 表数量："count" : int
     */
    Map<String, String> updateDataSourceField(DataSourceResource dataSourceResource, DataSourceTable datasourceTable, Connection conn);

    /**
     * 测试连接
     * 目前只能测试MySql类型的数据源
     */
    BeanResult testDataSourceConn(DataSourceResource dataSourceResource);

    /**
     * 测试链接方法；
     * 在自动更新数据源时调用，也可以在添加时调用；
     * HDFS型返回null
     * 目的：避免多次创建连接
     * 注意：本方法不释放链接，由调用方法释放
     */
    Connection testDataSourceConnWithoutClose(DataSourceResource dataSourceResource);

    /**
     * 根据用户自己写的sql查询数据
     */
    BeanResult getDataFromDataSourceBySql(DataSourceResource dataSourceResource, String sql);

    /**
     * 根据用户自己写的sql查询数据，定时任务所用
     * 数据和标题分开处理
     */
    Map<String, Object> getDataFromTableBySql(DataSourceResource dataSourceResource, String sql);

    /**
     * 根据用户自己写的sql查询数据
     * 和上一个方法不同的地方在于返回的数据内容不一样
     * 这个方法会多返回 一个标题 (title) list 和 数据 (rows) list
     *
     * @param dataSourceResource
     * @param sql
     * @return
     */
    BeanResult getDataFromDataSourceBySql2(DataSourceResource dataSourceResource, String sql);

    /**
     * 判断数据源更新任务是否执行；
     * 进行判断
     */
    boolean isUpdateDs(final String step);
}
