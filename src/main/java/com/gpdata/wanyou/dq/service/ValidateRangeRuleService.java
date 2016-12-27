package com.gpdata.wanyou.dq.service;

import java.util.List;
import java.util.Map;

import com.gpdata.wanyou.dq.dto.ValidateRangeRuleDto;
import com.gpdata.wanyou.dq.entity.ValidateRangeRule;

/**
 * 范围验证规则Service
 * @author ligang
 * 2016年11月14日
 */
public interface ValidateRangeRuleService {
    
    /**
     * 说明：保存新范围验证规则
     * 参数：范围验证规则ValidateRangeRule（必填）
     * 成功：ruleId
     * 
     * @param input
     * @return ruleId/[“error”:”错误原因”]
     */
    public int save(ValidateRangeRule input);

    public void createInnerDataSourceAndOther(Integer entityId);

    /**
     * 说明：删除范围验证规则
     * 参数：ruleId（必填） 
     * 成功：status : SUCCESS
     * @param ruleId
     * @return status : SUCCESS/[“error”:”错误原因”]
     * @throws Exception 
     */
    public void delete(Integer ruleId) throws Exception;
    

    /**
     * 说明：修改 
     * 参数：ValidateRangeRule
     * 成功：status : SUCCESS
     * 
     * @param ValidateRangeRule
     * @return status : SUCCESS/[“error”:”错误原因”]
     */
    public void update(ValidateRangeRule input);
    /**
     * 说明：获取指定 id 的范围验证规则
     * 参数：ruleId
     * 成功：表validate_range_rule中指定ruleId的记录
     * 
     * @param ruleId
     * @return ValidateRangeRule/[“error”:”错误原因”]
     */
    public ValidateRangeRuleDto getById(Integer ruleId);
    /**
     * 说明：条件查询 
     * 参数：
     * 成功：数据对象 
     * 
     * @param ValidateRangeRuleDto
     * @return 数据对象/[“error”:”错误原因”]
     */
    public Map<String, Object> query(ValidateRangeRuleDto input,Integer limit,Integer offset);
    /**
    *通过字段ID获取元数据实体ID
    * @param fieldId
    * @return entityId
    */
    public Integer getEntityIdByFieldID(Integer fieldId);

}