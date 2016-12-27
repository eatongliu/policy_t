package com.gpdata.wanyou.md.service;

import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.md.entity.DataStandard;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * 3.1数据标准
 *
 * @author acer_liuyutong
 */
public interface DataStandardService {

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
    public Pair<Integer, List<DataStandard>> getDataStandards(String caption, int offset, int limit);

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

    /**
     * 根据数据标准 id
     * 获取未曾和此标准进行映射的数据源的列表
     * @param standId
     * @return
     */
    List<DataSourceResource> getNonMappingDataSourceResources(Integer standId);
}
