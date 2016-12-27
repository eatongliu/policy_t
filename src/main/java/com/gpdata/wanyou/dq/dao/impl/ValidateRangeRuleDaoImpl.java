package com.gpdata.wanyou.dq.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.dq.dao.ValidateRangeRuleDao;
import com.gpdata.wanyou.dq.dto.ValidateRangeRuleDto;
import com.gpdata.wanyou.dq.entity.ValidateDataSource;
import com.gpdata.wanyou.dq.entity.ValidateRangeRule;

/**
 * @author ligang
 * 2016年11月14日
 */
@Repository
public class ValidateRangeRuleDaoImpl extends BaseDao implements ValidateRangeRuleDao{

    @Override
    public int save(ValidateRangeRule input) {
        getCurrentSession().save(input);
        return input.getRuleId();
    }

    @Override
    public void delete(Integer ruleId) {
        ValidateRangeRule validateRangeRule = (ValidateRangeRule)this.getCurrentSession().get(ValidateRangeRule.class,ruleId);
        this.getCurrentSession().delete(validateRangeRule);
    }

    @Override
    public void update(ValidateRangeRule input) {
        this.getCurrentSession().update(input);
    }

    @Override
    public ValidateRangeRuleDto getById(Integer ruleId) {
        String sql = "SELECT t.*, me.caption metadataEntityCaption, mb.caption metadataCaption,vf.formulaName,mb.metadataId,"
                + "  u.nickName creatorName "
                + "FROM validate_range_rule t "
                + "LEFT JOIN metadata_entity me ON me.entityId = t.entityId "
                + "LEFT JOIN metadata_bean mb ON mb.metadataId = me.metadataId "
                + "LEFT JOIN sys_user u ON u.userId = t.creatorId "
                + "LEFT JOIN validate_formula vf ON t.formulaId = vf.formulaId "
                + "WHERE t.ruleId =  "+ruleId;
        SQLQuery query = getCurrentSession().createSQLQuery(sql);
        ValidateRangeRuleDto dto = (ValidateRangeRuleDto) query.setResultTransformer(Transformers.aliasToBean(ValidateRangeRuleDto.class)).uniqueResult();
        return dto;
    }

    @Override
    public Map<String, Object> query(ValidateRangeRuleDto bean, Integer limit, Integer offset) {

        String sql = "SELECT t.*, me.caption metadataEntityCaption, mb.caption metadataCaption,vf.formulaName,mb.metadataId,"
                + "  u.nickName creatorName "
                + "FROM validate_range_rule t "
                + "LEFT JOIN metadata_entity me ON me.entityId = t.entityId "
                + "LEFT JOIN metadata_bean mb ON mb.metadataId = me.metadataId "
                + "LEFT JOIN sys_user u ON u.userId = t.creatorId "
                + "LEFT JOIN validate_formula vf ON t.formulaId = vf.formulaId "
                + "WHERE 1=1 ";
        String totalSql = "select count(t.ruleId)  "
                + "FROM validate_range_rule t "
                + "LEFT JOIN metadata_entity me ON me.entityId = t.entityId "
                + "LEFT JOIN metadata_bean mb ON mb.metadataId = me.metadataId "
                + "LEFT JOIN sys_user u ON u.userId = t.creatorId "
                + "LEFT JOIN validate_formula vf ON t.formulaId = vf.formulaId "
                + "WHERE 1=1 ";
        //查询条件
        //创建日期
        if (bean.getCreateDate()!=null) {
            sql+=" and t.createDate = :createDate ";
            totalSql+=" and t.createDate = :createDate ";
        }
        //元数据实体名称
        if (StringUtils.isNotBlank(bean.getMetadataEntityCaption())) {
            sql+=" and me.caption like :metadataEntityCaption ";
            totalSql+=" and me.caption like :metadataEntityCaption ";
        }
        //元数据名称
        if (StringUtils.isNotBlank(bean.getMetadataCaption())) {
            sql+=" and mb.caption like :metadataCaption ";
            totalSql+=" and mb.caption like :metadataCaption ";
        }
        Query query = this.getCurrentSession().createSQLQuery(sql).setFirstResult(offset).setMaxResults(limit);
        SQLQuery totalQuery = getCurrentSession().createSQLQuery(totalSql);
        //查询条件
        //创建日期
        if (bean.getCreateDate()!=null) {
            query.setDate("createDate", bean.getCreateDate());
            totalQuery.setDate("createDate",  bean.getCreateDate());
        }
        //元数据实体名称
        if (StringUtils.isNotBlank(bean.getMetadataEntityCaption())) {
            query.setString("metadataEntityCaption", "%" +bean.getMetadataEntityCaption()+ "%");
            totalQuery.setString("metadataEntityCaption", "%" +bean.getMetadataEntityCaption()+ "%");
        }
        //元数据名称
        if (StringUtils.isNotBlank(bean.getMetadataCaption())) {
            query.setString("metadataCaption", "%" +bean.getMetadataCaption()+ "%");
            totalQuery.setString("metadataCaption", "%" +bean.getMetadataCaption()+ "%");
        }
        
        @SuppressWarnings("unchecked")
        List<ValidateRangeRuleDto> validateRangeRuleList = query
        .setResultTransformer(Transformers.aliasToBean(ValidateRangeRuleDto.class))
        .list();
        int total = Integer.parseInt(totalQuery.uniqueResult().toString());
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("rows", validateRangeRuleList);
        return map;
    }
    /**
     * 通过数据源Id、表Id、字段名、来查询对应的规则
     * @param resourceId
     * @param tableId
     * @param fieldName
     */
    @Override
    public List<ValidateRangeRule> getRuleByField(Integer resourceId,Integer tableId,String fieldName){
        String sql = "SELECT a.* FROM validate_range_rule a "
                + "LEFT JOIN metadata_entity b ON a.entityId = b.entityId "
                + "LEFT JOIN datasource_field c ON b.fieldId = c.fieldId "
                + "LEFT JOIN datasource_table d ON c.tableId = d.tableId "
                + "WHERE  a.ruleStatus = 1 AND d.tableId = :tableId "
                + "AND d.resourceId  = :resourceId AND c.fieldName = :fieldName ";
       SQLQuery query = getCurrentSession().createSQLQuery(sql);
       ResultTransformer transformer = Transformers.aliasToBean(ValidateRangeRule.class);
       query.setResultTransformer(transformer);
       query.setParameter("resourceId", resourceId).setParameter("tableId", tableId).setParameter("fieldName", fieldName);
       List<ValidateRangeRule> list = query.list();
       return list;
    }

    @Override
    public ValidateDataSource getValidateDataSourceByEntityId(Integer entityId) {

        String hql = "select a from ValidateDataSource a "
                + " , DataSourceTable b, DataSourceField c "
                + " , MetadataEntity d "
                + " where a.id = b.resourceId "
                + " and b.tableId = c.tableId "
                + " and c.fieldId = d.fieldId "
                + " and d.entityId = :entityId ";
        return (ValidateDataSource) this.getCurrentSession()
                .createQuery(hql)
                .setInteger("entityId", entityId)
                .uniqueResult();
    }

    @Override
    public Integer getEntityIdByFieldID(Integer fieldId) {
        String sql = "SELECT me.entityId FROM metadata_entity me WHERE me.fieldId = :fieldId";
        return (Integer) this.getCurrentSession()
                .createSQLQuery(sql)
                .setInteger("fieldId", fieldId)
                .uniqueResult();
    }
}