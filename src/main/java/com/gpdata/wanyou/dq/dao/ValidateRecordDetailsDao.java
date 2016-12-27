package com.gpdata.wanyou.dq.dao;

import java.util.Map;

import com.gpdata.wanyou.base.vo.BeanResult;

public interface ValidateRecordDetailsDao {

    /**
     * 通过记录Id获取记录详情
     * @param recordId
     */
    public BeanResult getDetailsByRecordId(Integer recordId,Integer offset,Integer limit);

    /**
     * 通过指定条件获取详情列表
     * @param params
     * @return
     */
    public BeanResult getDetailsByConditions(Map<String, Object> params);

}