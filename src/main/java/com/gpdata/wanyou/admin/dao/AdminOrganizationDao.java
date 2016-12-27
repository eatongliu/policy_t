package com.gpdata.wanyou.admin.dao;

import com.gpdata.wanyou.admin.entity.AdminOrganization;

import java.util.List;


/**
 * 组织
 */
public interface AdminOrganizationDao {

    public AdminOrganization createOrganization(AdminOrganization adminOrganization);

    public AdminOrganization updateOrganization(AdminOrganization adminOrganization);

    public void deleteOrganization(Long organizationId);

    AdminOrganization findOne(Long organizationId);

    List<AdminOrganization> findAll();

    List<AdminOrganization> findAllWithExclude(AdminOrganization excludeOraganization);

    void move(AdminOrganization source, AdminOrganization target);
}
