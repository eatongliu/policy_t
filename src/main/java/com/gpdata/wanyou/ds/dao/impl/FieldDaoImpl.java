package com.gpdata.wanyou.ds.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.ds.dao.FieldDao;
import com.gpdata.wanyou.ds.entity.DataSourceField;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据字段持久层
 *
 * @author qyl
 */
@Repository
public class FieldDaoImpl extends BaseDao implements FieldDao {

    /**
     * 通过ID查询数据字段
     *
     * @param fieldId
     * @return
     */
    @Override
    public DataSourceField getDataFieldById(Integer fieldId) {
        return (DataSourceField) this.getCurrentSession().get(DataSourceField.class, fieldId);
    }


    /**
     * 关键字模糊查询数据字段
     *
     * @param tableId
     * @param caption
     * @param fieldName
     * @param limit
     * @param offset
     * @return
     */
    @SuppressWarnings({"unchecked"})
    @Override
    public Map<String, Object> getDataTableColumns(Integer tableId, String caption,
                                                   String fieldName, Integer limit, Integer offset) {
        String sql = "select df from DataSourceField df  where  df.tableId=:tableId "
                + " and df.caption like :caption "
                + " and df.fieldName like :fieldName ";
        Query query = getCurrentSession().createQuery(sql)
                .setInteger("tableId", tableId)
                .setString("caption", "%" + caption + "%")
                .setString("fieldName", "%" + fieldName + "%")
                .setFirstResult(offset)
                .setMaxResults(limit);
        List<DataSourceField> dataSourceFieldList = query.list();

        String totalSql = "select count(df) from DataSourceField df  where  df.tableId=:tableId "
                + " and df.caption like :caption "
                + " and df.fieldName like :fieldName ";
        Integer total = Integer.valueOf(getCurrentSession().createQuery(totalSql)
                .setInteger("tableId", tableId)
                .setString("caption", "%" + caption + "%")
                .setString("fieldName", "%" + fieldName + "%")
                .uniqueResult().toString(), 10);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("rows", dataSourceFieldList);
        return map;
    }

    @Override
    public void addDataField(DataSourceField dataSourceField) {
        getCurrentSession().save(dataSourceField);
    }

    @Override
    public void updateDataField(DataSourceField dataSourceField) {
        getCurrentSession().update(dataSourceField);
    }

    @Override
    public List<DataSourceField> getDataFieldByTableId(Integer tableId) {
        String sql = "from DataSourceField df where df.tableId = :tableId";
        Query query = getCurrentSession().createQuery(sql).setInteger("tableId", tableId);
        return query.list();
    }
}
