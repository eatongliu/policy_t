package com.gpdata.wanyou.admin.dao.impl;

import java.util.List;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.admin.dao.AdminResourceDao;
import com.gpdata.wanyou.admin.entity.AdminResource;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


/**
 *
 */
@Repository
public class AdminResourceDaoImpl extends BaseDao implements AdminResourceDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminResourceDaoImpl.class);

    public AdminResource createResource(final AdminResource adminResource) {
        this.getCurrentSession().save(adminResource);
        return adminResource;
    }

    @Override
    public AdminResource updateResource(AdminResource adminResource) {
        this.getCurrentSession().merge(adminResource);
        return adminResource;
    }

    public int deleteResource(Long resourceId) {
        AdminResource adminResource = findOne(resourceId);
        final String deleteSelfSql = "delete from AdminResource where id =:id";
        Query query = getCurrentSession().createQuery(deleteSelfSql);
        query.setLong("id", resourceId);
        query.executeUpdate();
        final String deleteDescendantsSql = "delete from AdminResource where parent_ids like :parent_ids";
        Query query2 = getCurrentSession().createQuery(deleteDescendantsSql);
        query2.setString("parent_ids", adminResource.makeSelfAsParentIds() + "%");
        int flag = query2.executeUpdate();
        return flag;
    }

    @Override
    public AdminResource findOne(Long resourceId) {
        return (AdminResource) this.getCurrentSession().get(AdminResource.class, resourceId);
    }

    @Override
    public List<AdminResource> findAll() {
        final String sql = "select id, name, type, url, permission, parent_id, parent_ids, available from AdminResource order by concat(parent_ids, id) asc";
        Query query = getCurrentSession().createQuery(sql);
        List<AdminResource> adminResourceList = query.list();
        return adminResourceList;
    }

}
