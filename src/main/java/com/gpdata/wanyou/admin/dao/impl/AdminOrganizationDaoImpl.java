package com.gpdata.wanyou.admin.dao.impl;

import java.util.List;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.admin.dao.AdminOrganizationDao;
import com.gpdata.wanyou.admin.entity.AdminOrganization;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


/**
 *
 */
@Repository
public class AdminOrganizationDaoImpl extends BaseDao implements AdminOrganizationDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminOrganizationDaoImpl.class);

    public AdminOrganization createOrganization(final AdminOrganization adminOrganization) {
        this.getCurrentSession().save(adminOrganization);
        return adminOrganization;
    }

    @Override
    public AdminOrganization updateOrganization(AdminOrganization adminOrganization) {
//        final String sql = "update admin_organization set name=?, parent_id=?, parent_ids=?, available=? where id=?";
//        jdbcTemplate.update(sql, adminOrganization.getName(), adminOrganization.getParentId(),
//                adminOrganization.getParentIds(), adminOrganization.getAvailable(), adminOrganization.getId());
        this.getCurrentSession().merge(adminOrganization);
        return adminOrganization;
    }

    public void deleteOrganization(Long organizationId) {
        AdminOrganization adminOrganization = findOne(organizationId);
        String hql = "DELETE FROM AdminOrganization where 1=1";
        if (organizationId != null) {
            hql += " and id =:organizationId";
        }
        Query query = getCurrentSession().createQuery(hql);
        if (organizationId != null) {
            query.setLong("organizationId", organizationId);
        }
        String deleteDescendantsHql = "DELETE FROM AdminOrganization where 1=1";
        if (adminOrganization.makeSelfAsParentIds() != null) {
            deleteDescendantsHql += " and parent_ids  like:parent_ids";
        }
        Query query2 = getCurrentSession().createQuery(deleteDescendantsHql);
        if (adminOrganization.makeSelfAsParentIds() != null) {
            query2.setString("parent_ids", adminOrganization.makeSelfAsParentIds() + "%");
        }
        query2.executeUpdate();
    }

    @Override
    public AdminOrganization findOne(Long organizationId) {
        return (AdminOrganization) this.getCurrentSession().get(AdminOrganization.class, organizationId);
    }

    @Override
    public List<AdminOrganization> findAll() {
        final String hql = "select id, name, parent_id, parent_ids, available from AdminOrganization";
        Query query = getCurrentSession().createQuery(hql);
        List<AdminOrganization> adminOrganizations = query.list();
        return adminOrganizations;
    }

    @Override
    public List<AdminOrganization> findAllWithExclude(AdminOrganization excludeOraganization) {
        // TODO 改成not exists 利用索引
        final String hql = "select id, name, parent_id, parent_ids, available from admin_organization where id!=:id and parent_ids not like: parent_ids";
        Query query = getCurrentSession().createQuery(hql);
        query.setLong("id", excludeOraganization.getId());
        query.setString("parent_ids", excludeOraganization.makeSelfAsParentIds() + "%");
        List<AdminOrganization> adminOrganizationList = query.list();
        return adminOrganizationList;
    }

    @Override
    public void move(AdminOrganization source, AdminOrganization target) {
        String moveSourceSql = "update AdminOrganization set parent_id=:parent_id,parent_ids=:parent_ids where id=:id";
        Query query = getCurrentSession().createQuery(moveSourceSql);

        query.setLong("parent_id", target.getId());
        query.setString("parent_ids", target.getParentIds());
        query.setLong("id", source.getId());
        query.executeUpdate();
        String moveSourceDescendantsSql = "update AdminOrganization set parent_ids=concat(:targetmakeSelfAsParentIds, substring(parent_ids, length(:sourcemakeSelfAsParentIds))) where parent_ids like :sourcemakeSelfAsParentIds";
        Query query2 = getCurrentSession().createQuery(moveSourceDescendantsSql);

        query2.setString("targetmakeSelfAsParentIds", target.makeSelfAsParentIds());
        query2.setString("sourcemakeSelfAsParentIds", source.makeSelfAsParentIds());
        query2.setString("sourcemakeSelfAsParentIds", source.makeSelfAsParentIds() + "%");
        query2.executeUpdate();
    }
}
