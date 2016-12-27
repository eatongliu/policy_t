package com.gpdata.wanyou.dq.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.dq.dao.ValidateRecordDetailsDao;
import com.gpdata.wanyou.dq.service.ValidateRecordDetailsService;
import com.gpdata.wanyou.ds.entity.DataSourceResource;
/**
 * @author acer_liuyutong
 */
@Service
public class ValidateRecordDetailsServiceImpl extends BaseService implements ValidateRecordDetailsService{

    @Autowired
    private ValidateRecordDetailsDao validateRecordDetailsDao;

    @Override
    public BeanResult getDetailsByRecordId(Integer recordId,Integer offset,Integer limit) {
        return validateRecordDetailsDao.getDetailsByRecordId(recordId,offset,limit);
    }

    @Override
    public BeanResult getDetailsByConditions(Map<String, Object> params) {
        return validateRecordDetailsDao.getDetailsByConditions(params);
    }
    
    /**
     * 获取指定数据源下的质量管理报表详情
     * @param resourceId
     */
    @Override
    public BeanResult getResourceReportDetails(Integer resourceId,Integer offset,Integer limit){
        DataSourceResource dataSource = simpleDao.getById(DataSourceResource.class, resourceId);
        if (dataSource == null) {
            return BeanResult.error("所选数据源不存在！");
        }
        //TODO 待完成
        return null;
    }
}
