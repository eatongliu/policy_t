package com.gpdata.wanyou.md.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.ds.entity.DataSourceField;
import com.gpdata.wanyou.md.dao.StandSourceMapDao;
import com.gpdata.wanyou.md.entity.StandSourceMap;

/**
 * 数据标准数据源映射
 *
 * @author acer_liuyutong
 */
@Repository
public class StandSourceMapDaoImpl extends BaseDao implements StandSourceMapDao {

    /**
     * 获取一个对象
     */
    @Override
    public StandSourceMap getById(String ssmId) {
        return (StandSourceMap) getCurrentSession().get(StandSourceMap.class, ssmId);
    }

    /**
     * 查询映射信息
     */
    @Override
    public Integer queryTotal(String caption, int limit, int offset) {

        String hql = "select count(s) from StandSourceMap s ";

        if (StringUtils.isNotBlank(caption)) {
            hql += "where s.caption like :caption ";
        }

        Query query = getCurrentSession().createQuery(hql);

        if (StringUtils.isNotBlank(caption)) {
            query.setString("caption", "%" + caption + "%");
        }

        String total = query.uniqueResult().toString();

        return Integer.valueOf(total, 10);
    }

    @Override
    public List<StandSourceMap> queryRows(String caption, int limit, int offset) {

        String hql = "from StandSourceMap s ";

        if (StringUtils.isNotBlank(caption)) {
            hql += "where s.caption like :caption ";
        }

        Query query = getCurrentSession().createQuery(hql);
        
        if (StringUtils.isNotBlank(caption)) {
            query.setString("caption", "%" + caption + "%");
        }

        query.setFirstResult(offset).setMaxResults(limit);

        @SuppressWarnings("unchecked")
        List<StandSourceMap> rows = query.list();

        return rows;
    }

	/**
	 * 获取所有的字段
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DataSourceField> getAllFields(Integer sourceId){
		
		String sql = "SELECT  * FROM datasource_field df "
				+ "LEFT OUTER JOIN datasource_table dt ON df.tableid = dt.tableid "
				+ "WHERE dt.resourceId = :sourceId";
		SQLQuery query = getCurrentSession().createSQLQuery(sql).addEntity(DataSourceField.class);
		
		List<DataSourceField> allFields = query.setParameter("sourceId", sourceId).list();
		
		return allFields;
	}
	
	/**
	 * 通过fieldId获取tableId和totalField
	 */
	@Override
	public Object[] getValArrByFieldId(Integer fieldId){
		String sql = "SELECT tableId,COUNT(fieldId) totalField FROM datasource_field WHERE tableId = ("
					+ "SELECT tableId FROM datasource_field WHERE fieldId = :fieldId)";
		
		Object[] arr = (Object[]) getCurrentSession().createSQLQuery(sql).setParameter("fieldId", fieldId).uniqueResult();
		
		return arr;
	}
	
        

    /**
     * 使用数据标准和数据源生成映射对象, 同时生成元数据实体和元数据
     */
    @Override
    public String save(StandSourceMap standSourceMap) {
    	getCurrentSession().save(standSourceMap);
        return standSourceMap.getSsmId();
    }

    /**
     * 删除一个映射, 如果有于此对象相关联的对象存在,不许删除.
     */
    @Override
    public boolean delete(StandSourceMap standSourceMap) {
        getCurrentSession().delete(standSourceMap);
        return true;
    }

}