package com.gpdata.wanyou.dq.service;

import java.util.Map;

import com.gpdata.wanyou.base.vo.BeanResult;

public interface ValidateRecordDetailsService {

    /**
     * 通过记录Id获取验证详情
     * @param recordId
     */
    BeanResult getDetailsByRecordId(Integer recordId,Integer offset,Integer limit);
    
    /**
     * 通过条件获取验证详情列表
     * @param params 
     */
    BeanResult getDetailsByConditions(Map<String, Object> params);

    /**
     * 获取指定数据源下的质量管理报表详情
     * @param resourceId
     */
    BeanResult getResourceReportDetails(Integer resourceId,Integer offset,Integer limit);

}
