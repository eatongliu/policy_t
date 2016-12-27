package com.gpdata.wanyou.dq.dao;

import com.gpdata.wanyou.dq.entity.ValidateRangeRecord;

/**
 * @author acer_liuyutong
 */
public interface ValidateRangeRecordDao {
    
	/**
	 * 通过规则Id获取记录
	 * @param ruleId
	 */
    public ValidateRangeRecord getRecordByRuleId(Integer ruleId);
    /**
     * 更新一条记录，并执行flush操作
     * @param record
     */
    void updateRecord(ValidateRangeRecord record);
    
    /**
     * 通过数据源Id、表Id、字段名、来查询对应的记录
     * @param resourceId
     * @param tableId
     * @param fieldName
     */
    ValidateRangeRecord getRecordByField(Integer resourceId, Integer tableId, String fieldName);

}