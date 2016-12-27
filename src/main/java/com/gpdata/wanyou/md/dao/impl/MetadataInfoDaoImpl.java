package com.gpdata.wanyou.md.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.md.dao.MetadataInfoDao;
import com.gpdata.wanyou.md.entity.MetadataInfo;
import com.gpdata.wanyou.utils.SimpleBeanPropertiesUtil;
import org.eclipse.jetty.util.StringUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chengchao
 */
@Repository
public class MetadataInfoDaoImpl extends BaseDao implements MetadataInfoDao {

    /*
     * (non-Javadoc)
     * @see com.gpdata.wanyou.md.dao.MetadataInfoDao#getById(int)
     */
    @Override
    public MetadataInfo getById(Integer metadataid) {

        MetadataInfo result = (MetadataInfo) this.getCurrentSession().get(MetadataInfo.class, metadataid);
        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.gpdata.wanyou.md.dao.MetadataInfoDao#save(com.gpdata.wanyou.md.entity.MetadataInfo)
     */
    @Override
    public MetadataInfo save(MetadataInfo input) {
        this.getCurrentSession().save(input);

        return input;
    }

    /*
     * (non-Javadoc)
     * @see com.gpdata.wanyou.md.dao.MetadataInfoDao#update(com.gpdata.wanyou.md.entity.MetadataInfo)
     */
    @Override
    public MetadataInfo update(MetadataInfo input) {

        MetadataInfo original = this.getById(input.getMetadataid());

        if (original == null) {
            throw new RuntimeException("被修改的对象不存在!");
        }
        SimpleBeanPropertiesUtil.copyNotNullProperties(input, original);

        this.getCurrentSession().update(original);

        return original;
    }

    /*
     * (non-Javadoc)
     * @see com.gpdata.wanyou.md.dao.MetadataInfoDao#delete(com.gpdata.wanyou.md.entity.MetadataInfo)
     */
    @Override
    public void delete(MetadataInfo input) {
        this.getCurrentSession().delete(input);
    }

    private StringBuilder assemblingCond(MetadataInfo input) {

        StringBuilder cond = new StringBuilder();
        cond.append("where ");
        String caption = input.getCaption();
        if (StringUtil.isNotBlank(caption)) {
            cond.append(" a.caption like :caption and ");
        }

        Integer dialectid = input.getDialectid();
        if (dialectid != null) {
            cond.append(" a.dialectid = :dialectid and ");
        }

        Integer fieldid = input.getFieldid();
        if (fieldid != null) {
            cond.append(" a.fieldid = :fieldid and ");
        }

        String metaname = input.getMetaname();
        if (StringUtil.isNotBlank(metaname)) {
            cond.append(" a.metaname = :metaname and ");
        }

        Integer ontologyid = input.getOntologyid();
        if (ontologyid != null) {
            cond.append(" a.ontologyid = :ontologyid and ");
        }

        // 如果有条件, 删除最后的 "and "
        // 否则什么也没有
        if (cond.length() > 6) {
            cond.delete(cond.length() - 4, cond.length());
        } else {
            cond.delete(0, cond.length());
        }

        return cond;
    }

    private Query assemblingCond(Query query, MetadataInfo input) {

        String caption = input.getCaption();
        if (StringUtil.isNotBlank(caption)) {
            query.setString("caption", "%" + caption + "%");
        }
        Integer dialectid = input.getDialectid();
        if (dialectid != null) {

            query.setInteger("dialectid", dialectid);
        }
        Integer fieldid = input.getFieldid();
        if (fieldid != null) {

            query.setInteger("fieldid", fieldid);
        }
        String metaname = input.getMetaname();
        if (StringUtil.isNotBlank(metaname)) {
            query.setString("metaname", metaname);
        }

        Integer ontologyid = input.getOntologyid();
        if (ontologyid != null) {
            query.setInteger("ontologyid", ontologyid);
        }
        return query;
    }

    /*
     * 说明：检索元数据信息
     * 参数1：caption标题
     * 参数2：dialectid方言id
     * 参数3：ontologyid本体id
     * 参数4：fieldid字段id
     * 参数5：metaname元数据标识
     * 
     * (non-Javadoc)
     * @see com.gpdata.wanyou.md.dao.MetadataInfoDao#queryTotal(com.gpdata.wanyou.md.entity.MetadataInfo)
     */
    @Override
    public Integer queryTotal(MetadataInfo input) {

        StringBuilder buff = new StringBuilder();
        buff.append("select count(a) from MetadataInfo a ");

        StringBuilder cond = this.assemblingCond(input);
        buff.append(cond);

        Session session = this.getCurrentSession();
        Query query = session.createQuery(buff.toString());
        query = assemblingCond(query, input);

        String result = query.uniqueResult().toString();

        return Integer.valueOf(result, 10);

    }

    /*
     * (non-Javadoc)
     * @see com.gpdata.wanyou.md.dao.MetadataInfoDao#queryRows(com.gpdata.wanyou.md.entity.MetadataInfo)
     */
    @Override
    public List<MetadataInfo> queryRows(MetadataInfo input) {

        StringBuilder buff = new StringBuilder();
        buff.append("select a from MetadataInfo a ");

        StringBuilder cond = this.assemblingCond(input);
        buff.append(cond);
        buff.append(" order by a.metadataid desc ");

        Session session = this.getCurrentSession();
        Query query = session.createQuery(buff.toString());
        query = assemblingCond(query, input);

        @SuppressWarnings("unchecked")
        List<MetadataInfo> result = query.list();

        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.gpdata.wanyou.md.dao.MetadataInfoDao#updateRelyon(com.gpdata.wanyou.md.entity.MetadataInfo)
     */
    @Override
    public MetadataInfo updateRelyon(MetadataInfo info) {
        this.getCurrentSession().update(info);

        return info;
    }

    @Override
    public String getMetadataSynonyms(Integer metadataid) {
        MetadataInfo result = (MetadataInfo) this.getCurrentSession().get(MetadataInfo.class, metadataid);
        return result.getSynmetadataids();
    }


    /**
     * 查找影响分析相关数据:
     * <p>
     * 查找数据库中 MetadataInfo 表的 depmetadataid 属性包含 metadataId 的数据
     *
     * @param metadataId
     * @return
     */
    @Override
    public List<MetadataInfo> getAffectMetadataInfoList(Integer metadataId) {

        String hql = "from MetadataInfo a where a.depmetadataid like :metadataId ";
        Session session = this.getCurrentSession();
        List<MetadataInfo> result = session.createQuery(hql)
                .setString("metadataId", "%" + metadataId + "%")
                .list();

        return result;
    }

    /**
     * 查找一个本体对象所直接关联的元数据对象的列表
     *
     * @param ontologyId
     * @return
     */
    @Override
    public List<MetadataInfo> getMetadataInfosByOntologyId(Integer ontologyId) {

        String hql = " from MetadataInfo a where a.ontologyid = :ontologyId ";
        Session session = this.getCurrentSession();
        List<MetadataInfo> result = session.createQuery(hql)
                .setInteger("ontologyId", ontologyId)
                .list();

        return result;
    }
}
