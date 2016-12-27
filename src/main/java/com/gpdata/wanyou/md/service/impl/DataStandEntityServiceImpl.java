package com.gpdata.wanyou.md.service.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.md.dao.DataStandEntityDao;
import com.gpdata.wanyou.md.entity.DataStandEntity;
import com.gpdata.wanyou.md.service.DataStandEntityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Map;

@Service
@Transactional(rollbackForClassName = { "RuntimeException", "Exception" })
public class DataStandEntityServiceImpl extends BaseService implements DataStandEntityService {

    @Resource
    private DataStandEntityDao dataStandEntityDao;

    @Override
    public DataStandEntity getById(Integer standentid) {
        return dataStandEntityDao.getById(standentid);
    }

    @Override
    public void delete(Integer standentid) {
        dataStandEntityDao.delete(standentid);
    }

    @Override
    public int save(DataStandEntity input) {
        return dataStandEntityDao.save(input);
    }

    @Override
    public Map<String, Object> query(DataStandEntity input, Integer limit, Integer offset) {
        return dataStandEntityDao.query(input, limit, offset);
    }

    @Override
    public void update(DataStandEntity input) {
        dataStandEntityDao.update(input);
    }

    @Override
    public Map<String, Object> getAllByStandId(Integer standId, Integer limit, Integer offset) {
        return dataStandEntityDao.getAllByStandId(standId, limit, offset);
    }

    @Override
    public void upload(List<DataStandEntity> list) {
        for (DataStandEntity dse : list) {
            save(dse);
        }
    }

}
