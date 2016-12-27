package com.gpdata.wanyou.md.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.md.dao.DataStandEntityDao;
import com.gpdata.wanyou.md.entity.DataStandEntity;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据标准实体DaoImpl
 * <p>
 * Created by ligang on 16-10-25.
 */


@Repository
public class DataStandEntityDaoImpl extends BaseDao implements DataStandEntityDao {


	@Override
    public DataStandEntity getById(Integer standentId) {
		return (DataStandEntity)this.getCurrentSession().get(DataStandEntity.class,standentId);
	}

    @Override
    public int save(DataStandEntity input) {
		Session session = this.getCurrentSession();
		session.save(input);
		session.flush();
        return input.getStandentId();
    }

    @Override
    public void update(DataStandEntity input) {
        this.getCurrentSession().update(input);
    }

	@Override
	public void delete(Integer standentId) {
		DataStandEntity dataStandEntity = (DataStandEntity)this.getCurrentSession().get(DataStandEntity.class,standentId);
		this.getCurrentSession().delete(dataStandEntity);
		
	}

	@Override
	public Map<String, Object> query(DataStandEntity bean,Integer limit,Integer offset) {

		String sql = "select * from data_stand_entity where standentId!=-1  ";
		String totalSql = "select count(standentId) from data_stand_entity where standentId!=-1  ";
		//查询条件
		//数据标准实体-中文名称
		if (StringUtils.isNotBlank(bean.getStandentName())) {
			sql+=" and standentName like :standentName ";
			totalSql+=" and standentName like :standentName ";
		}
		//数据标准实体-英文名称
		if (StringUtils.isNotBlank(bean.getStandentCaption())) {
			sql+=" and standentCaption like :standentCaption ";
			totalSql+=" and standentCaption like :standentCaption ";
		}
		//数据标准实体-数据类型
		if (StringUtils.isNotBlank(bean.getDataType())) {
			sql+=" and dataType like :dataType ";
			totalSql+=" and dataType like :dataType ";
		}
		SQLQuery query = (SQLQuery) getCurrentSession().createSQLQuery(sql).setFirstResult(offset).setMaxResults(limit);
		SQLQuery totalQuery = getCurrentSession().createSQLQuery(totalSql);
		//查询条件
		//数据标准实体-中文名称
		if (StringUtils.isNotBlank(bean.getStandentName())) {
			query.setString("standentName", "%" +bean.getStandentName()+ "%");
			totalQuery.setString("standentName", "%" +bean.getStandentName()+ "%");
		}
		//数据标准实体-英文名称
		if (StringUtils.isNotBlank(bean.getStandentCaption())) {
			query.setString("standentCaption", "%" +bean.getStandentCaption()+ "%");
			totalQuery.setString("standentCaption", "%" +bean.getStandentCaption()+ "%");
		}
		//数据标准实体-数据类型
		if (StringUtils.isNotBlank(bean.getDataType())) {
			query.setString("dataType", "%" +bean.getDataType()+ "%");
			totalQuery.setString("dataType", "%" +bean.getDataType()+ "%");
		}
		
		@SuppressWarnings("unchecked")
		List<DataStandEntity> dataStandEntityList = query.addEntity(DataStandEntity.class).list();
		int total = Integer.parseInt(totalQuery.uniqueResult().toString());
		Map<String, Object> map = new HashMap<>();
		map.put("total", total);
		map.put("rows", dataStandEntityList);
		return map;
    }


	@Override
	public Map<String, Object> getAllByStandId(Integer standId,Integer limit,Integer offset) {

		String sql = "select  * from data_stand_entity where standentId!=-1  ";
		String totalSql = "select  count(standentId) from data_stand_entity where standentId!=-1  ";
		//查询条件
		sql+=" and standId =:standId";
		totalSql+=" and standId =:standId";
	
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		SQLQuery totalQuery = getCurrentSession().createSQLQuery(totalSql);
		if(limit != null && offset != null){
			query = (SQLQuery) getCurrentSession().createSQLQuery(sql).setFirstResult(offset).setMaxResults(limit);
		}
		
		//查询条件
		query.setInteger("standId", standId);
		totalQuery.setInteger("standId", standId);
		@SuppressWarnings("unchecked")
        List<DataStandEntity> dataStandEntityList = query.addEntity(DataStandEntity.class).list();
        String total = totalQuery.uniqueResult().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("rows", dataStandEntityList);
        return map;
	}

}
