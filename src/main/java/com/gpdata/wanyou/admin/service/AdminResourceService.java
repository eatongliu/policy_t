package com.gpdata.wanyou.admin.service;


import java.util.List;
import java.util.Set;

import com.gpdata.wanyou.admin.entity.AdminResource;

public interface AdminResourceService {


    public AdminResource createResource(AdminResource resource);

    public AdminResource updateResource(AdminResource resource);

    public int deleteResource(Long resourceId);

    AdminResource findOne(Long resourceId);

    List<AdminResource> findAll();

    /**
     * 得到资源对应的权限字符串
     *
     * @param resourceIds
     * @return
     */
    Set<String> findPermissions(Set<Long> resourceIds);

    /**
     * 根据用户权限得到菜单
     *
     * @param permissions
     * @return
     */
    List<AdminResource> findMenus(Set<String> permissions);

    /**
     * 根据用户权限得到z子菜单
     *
     * @param permissions
     * @return
     */
    List<AdminResource> findChildMenus(Set<String> permissions);
}
