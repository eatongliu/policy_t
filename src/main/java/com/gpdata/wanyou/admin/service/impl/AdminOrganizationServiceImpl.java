package com.gpdata.wanyou.admin.service.impl;

import com.gpdata.wanyou.admin.dao.AdminOrganizationDao;
import com.gpdata.wanyou.admin.entity.AdminOrganization;
import com.gpdata.wanyou.admin.service.AdminOrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class AdminOrganizationServiceImpl implements AdminOrganizationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminOrganizationServiceImpl.class);
    @Autowired
    private AdminOrganizationDao adminOrganizationDao;

    @Override
    public AdminOrganization createOrganization(AdminOrganization organization) {
        return adminOrganizationDao.createOrganization(organization);
    }

    @Override
    public AdminOrganization updateOrganization(AdminOrganization organization) {
        return adminOrganizationDao.updateOrganization(organization);
    }

    @Override
    public void deleteOrganization(Long organizationId) {
        adminOrganizationDao.deleteOrganization(organizationId);
    }

    @Override
    public AdminOrganization findOne(Long organizationId) {
        return adminOrganizationDao.findOne(organizationId);
    }

    @Override
    public List<AdminOrganization> findAll() {
        return adminOrganizationDao.findAll();
    }

    @Override
    public List<AdminOrganization> findAllWithExclude(AdminOrganization excludeOraganization) {
        return adminOrganizationDao.findAllWithExclude(excludeOraganization);
    }

    @Override
    public void move(AdminOrganization source, AdminOrganization target) {
        adminOrganizationDao.move(source, target);
    }
}
