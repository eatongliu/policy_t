package com.gpdata.wanyou.md.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.md.dao.OntologyDao;
import com.gpdata.wanyou.md.entity.OntologyBaseinfo;
import com.gpdata.wanyou.md.entity.OntologyGroup;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OntologyDaoImpl extends BaseDao implements OntologyDao {

    @Override
    public OntologyGroup getGroupById(int groupid) {
        return (OntologyGroup) this.getCurrentSession().get(OntologyGroup.class, groupid);
    }

    @Override
    public int addOntologyGroup(OntologyGroup ontologyGroup) {
        getCurrentSession().save(ontologyGroup);
        return ontologyGroup.getGroupId();
    }

    @Override
    public void updateOntologyGroup(OntologyGroup ontologyGroup) {
        this.getCurrentSession().update(ontologyGroup);
    }

    @Override
    public void deleteOntologyGroup(int groupid) {

        Session session  = this.getCurrentSession();

        OntologyGroup ontologyGroup = (OntologyGroup) session.get(OntologyGroup.class, groupid);

        if (ontologyGroup != null) {
            session.delete(ontologyGroup);
        }
    }


    /*
     *
     */
    @Override
    public Pair<Integer, List<OntologyGroup>> searchOntologyGroup(String caption, Integer limit, Integer offset) {

        Session session = this.getCurrentSession();
        String sql = "from OntologyGroup  ";
        if (StringUtils.isNotBlank(caption)) {
            sql += " where caption like :caption";
        }
        Query query = session.createQuery(sql)
                .setFirstResult(offset)
                .setMaxResults(limit);
        if (StringUtils.isNotBlank(caption)) {
            query.setString("caption", "%" + caption + "%");
        }


        List<OntologyGroup> ontologyGroupList = query.list();

        sql = "select count(a) from OntologyGroup a ";
        if (StringUtils.isNotBlank(caption)) {
            sql += " where a.caption like :caption";
        }
        query = session.createQuery(sql);
        if (StringUtils.isNotBlank(caption)) {
            query.setString("caption", "%" + caption + "%");
        }

        String total = query.uniqueResult().toString();

        return Pair.of(Integer.valueOf(total, 10), ontologyGroupList);
    }

    @Override
    public OntologyBaseinfo getOntologyById(int ontologyid) {
        return (OntologyBaseinfo) this.getCurrentSession().get(OntologyBaseinfo.class, ontologyid);
    }

    @Override
    public int addOntology(OntologyBaseinfo ontology) {
        getCurrentSession().save(ontology);
        return ontology.getOntologyId();
    }

    @Override
    public void updateOntology(OntologyBaseinfo ontology) {
        this.getCurrentSession().update(ontology);
    }

    /**
     * 删除单个本体
     * @param ontologyId
     */
    @Override
    public void deleteOntology(Integer ontologyId) {

        Session session = this.getCurrentSession();

        OntologyBaseinfo ontology = (OntologyBaseinfo) session.get(OntologyBaseinfo.class, ontologyId);

        if ( ontology != null) {
            /* 将与本体相关联的元数据的外键清空 */
            String hql = "update MetadataBean set ontologyId = null where ontologyId = :ontologyId ";
            Query query = session.createQuery(hql);
            query.setInteger("ontologyId", ontologyId);
            query.executeUpdate();
            /* 删除本体 */
            session.delete(ontology);
            session.flush();
        }
    }


    /**
     *
     * @param bean 必须有 groupId (本体组 id), 可以有标题.
     * @param limit
     * @param offset
     * @return
     */
    @Override
    public Pair<Integer, List<OntologyBaseinfo>> searchOntologyBaseinfo(OntologyBaseinfo bean, Integer limit,
                                                                        Integer offset) {

        String hql = " from OntologyBaseinfo a " +
                " where a.groupId = :groupId ";

        if (StringUtils.isNotBlank(bean.getCaption())) {
            hql += " and a.caption like :caption  ";
        }

        Session session = this.getCurrentSession();

        Query query = session.createQuery(hql + " order by a.ontologyId desc ")
                .setInteger("groupId", bean.getGroupId())
                .setFirstResult(offset)
                .setMaxResults(limit);

        if (StringUtils.isNotBlank(bean.getCaption())) {
            query.setString("caption", "%" + bean.getCaption() + "%");
        }

        @SuppressWarnings({"unchecked"})
        List<OntologyBaseinfo> ontologyList = query.list();


        hql = "select count(a) " + hql;

        query = session.createQuery(hql )
                .setInteger("groupId", bean.getGroupId());

        if (StringUtils.isNotBlank(bean.getCaption())) {
            query.setString("caption", "%" + bean.getCaption() + "%");
        }


        String total = query.uniqueResult().toString();


        return Pair.of(Integer.valueOf(total, 10), ontologyList);
    }


}
