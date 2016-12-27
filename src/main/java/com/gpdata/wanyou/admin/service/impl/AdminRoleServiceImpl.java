package com.gpdata.wanyou.admin.service.impl;


import com.gpdata.wanyou.admin.dao.AdminRoleDao;
import com.gpdata.wanyou.admin.entity.AdminRole;
import com.gpdata.wanyou.admin.service.AdminResourceService;
import com.gpdata.wanyou.admin.service.AdminRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 角色
 */
@Service
public class AdminRoleServiceImpl implements AdminRoleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminRoleServiceImpl.class);
    @Autowired
    private AdminRoleDao roleDao;
    @Autowired
    private AdminResourceService resourceService;

    public AdminRole createAdminRole(AdminRole role) {
        return roleDao.createRole(role);
    }

    public AdminRole updateRole(AdminRole role) {
        return roleDao.updateRole(role);
    }

    public int deleteRole(Long roleId) {
        return roleDao.deleteRole(roleId);
    }

    @Override
    public AdminRole findOne(Long roleId) {
        return roleDao.findOne(roleId);
    }

    @Override
    public List<AdminRole> findAll() {
        return roleDao.findAll();
    }

    @Override
    public List<AdminRole> findAll(Long offset, Long limit) {
        return roleDao.findAll(offset, limit);
    }

    @Override
    public int findAllCount() {
        return roleDao.findAllCount();
    }

    @Override
    public Set<String> findRoles(Long... roleIds) {
        Set<String> roles = new HashSet<String>();
        for (Long roleId : roleIds) {
            AdminRole role = findOne(roleId);
            if (role != null) {
                roles.add(role.getRole());
            }
        }
        return roles;
    }

    @Override
    public Set<String> findPermissions(Long[] roleIds) {
        Set<Long> resourceIds = new HashSet<Long>();
        for (Long roleId : roleIds) {
            AdminRole role = findOne(roleId);
            if (role != null) {
                resourceIds.addAll(role.getResourceIds());
            }
        }
        return resourceService.findPermissions(resourceIds);
    }

}
