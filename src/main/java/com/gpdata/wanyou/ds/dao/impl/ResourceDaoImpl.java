package com.gpdata.wanyou.ds.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.ds.dao.ResourceDao;
import com.gpdata.wanyou.ds.entity.DataSourceResource;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ResourceDaoImpl extends BaseDao implements ResourceDao {
    /**
     * 根据id查询数据源
     */
    @Override
    public DataSourceResource getDataSourceById(int resourceId) {
        return (DataSourceResource) this.getCurrentSession().get(DataSourceResource.class, resourceId);

    }

    /**
     * 获取实体集合，不分页，不分条件
     * 在更新所有数据源时调用，此外禁止使用此方法
     */
    @Override
    public List<DataSourceResource> getAllDataSourceWithoutLimit() {
        String hql = "from DataSourceResource";
        Query query = getCurrentSession().createQuery(hql);
        return query.list();
    }

    /**
     * 传输数据源对象更新数据源
     */
    @Override
    public void updateDataSource(DataSourceResource dataSource) {
        this.getCurrentSession().update(dataSource);
    }

    /**
     * 新增传入数据源对象
     */
    @Override
    public int addDataSource(DataSourceResource dataSource) {
        getCurrentSession().save(dataSource);
        return dataSource.getResourceId();
    }

    /**
     * 获取实体集合
     */
    @Override
    public List<DataSourceResource> getDataSourceList(Map<String, String> params) {
        //查询条件
        String position = params.get("position");
        String resourceType = params.get("resourceType");
        String caption = params.get("caption");
        //分页条件
        String limit = params.get("limit");
        String offset = params.get("offset");

        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(DataSourceResource.class);

        criteria.add(Restrictions.eq("resourceType", resourceType));
        if (null != position && !position.equals("")) {
            criteria.add(Restrictions.eq("position", position));
        }
        if (null != caption && !caption.equals("")) {
            criteria.add(Restrictions.like("caption", "%" + caption + "%"));
        }

        criteria.setFirstResult(Integer.parseInt(offset));
        criteria.setMaxResults(Integer.parseInt(limit));

        List<DataSourceResource> resultList = criteria.list();
        return resultList;
    }

    /**
     * 获取实体总数
     */
    @Override
    public int getTotalDataSource(Map<String, String> params) {
        //查询条件
        String position = params.get("position");
        String resourceType = params.get("resourceType");
        String caption = params.get("caption");

        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(DataSourceResource.class);
        criteria.setProjection(Projections.rowCount());

        criteria.add(Restrictions.eq("resourceType", resourceType));
        if (null != position && !position.equals("")) {
            criteria.add(Restrictions.eq("position", position));
        }
        if (null != caption && !caption.equals("")) {
            criteria.add(Restrictions.like("caption", "%" + caption + "%"));
        }

        String result = criteria.uniqueResult().toString();
        return Integer.valueOf(result, 10);
    }

    @Override
    public List<Map<String, Object>> getAllDataSourceIdAndNameList(String resourceType) {
        String sql = "select dr.resourceId , dr.caption from DataSourceResource dr where dr.resourceType = :resourceType ";
        Session session = this.getCurrentSession();
        Query query = session.createQuery(sql);
        query.setString("resourceType", resourceType);
        return query.list();
    }

    /**
     * 查一个 entityId 所对应的数据源
     * @param entityId
     * @return
     */
    @Override
    public DataSourceResource getDataSourceByEntityId(Integer entityId) {
        String hql = "select a from DataSourceResource a "
                + " , DataSourceTable b, DataSourceField c "
                + " , MetadataEntity d "
                + " where a.resourceId = b.resourceId "
                + " and b.tableId = c.tableId "
                + " and c.fieldId = d.fieldId "
                + " and d.entityId = :entityId "
                ;
        return (DataSourceResource) this.getCurrentSession()
                .createQuery(hql)
                .setInteger("entityId", entityId)
                .uniqueResult();
    }


}
