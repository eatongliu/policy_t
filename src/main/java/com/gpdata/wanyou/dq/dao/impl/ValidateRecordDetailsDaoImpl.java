package com.gpdata.wanyou.dq.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.dq.dao.ValidateRecordDetailsDao;
import com.gpdata.wanyou.dq.dto.RecordDetailsDto;
import com.gpdata.wanyou.dq.entity.ValidateRangeRecord;

@Repository
public class ValidateRecordDetailsDaoImpl extends BaseDao implements ValidateRecordDetailsDao{

    /**
     * 通过记录Id获取记录详情
     */
    @Override
    public BeanResult getDetailsByRecordId(Integer recordId,Integer offset,Integer limit) {
        Session session = getCurrentSession();
        String totalHql = "select count(v) from ValidateRecordDetails v where v.recordId = :recordId";
        Query totalQuery = session.createQuery(totalHql).setParameter("recordId", recordId);
        String total = totalQuery.uniqueResult().toString();
        
        String rowsHql = "from ValidateRecordDetails v where v.recordId = :recordId";
        Query rowsQuery = session.createQuery(rowsHql).setParameter("recordId", recordId);
        rowsQuery.setFirstResult(offset).setMaxResults(limit);
        List<ValidateRangeRecord> rows = rowsQuery.list();
        
        BeanResult beanResult = BeanResult.success(Integer.valueOf(total,10), rows);
        return beanResult;
    }
    
    /**
     * 通过条件获取详情列表
     */
    @Override
    public BeanResult getDetailsByConditions(Map<String, Object> params){
        Session session = this.getCurrentSession();
        Integer offset = (Integer) params.get("offset");
        Integer limit = (Integer) params.get("limit");
        String totalSql = "SELECT count(a.detailsId) "
                + "FROM validate_record_details a "
                + "LEFT JOIN validate_range_record b ON a.recordId = b.recordId "
                + "LEFT JOIN validate_range_rule c ON b.ruleId = c.ruleId "
                + "LEFT JOIN validate_formula d ON c.formulaId = d.formulaId "
                + "LEFT JOIN metadata_entity e ON c.entityId = e.entityId "
                + "LEFT JOIN metadata_bean f ON e.metadataId = f.metadataId "
                + "WHERE 1 = 1 ";
        totalSql = parseParamsOfSql(totalSql,params);
        SQLQuery totalQuery = session.createSQLQuery(totalSql);
        totalQuery = parseParamsOfQuery(totalQuery, params);
        String total = totalQuery.uniqueResult().toString();
        
        String rowSql = "SELECT a.detailsId,a.npassData,a.reason,b.validateDate,"
                + "c.maxVal,c.minVal,c.dataPrecision,d.formulaId, "
                + "d.formulaName,f.caption metadataCaption,e.caption metadataEntityCaption "
                + "FROM validate_record_details a "
                + "LEFT JOIN validate_range_record b ON a.recordId = b.recordId "
                + "LEFT JOIN validate_range_rule c ON b.ruleId = c.ruleId "
                + "LEFT JOIN validate_formula d ON c.formulaId = d.formulaId "
                + "LEFT JOIN metadata_entity e ON c.entityId = e.entityId "
                + "LEFT JOIN metadata_bean f ON e.metadataId = f.metadataId "
                + "WHERE 1 = 1 ";
        rowSql = parseParamsOfSql(rowSql,params);
        SQLQuery rowsQuery = session.createSQLQuery(rowSql);
        rowsQuery = parseParamsOfQuery(rowsQuery, params);
        
        ResultTransformer transformer = Transformers.aliasToBean(RecordDetailsDto.class);
        rowsQuery.setResultTransformer(transformer).setFirstResult(offset).setMaxResults(limit);
        List<RecordDetailsDto> rows = rowsQuery.list();
        return BeanResult.success(Integer.valueOf(total), rows);
    }
    
    public String parseParamsOfSql(String sql,Map<String, Object> params){
        String metadataName = (String) params.get("metadataName");
        String metadataEntityName = (String) params.get("metadataEntityName");
        Integer formulaId = (Integer) params.get("formulaId");
        Integer maxVal = (Integer) params.get("maxVal");
        Integer minVal = (Integer) params.get("minVal");
        Integer dataPrecision = (Integer) params.get("dataPrecision");
        Date validateDate = (Date) params.get("validateDate");
        
        if (StringUtils.isNotBlank(metadataName)) {
            sql += "and  f.caption like :metadataName ";
        }
        if (StringUtils.isNotBlank(metadataEntityName)) {
            sql += "and  e.caption like :metadataEntityName ";
        }
        if (formulaId != null) {
            sql +="and d.formulaId = :formulaId ";
        }
        if (maxVal != null) {
            sql +="and c.maxVal = :maxVal ";
        }
        if (minVal != null) {
            sql +="and c.minVal = :minVal ";
        }
        if (dataPrecision != null) {
            sql +="and c.dataPrecision = :dataPrecision ";
        }
        if (validateDate != null) {
            sql +="and c.validateDate = :validateDate ";
        }
        return sql;
    }
    
    public SQLQuery parseParamsOfQuery(SQLQuery query,Map<String, Object> params){
        String metadataName = (String) params.get("metadataName");
        String metadataEntityName = (String) params.get("metadataEntityName");
        Integer formulaId = (Integer) params.get("formulaId");
        Integer maxVal = (Integer) params.get("maxVal");
        Integer minVal = (Integer) params.get("minVal");
        Integer dataPrecision = (Integer) params.get("dataPrecision");
        Date validateDate = (Date) params.get("validateDate");
        if (StringUtils.isNotBlank(metadataName)) {
            query.setParameter("metadataName", "%"+metadataName+"%");
        }
        if (StringUtils.isNotBlank(metadataEntityName)) {
            query.setParameter("metadataEntityName", "%"+metadataEntityName+"%");
        }
        if (formulaId != null) {
            query.setParameter("formulaId", formulaId);
        }
        if (maxVal != null) {
            query.setParameter("maxVal", maxVal);
        }
        if (minVal != null) {
            query.setParameter("minVal", minVal);
        }
        if (dataPrecision != null) {
            query.setParameter("dataPrecision", dataPrecision);
        }
        if (validateDate != null) {
            query.setParameter("validateDate", validateDate);
        }
        return query;
    }
    
}