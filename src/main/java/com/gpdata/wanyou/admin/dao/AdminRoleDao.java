package com.gpdata.wanyou.admin.dao;

import com.gpdata.wanyou.admin.entity.AdminRole;

import java.util.List;


/**
 * <p>
 * User: 权限
 */
public interface AdminRoleDao {

    public AdminRole createRole(AdminRole role);

    public AdminRole updateRole(AdminRole role);

    public int deleteRole(Long roleId);

    public AdminRole findOne(Long roleId);

    public List<AdminRole> findAll();

    public List<AdminRole> findAll(Long offset, Long limit);

    public int findAllCount();
}
