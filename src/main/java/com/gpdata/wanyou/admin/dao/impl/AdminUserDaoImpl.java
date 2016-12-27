package com.gpdata.wanyou.admin.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.admin.dao.AdminUserDao;
import com.gpdata.wanyou.admin.entity.AdminUser;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * User: guo
 */
@Repository
public class AdminUserDaoImpl extends BaseDao implements AdminUserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminUserDaoImpl.class);

    public AdminUser createUser(final AdminUser user) {
        return (AdminUser) this.getCurrentSession().save(user);

    }

    public void updateUser(AdminUser user) {
        this.getCurrentSession().merge(user);
    }

    public void deleteUser(AdminUser user) {
        this.getCurrentSession().delete(user);
    }

    @Override
    public AdminUser findOne(Integer userId) {
        return (AdminUser) this.getCurrentSession().get(AdminUser.class, userId);
    }

    @Override
    public List<AdminUser> findAll(Map<String, String> maps) {
//        String sql = "select admin_id, organization_id, admin_username,admin_loginname, admin_password,  admin_registtime, admin_lastlogintime,admin_roleid as roleIdsStr, locked from admin_user";
//        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(AdminUser.class));
        String adminUsername = maps.get("username");
        String hql = "from AdminUser where 1=1";
        if (adminUsername != null) {
            hql += " and adminUsername =:adminUsername";
        }
        Query query = getCurrentSession().createQuery(hql);
        if (adminUsername != null) {
            query.setString("adminUsername", adminUsername);
        }
        return query.list();
    }

    @Override
    public Integer findAllTotal(Map<String, String> maps) {
//        String sql = "select admin_id, organization_id, admin_username,admin_loginname, admin_password,  admin_registtime, admin_lastlogintime,admin_roleid as roleIdsStr, locked from admin_user";
//        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(AdminUser.class));
        String adminUsername = maps.get("username");
        String hql = "SELECT count(*) from AdminUser where 1=1";
        if (adminUsername != null) {
            hql += " and adminUsername =:adminUsername";
        }
        Query query = getCurrentSession().createQuery(hql);
        if (adminUsername != null) {
            query.setString("adminUsername", adminUsername);
        }
        String total = query.uniqueResult().toString();
        return Integer.valueOf(total, 10);
    }

}
