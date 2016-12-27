package com.gpdata.wanyou.admin.service;

import java.util.List;
import java.util.Set;

import com.gpdata.wanyou.admin.entity.AdminRole;

/**
 * 角色
 */
public interface AdminRoleService {


    public AdminRole createAdminRole(AdminRole AdminRole);

    public AdminRole updateRole(AdminRole AdminRole);

    public int deleteRole(Long AdminRoleId);

    public AdminRole findOne(Long AdminRoleId);

    public List<AdminRole> findAll();

    public List<AdminRole> findAll(Long offset, Long limit);

    public int findAllCount();

    /**
     * 根据角色编号得到角色标识符列表
     *
     * @param AdminRoleIds
     * @return
     */
    Set<String> findRoles(Long... roleIds);

    /**
     * 根据角色编号得到权限字符串列表
     *
     * @param AdminRoleIds
     * @return
     */
    Set<String> findPermissions(Long[] AdminRoleIds);
}
