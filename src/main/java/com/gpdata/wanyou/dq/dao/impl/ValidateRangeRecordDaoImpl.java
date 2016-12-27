package com.gpdata.wanyou.dq.dao.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.dq.dao.ValidateRangeRecordDao;
import com.gpdata.wanyou.dq.entity.ValidateRangeRecord;
/**
 * @author acer_liuyutong
 */
@Repository
public class ValidateRangeRecordDaoImpl extends BaseDao implements ValidateRangeRecordDao{

    /**
     * 通过规则Id获取记录
     */
    @Override
    public ValidateRangeRecord getRecordByRuleId(Integer ruleId) {
        String sql = "select * from validate_range_record a where a.ruleId = :ruleId ";
        SQLQuery query = getCurrentSession().createSQLQuery(sql).addEntity(ValidateRangeRecord.class);
        List<ValidateRangeRecord> list = query.setParameter("ruleId", ruleId).list();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    
    /**
     * 更新一个记录，并执行flush操作
     */
    @Override
    public void updateRecord(ValidateRangeRecord record){
        Session session = this.getCurrentSession();
        session.update(record);
        session.flush();
    }
    /**
     * 通过数据源Id、表Id、字段名、来查询对应的记录
     * @param resourceId
     * @param tableId
     * @param fieldName
     */
    @Override
    public ValidateRangeRecord getRecordByField(Integer resourceId,Integer tableId,String fieldName) {
        ValidateRangeRecord record = null;
        String sql =  "SELECT a.* "
                + "FROM validate_range_record a "
                + "LEFT JOIN metadata_entity b ON a.entityId = b.entityId "
                + "LEFT JOIN datasource_field c ON b.fieldId = c.fieldId "
                + "LEFT JOIN datasource_table d ON c.tableId = d.tableId "
                + "WHERE  a.ruleStatus = 1 AND d.tableId = :tableId "
                + "AND d.resourceId  = :resourceId AND c.fieldName = :fieldName ";
        SQLQuery query = getCurrentSession().createSQLQuery(sql);
        ResultTransformer transformer = Transformers.aliasToBean(ValidateRangeRecord.class);
        query.setResultTransformer(transformer);
        query.setParameter("resourceId", resourceId).setParameter("tableId", tableId).setParameter("fieldName", fieldName);
        List<ValidateRangeRecord> list = query.list();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

}