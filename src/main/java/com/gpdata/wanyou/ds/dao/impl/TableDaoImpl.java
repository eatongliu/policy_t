package com.gpdata.wanyou.ds.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.ds.constant.DataSourceConstant;
import com.gpdata.wanyou.ds.dao.TableDao;
import com.gpdata.wanyou.ds.entity.DataSourceTable;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据表Dao层接口的实现
 *
 * @author quyili
 */
@SuppressWarnings("rawtypes")
@Repository
public class TableDaoImpl extends BaseDao implements TableDao {


    /**
     * 获取数据表表名列表
     *
     * @param resourceId
     * @param caption
     * @return
     */
    @Override
    public List getDtIdAndNames(Integer resourceId, String caption) {
        String sql = "select dt.tableId,dt.tableName from DataSourceTable dt where "
                + " dt.resourceId=:resourceId "
                + " and dt.caption like :caption ";
        Query query = getCurrentSession().createQuery(sql)
                .setInteger("resourceId", resourceId)
                .setString("caption", "%" + caption + "%");
        List dataTableList = query.list();
        return dataTableList;
    }


    /**
     * 通过ID查询数据表
     *
     * @param tableId
     * @return
     */
    @Override
    public DataSourceTable getDataTableById(Integer tableId) {
        return (DataSourceTable) this.getCurrentSession().get(DataSourceTable.class, tableId);
    }


    /**
     * 关键字查询数据表
     *
     * @param resourceid
     * @param caption
     * @param limit
     * @param offset
     * @return
     */
    @SuppressWarnings({"unchecked"})
    @Override
    public Map<String, Object> getDataTableByKeyword(Integer resourceId, String caption, Integer limit, Integer offset) {
        String sql = "select dt from DataSourceTable dt where "
                + " dt.resourceId=:resourceId "
                + " and dt.caption like :caption ";
        Query query = getCurrentSession().createQuery(sql)
                .setInteger("resourceId", resourceId)
                .setString("caption", "%" + caption + "%")
                .setFirstResult(offset)
                .setMaxResults(limit);
        List<DataSourceTable> dataTableList = query.list();

        String totalSql = "select count(dt) from DataSourceTable dt where "
                + " dt.resourceId=:resourceId "
                + " and dt.caption like :caption ";
        Integer total = Integer.valueOf(getCurrentSession().createQuery(totalSql)
                .setInteger("resourceId", resourceId)
                .setString("caption", "%" + caption + "%")
                .uniqueResult().toString(), 10);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("rows", dataTableList);
        return map;
    }

    @Override
    public void addDataTable(DataSourceTable table) {
        getCurrentSession().save(table);
    }

    @Override
    public void updataDataTable(DataSourceTable table) {
        getCurrentSession().update(table);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DataSourceTable> getTableListByDsId(Integer dataSourceId) {
        String sql = "from DataSourceTable dt where dt.resourceId = :resourceId";
        Query query = getCurrentSession().createQuery(sql).setInteger("resourceId", dataSourceId);
        return query.list();
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<String> getDtNameList(Integer resourceId) {
        String sql = "select dt.tableName from DataSourceTable dt where "
                + " dt.resourceId=:resourceId ";
        Query query = getCurrentSession().createQuery(sql)
                .setInteger("resourceId", resourceId);
        List<String> dataTableList = query.list();
        return dataTableList;
    }

    @Override
    public List<DataSourceTable> getTableListOfAutoUpdate() {
        String sql = "from DataSourceTable dt where dt.isAutoUpdate = :isAutoUpdate";
        Query query = getCurrentSession().createQuery(sql).setString("isAutoUpdate", DataSourceConstant.YES);
        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DataSourceTable> getDataSourceTableList(Integer resourceId) {

        String hql = "from DataSourceTable a where a.resourceId = :resourceId order by a.tableId ";

        return this.getCurrentSession()
                .createQuery(hql)
                .setInteger("resourceId", resourceId)
                .list();
    }
}
