package com.gpdata.wanyou.admin.dao;


import com.gpdata.wanyou.admin.entity.AdminResource;

import java.util.List;


/**
 * <p>
 * Resource: 资源
 */
public interface AdminResourceDao {

    public AdminResource createResource(AdminResource adminResource);

    public AdminResource updateResource(AdminResource adminResource);

    public int deleteResource(Long resourceId);

    AdminResource findOne(Long resourceId);

    List<AdminResource> findAll();

}
