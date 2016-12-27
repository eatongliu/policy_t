package com.gpdata.wanyou.dq.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.dq.dao.ValidateRangeRecordDao;
import com.gpdata.wanyou.dq.entity.ValidateRangeRecord;
import com.gpdata.wanyou.dq.service.ValidateRangeRecordService;
import com.gpdata.wanyou.dq.utils.DataValidateUtil;
import com.gpdata.wanyou.ds.entity.DataSourceResource;
/**
 * @author acer_liuyutong
 */
@Service
public class ValidateRangeRecordServiceImpl extends BaseService implements ValidateRangeRecordService{

    @Autowired
    private ValidateRangeRecordDao validateRangeRecordDao;
    @Autowired
    private DataValidateUtil dataValidateUtil;

    @Override
    public BeanResult getRecordByRuleId(Integer ruleId) {
        ValidateRangeRecord record = validateRangeRecordDao.getRecordByRuleId(ruleId);
        return BeanResult.success(record);
    }
    
    @Override
    public List<List> dataValidate(Integer resourceId, Integer tableId, List<List> messageList){
        return dataValidateUtil.rangeValidate(resourceId, tableId, messageList);
    }
    
    /**
     * 获取指定数据源下的质量管理报表
     * @param resourceId
     */
    @Override
    public BeanResult getResourceReport(Integer resourceId) {
        DataSourceResource dataSource = simpleDao.getById(DataSourceResource.class, resourceId);
        if (dataSource == null) {
            return BeanResult.error("所选数据源不存在！");
        }
        //TODO 待完成
        return null;
    }
    
}