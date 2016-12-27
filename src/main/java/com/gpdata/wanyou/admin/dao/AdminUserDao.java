package com.gpdata.wanyou.admin.dao;

import com.gpdata.wanyou.admin.entity.AdminUser;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * User: guo
 */
public interface AdminUserDao {

    public AdminUser createUser(AdminUser user);

    public void updateUser(AdminUser user);

    public void deleteUser(AdminUser user);

    AdminUser findOne(Integer userId);

    List<AdminUser> findAll(Map<String, String> maps);

    Integer findAllTotal(Map<String, String> maps);

}
