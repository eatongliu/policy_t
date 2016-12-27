package com.gpdata.wanyou.md.dao;

import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.md.entity.DataStandard;

import java.util.List;

public interface DataStandardDao {

    /**
     * 获得一个数据标准的详细信息
     *
     * @param standId
     * @return
     */
    public DataStandard getById(Integer standId);

    /**
     * 保存一个新对象
     *
     * @param dataStandard
     * @return
     */
    public Integer save(DataStandard dataStandard);

    /**
     * query 查询, 无条件等同于列表显示
     *
     * @param offset
     * @param limit
     * @return
     */
    public Integer getDataStandardTotal(String standName, int offset, int limit);

    public List<DataStandard> getDataStandardRows(String standName, int offset, int limit);

    /**
     * 删除一个对象
     *
     * @param standId
     */
    public void delete(Integer standId);

    /**
     * 更新一个对象
     *
     * @param dataStandard
     */
    public void update(DataStandard dataStandard);

    List<DataSourceResource> getNonMappingDataSourceResources(Integer standId);
}