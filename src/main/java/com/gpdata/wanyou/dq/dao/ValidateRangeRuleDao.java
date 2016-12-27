package com.gpdata.wanyou.dq.dao;

import java.util.List;
import java.util.Map;

import com.gpdata.wanyou.dq.dto.ValidateRangeRuleDto;
import com.gpdata.wanyou.dq.entity.ValidateDataSource;
import com.gpdata.wanyou.dq.entity.ValidateRangeRecord;
import com.gpdata.wanyou.dq.entity.ValidateRangeRule;

/**
 * @author ligang
 * 2016年11月14日
 */
public interface ValidateRangeRuleDao {

    //保存新范围验证规则
    public int save(ValidateRangeRule input);
    //删除 
    public void delete(Integer ruleId);
    //修改
    public void update(ValidateRangeRule input);
    //获取指定 id 的对象
    public ValidateRangeRuleDto getById(Integer ruleId);
    //query 查询
    public Map<String, Object> query(ValidateRangeRuleDto input,Integer limit,Integer offset);
    
    /**
     * 通过数据源Id、表Id、字段名、来查询对应的规则
     * @param resourceId
     * @param tableId
     * @param fieldName
     */
    List<ValidateRangeRule> getRuleByField(Integer resourceId, Integer tableId, String fieldName);

    /**
     *
     * @param entityId
     * @return
     */
    ValidateDataSource getValidateDataSourceByEntityId(Integer entityId);
    
    /**
    *通过字段ID获取元数据实体ID
    * @param fieldId
    * @return entityId
    */
    public Integer getEntityIdByFieldID(Integer fieldId);
}
