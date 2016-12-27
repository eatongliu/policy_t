package com.gpdata.wanyou.md.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.md.dao.MetadataDao;
import com.gpdata.wanyou.md.entity.MetadataDialect;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MetadataDaoImpl extends BaseDao implements MetadataDao {

    /**
     * 方言--显示具体某个方言的详细信息
     *
     * @param dialectid
     * @return
     */
    @Override
    public MetadataDialect getMetadataDialectById(int dialectid) {

        return (MetadataDialect) this.getCurrentSession().get(MetadataDialect.class, dialectid);
    }

    /**
     * 方言--新增
     *
     * @param caption remark
     * @return
     */
    @Override
    public MetadataDialect addMetadataDialect(MetadataDialect metadataDialect) {
        getCurrentSession().save(metadataDialect);
        return metadataDialect;
    }

    /**
     * 方言--修改方言信息
     *
     * @param map
     * @return
     */
    @Override
    public MetadataDialect updateMetadataDialect(MetadataDialect metadataDialect) {
        this.getCurrentSession().update(metadataDialect);
        return metadataDialect;
    }

    /**
     * 方言--删除某个方言
     *
     * @param dialectid方言id
     */


    @Override
    public void deleteMetadataDialect(MetadataDialect metadataDialect) {
        //MetadataDialect metadataDialect1 =(MetadataDialect)getCurrentSession().get(MetadataDialect.class, metadataDialect);
        getCurrentSession().delete(metadataDialect);
    }


    /**
     * 方言 -- 检索方言信息
     *
     * @param caption标题 成功：检索方言信息列表，caption模糊匹配
     *                  失败：[“error”:”错误原因”]
     */

    @SuppressWarnings("rawtypes")
    @Override
    public Map<String, List> qMetadata(String caption, int limit, int offset) {
        int start, stop;
        start = offset;
        stop = start + limit;
        String sql = "select SQL_CALC_FOUND_ROWS * from metadata_dialect where 1 = 1 ";
        if (!caption.isEmpty()) {
            sql += " and caption like '%" + caption + "%' ";
        }
        sql += " limit " + start + "," + stop;
        @SuppressWarnings("unchecked")
        List<MetadataDialect> metadataDialectlist = getCurrentSession().createSQLQuery(sql).addEntity(MetadataDialect.class).list();
        String totalSql = "SELECT FOUND_ROWS()";
        List total = getCurrentSession().createSQLQuery(totalSql).list();
        Map<String, List> map = new HashMap<String, List>();
        map.put("total", total);
        map.put("rows", metadataDialectlist);
        return map;
    }
}
