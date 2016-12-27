package com.gpdata.wanyou.admin.service;


import java.util.List;

import com.gpdata.wanyou.admin.entity.AdminOrganization;


public interface AdminOrganizationService {


    public AdminOrganization createOrganization(AdminOrganization organization);

    public AdminOrganization updateOrganization(AdminOrganization organization);

    public void deleteOrganization(Long organizationId);

    AdminOrganization findOne(Long organizationId);

    List<AdminOrganization> findAll();

    Object findAllWithExclude(AdminOrganization excludeOraganization);

    void move(AdminOrganization source, AdminOrganization target);
}
