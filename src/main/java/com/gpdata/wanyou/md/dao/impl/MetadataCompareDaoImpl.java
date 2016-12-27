package com.gpdata.wanyou.md.dao.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.md.dao.MetadataCompareDao;
import com.gpdata.wanyou.md.dto.MetadataCompareDto;
import com.gpdata.wanyou.md.entity.MetadataCompare;
/**
 * @author acer_liuyutong
 */
@Repository
public class MetadataCompareDaoImpl extends BaseDao implements MetadataCompareDao{

	/**
	 * 通过tableId获取MetadataCompare
	 */
	@Override
	@SuppressWarnings("unchecked")
	public MetadataCompare getMcByTableId(Integer tableId) {
		
        String sql = "select * from metadata_compare where tableId = :tableId";
		
        SQLQuery query = getCurrentSession().createSQLQuery(sql).addEntity(MetadataCompare.class);
        List<MetadataCompare> list = query.setParameter("tableId", tableId).list();
        MetadataCompare metadataCompare = null;
	    
        if (!list.isEmpty()) {
            metadataCompare = list.get(0);
        }
	    
		return metadataCompare;
    }

    /**
     * 获取MetadataCompare的总数
     */
    @Override
    public Integer getTotal(Integer resourceId,Integer tableId,Integer offset, Integer limit) {

    	StringBuilder sql = new StringBuilder();
        sql.append("select count(mc.tableId) from metadata_compare mc ");

        if (resourceId != null) {
			sql.append("left outer join datasource_table dt on mc.tableId = dt.tableId ")
			    .append("where dt.resourceId = :resourceId ");
		}
        if (tableId != null) {
			sql.append("where mc.tableId = :tableId");
		}

        SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
        
        if (resourceId != null) {
            query.setParameter("resourceId", resourceId);
        }
        if (tableId != null) {
        	query.setParameter("tableId", tableId);
		}

        String total = query.uniqueResult().toString();

        return Integer.valueOf(total, 10);
    }

    /**
     * 获取MetadataCompare的list
     */
    @Override
    public List<MetadataCompareDto> getRows(Integer resourceId,Integer tableId,Integer offset, Integer limit) {
    	StringBuilder sql = new StringBuilder();
        sql.append("select mc.*,dt.caption tableCaption from metadata_compare mc ");
        sql.append("left outer join datasource_table dt on mc.tableId = dt.tableId ");

        if (resourceId != null) {
			sql.append("where dt.resourceId = :resourceId ");
		}
        if (tableId != null) {
			sql.append("where mc.tableId = :tableId");
		}

        SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
        
        if (resourceId != null) {
            query.setParameter("resourceId", resourceId);
        }
        if (tableId != null) {
        	query.setParameter("tableId", tableId);
		}
        
        ResultTransformer transformer = Transformers.aliasToBean(MetadataCompareDto.class);
        query.setResultTransformer(transformer);
        query.setFirstResult(offset).setMaxResults(limit);
		
        @SuppressWarnings("unchecked")
        List<MetadataCompareDto> rows = query.list();
        
        return rows;
    }

    /**
	 * 字段、表 连表查询生成MetadataCompare
	 * @param sourceId
	 */
	@Override
	public void generateMetadataCompares(Integer sourceId) {
		String sql = "INSERT INTO metadata_compare(tableId,totalField,standField,nstandField,standPercent) "
				+ "SELECT df.tableId,COUNT(df.tableId) totalField,'0','0','0.0' "
				+ "FROM datasource_field df "
				+ "LEFT OUTER JOIN datasource_table dt "
				+ "ON df.tableId = dt.tableId "
				+ "WHERE dt.resourceId = :sourceId "
				+ "GROUP BY tableId";
		getCurrentSession().createSQLQuery(sql).setParameter("sourceId", sourceId).executeUpdate();
	}

	/**
	 * 通过fieldId获取MetadataCompare
	 */
	@Override
	public MetadataCompare getMcByFieldId(Integer fieldId) {
		String sql = "SELECT mc.id,totalField,mc.tableId,standField,nstandField,StandPercent "
				+ "FROM metadata_compare mc "
				+ "LEFT OUTER JOIN datasource_field df "
				+ "ON mc.tableId = df.tableId WHERE df.fieldId = :fieldId ";
		SQLQuery query = getCurrentSession().createSQLQuery(sql).addEntity(MetadataCompare.class);
		MetadataCompare MetadataCompare = (MetadataCompare) query.setParameter("fieldId", fieldId).list().get(0);
		return MetadataCompare;
	}

	/**
	 * MetadataCompare标准字段加1
	 */
	@Override
	public void incrStandField(MetadataCompare mc) {
        mc.incrStandField();
        getCurrentSession().update(mc);
	}
}