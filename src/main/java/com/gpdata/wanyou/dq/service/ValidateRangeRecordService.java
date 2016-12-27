package com.gpdata.wanyou.dq.service;

import java.util.List;

import com.gpdata.wanyou.base.vo.BeanResult;

public interface ValidateRangeRecordService {

    /**
     * 通过规则Id获取验证结果记录
     * @param ruleId
     */
    BeanResult getRecordByRuleId(Integer ruleId);

    /**
     * 进行数据验证
     * @param resourceId
     * @param tableId
     * @param messageList
     */
    List<List> dataValidate(Integer resourceId, Integer tableId, List<List> messageList);

    /**
     * 获取指定数据源下的质量管理报表
     * @param resourceId
     */
    public BeanResult getResourceReport(Integer resourceId);

}