package com.gpdata.wanyou.md.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.md.dao.DataStandardDao;
import com.gpdata.wanyou.md.entity.DataStandard;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据标准
 *
 * @author acer_liuyutong
 */
@Repository
public class DataStandardDaoImpl extends BaseDao implements DataStandardDao {

    /**
     * 获得一个数据标准的详细信息
     *
     * @param standId
     * @return
     */
    @Override
    public DataStandard getById(Integer standId) {
        return (DataStandard) this.getCurrentSession().get(DataStandard.class, standId);
    }

    /**
     * 保存一个新对象
     *
     * @param dataStandard
     * @return
     */
    @Override
    public Integer save(DataStandard dataStandard) {
        this.getCurrentSession().save(dataStandard);
        return dataStandard.getStandId();
    }

    /**
     * query 查询, 无条件等同于列表显示
     *
     * @param offset
     * @param limit
     * @return
     */
    @Override
    public Integer getDataStandardTotal(String standName, int offset, int limit) {

        String hql = "select count(d) from DataStandard d ";

        if (StringUtils.isNotBlank(standName)) {
            hql += "where d.standName like :standName ";
        }

        Query query = getCurrentSession().createQuery(hql);

        if (StringUtils.isNotBlank(standName)) {
            query.setString("standName", "%" + standName + "%");
        }

        String total = query.uniqueResult().toString();

        return Integer.valueOf(total, 10);
    }

    @Override
    public List<DataStandard> getDataStandardRows(String standName, int offset, int limit) {

        String hql = "from DataStandard d ";

        if (StringUtils.isNotBlank(standName)) {
            hql += "where d.standName like :standName ";
        }

        Query query = getCurrentSession().createQuery(hql);

        if (StringUtils.isNotBlank(standName)) {
            query.setString("standName", "%" + standName + "%");
        }

        query.setFirstResult(offset).setMaxResults(limit);

        @SuppressWarnings("unchecked")
        List<DataStandard> rows = query.list();

        return rows;
    }

    /**
     * 删除一个对象
     *
     * @param standId
     */
    @Override
    public void delete(Integer standId) {
        DataStandard dataStandard = (DataStandard) getCurrentSession().get(DataStandard.class, standId);
        getCurrentSession().delete(dataStandard);
    }

    /**
     * 更新一个对象
     *
     * @param dataStandard
     */
    @Override
    public void update(DataStandard dataStandard) {
        this.getCurrentSession().update(dataStandard);
    }

    @Override
    public List<DataSourceResource> getNonMappingDataSourceResources(Integer standId) {

        String hql = "select a from DataSourceResource a ";
        if (standId != null) {
            hql += " where a.resourceId not in (select b.sourceId from StandSourceMap b "
                    + " where b.standId = :standId ) ";
        }

        hql += " order by a.resourceId asc ";

        Session session = this.getCurrentSession();
        Query query = session.createQuery(hql);

        if (standId != null) {
            query.setInteger("standId", standId);
        }
        @SuppressWarnings("unchecked")
        List<DataSourceResource> result =  query.list();
        return result;
    }
}