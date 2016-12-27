package com.gpdata.wanyou.md.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.md.dao.MetadataCheckDao;
import com.gpdata.wanyou.md.entity.MetadataCheck;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chengchao on 16-9-30.
 */
@Repository
public class MetadataCheckDaoImpl extends BaseDao implements MetadataCheckDao {


    /**
     * 说明：显示具体某个元数据的验证信息。
     * 参数：metadataid元数据id（必填）
     * 成功：表metadata_check中指定metadataid的记录
     * 失败：[“error”:”错误原因”]
     *
     * @param metadataCheck
     * @return
     */
    @Override
    public MetadataCheck getMetadataCheck(MetadataCheck metadataCheck) {

        Session session = this.getCurrentSession();
        MetadataCheck result = (MetadataCheck) session.get(MetadataCheck.class, metadataCheck.getCheckid());
        return result;


    }

    /**
     * 说明：在元数据验证信息表中新增记录。
     * 参数1：metadataid元数据id（必填）
     * 参数2：checkformula验证公式
     * 成功：[“id”:元数据验证信息的id]
     * 失败：[“error”:”错误原因”]
     *
     * @param metadataCheck
     * @return
     */
    @Override
    public MetadataCheck addMetadataCheck(MetadataCheck metadataCheck) {

        this.getCurrentSession().save(metadataCheck);
        return metadataCheck;


    }

    /**
     * 说明：修改元数据验证信息
     * 参数1：checkid元数据验证id（必填）
     * 参数2：checkformula验证公式
     * 成功：”success”
     * 失败：[“error”:”错误原因”]
     *
     * @param metadataCheck
     */
    @Override
    public void updateMetadataCheck(MetadataCheck metadataCheck) {

        this.getCurrentSession().update(metadataCheck);

    }

    /**
     * 说明：删除某个元数据验证信息
     * 参数：checkid验证id（必填）
     * 成功：”success”
     * 失败：[“error”:”错误原因”]
     *
     * @param metadataCheck
     */
    @Override
    public void deleteMetadataCheck(MetadataCheck metadataCheck) {
        this.getCurrentSession().delete(metadataCheck);
    }

    /**
     * 说明：检索元数据验证信息
     * 参数：metadataid 元数据id
     * 成功：总数
     *
     * @param metadataCheck
     * @return
     */
    @Override
    public Integer queryMetadataCheckTotal(MetadataCheck metadataCheck) {

        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(MetadataCheck.class);
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq("metadataid", metadataCheck.getMetadataid()));

        String result = criteria.uniqueResult().toString();

        return Integer.valueOf(result, 10);

    }

    /**
     * 说明：检索元数据验证信息
     * 参数：metadataid元数据id
     * 成功：列表
     *
     * @param metadataCheck
     * @param offset
     * @param size
     * @return
     */
    @Override
    public List<MetadataCheck> queryMetadataCheckRows(MetadataCheck metadataCheck, Integer offset, Integer size) {

        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(MetadataCheck.class);

        criteria.add(Restrictions.eq("metadataid", metadataCheck.getMetadataid()));
        criteria.setMaxResults(size);
        criteria.setFirstResult((offset - 1) * size);

        List<MetadataCheck> result = criteria.list();

        return result;
    }
}
