package com.gpdata.wanyou.admin.service.impl;

import com.gpdata.wanyou.admin.dao.AdminResourceDao;
import com.gpdata.wanyou.admin.entity.AdminResource;
import com.gpdata.wanyou.admin.service.AdminResourceService;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 */
@Service
public class AdminResourceServiceImpl implements AdminResourceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminResourceServiceImpl.class);
    @Autowired
    private AdminResourceDao adminResourceDao;

    @Override
    public AdminResource createResource(AdminResource resource) {
        return adminResourceDao.createResource(resource);
    }

    @Override
    public AdminResource updateResource(AdminResource resource) {
        return adminResourceDao.updateResource(resource);
    }

    @Override
    public int deleteResource(Long resourceId) {
        return adminResourceDao.deleteResource(resourceId);
    }

    @Override
    public AdminResource findOne(Long resourceId) {
        return adminResourceDao.findOne(resourceId);
    }

    @Override
    public List<AdminResource> findAll() {
        return adminResourceDao.findAll();
    }

    @Override
    public Set<String> findPermissions(Set<Long> resourceIds) {
        Set<String> permissions = new HashSet<String>();
        for (Long resourceId : resourceIds) {
            AdminResource resource = findOne(resourceId);
            if (resource != null && !StringUtils.isEmpty(resource.getPermission())) {
                permissions.add(resource.getPermission());
            }
        }
        return permissions;
    }

    @Override
    public List<AdminResource> findMenus(Set<String> permissions) {
        List<AdminResource> allResources = findAll();
        List<AdminResource> menus = new ArrayList<AdminResource>();
        for (AdminResource resource : allResources) {
            if (resource.isRootNode()) {
                continue;
            }
            if (resource.getType() != AdminResource.ResourceType.menu) {
                continue;
            }
            if (!hasPermission(permissions, resource)) {
                continue;
            }
            menus.add(resource);
        }
        return menus;
    }

    private boolean hasPermission(Set<String> permissions, AdminResource resource) {
        if (StringUtils.isEmpty(resource.getPermission())) {
            return true;
        }
        for (String permission : permissions) {
            WildcardPermission p1 = new WildcardPermission(permission);
            WildcardPermission p2 = new WildcardPermission(resource.getPermission());
            if (p1.implies(p2) || p2.implies(p1)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<AdminResource> findChildMenus(Set<String> permissions) {

        List<AdminResource> allResources = findAll();
        List<AdminResource> menus = new ArrayList<AdminResource>();
        for (AdminResource resource : allResources) {
            if (resource.isRootNode()) {
                continue;
            }
            if (resource.getType() != AdminResource.ResourceType.button) {
                continue;
            }
            if (!hasPermission(permissions, resource)) {
                continue;
            }
            menus.add(resource);
        }
        return menus;
    }
}
