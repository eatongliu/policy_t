package com.gpdata.wanyou.admin.dao.impl;

import java.util.List;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.admin.dao.AdminRoleDao;
import com.gpdata.wanyou.admin.entity.AdminRole;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Role:权限
 */
@Repository
public class AdminRoleDaoImpl extends BaseDao implements AdminRoleDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminRoleDaoImpl.class);

    public AdminRole createRole(final AdminRole role) {
        this.getCurrentSession().save(role);
        return role;
    }

    @Override
    public AdminRole updateRole(AdminRole role) {
        this.getCurrentSession().merge(role);
        return role;
    }

    public int deleteRole(Long roleId) {
        final String sql = "delete from AdminRole where id=:id";
        Query query = getCurrentSession().createQuery(sql);
        query.setLong("id", roleId);

        return query.executeUpdate();
    }

    @Override
    public AdminRole findOne(Long roleId) {
        return (AdminRole) this.getCurrentSession().get(AdminRole.class, roleId);
    }

    @Override
    public List<AdminRole> findAll() {
        final String sql = "select id, role, description, resource_ids as resourceIdsStr, available from AdminRole";
        Query query = getCurrentSession().createQuery(sql);
        return query.list();
    }

    @Override
    public List<AdminRole> findAll(Long offset, Long limit) {
        final String sql = "select id, role, description, resource_ids as resourceIdsStr, available from AdminRole";
        Query query = getCurrentSession().createQuery(sql);
        return query.setFirstResult((int) (long) offset)
                .setMaxResults((int) (long) limit).list();
    }

    @Override
    public int findAllCount() {
        final String sql = "select count(*) from AdminRole";
        Query query = getCurrentSession().createQuery(sql);
        String total = query.uniqueResult().toString();
        return Integer.valueOf(total, 10);
    }

}
