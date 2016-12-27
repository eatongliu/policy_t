package com.gpdata.wanyou.md.service.impl;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.md.dao.DataStandardDao;
import com.gpdata.wanyou.md.entity.DataStandard;
import com.gpdata.wanyou.md.service.DataStandardService;
import com.gpdata.wanyou.utils.SimpleBeanPropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 3.1数据标准
 *
 * @author acer_liuyutong
 */
@Service
public class DataStandardServiceImpl extends BaseService implements DataStandardService {

    @Autowired
    private DataStandardDao dataStandardDao;

    /**
     * 获得一个数据标准的详细信息
     *
     * @param standId
     * @return
     */
    @Override
    public DataStandard getById(Integer standId) {
        return dataStandardDao.getById(standId);
    }

    /**
     * 保存一个新对象
     *
     * @param dataStandard
     * @return
     */
    @Override
    public Integer save(DataStandard dataStandard) {
        return dataStandardDao.save(dataStandard);
    }

    /**
     * query 查询, 无条件等同于列表显示
     *
     * @param offset
     * @param limit
     * @return
     */
    @Override
    public Pair<Integer, List<DataStandard>> getDataStandards(String caption, int offset, int limit) {

        Integer total = dataStandardDao.getDataStandardTotal(caption, offset, limit);
        List<DataStandard> rows = dataStandardDao.getDataStandardRows(caption, offset, limit);

        return Pair.of(total, rows);
    }

    /**
     * 删除一个对象
     *
     * @param standId
     */
    @Override
    public void delete(Integer standId) {
        dataStandardDao.delete(standId);
    }

    /**
     * 更新一个对象
     *
     * @param input
     */
    @Override
    public void update(DataStandard input) {
        DataStandard original = dataStandardDao.getById(input.getStandId());

        if (original == null) {
            throw new RuntimeException("被修改的对象不存在!");
        }
        SimpleBeanPropertiesUtil.copyNotNullProperties(input, original);

        dataStandardDao.update(original);
    }

    /*
     *
     */
    @Override
    public List<DataSourceResource> getNonMappingDataSourceResources(Integer standId) {

        return this.dataStandardDao.getNonMappingDataSourceResources(standId);
    }
}
