package com.gpdata.wanyou.admin.service.impl;

import com.gpdata.wanyou.admin.dao.SiteConfigDao;
import com.gpdata.wanyou.admin.entity.SiteConfig;
import com.gpdata.wanyou.admin.service.SiteConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by ligang 2016/12/10.
 */
@Service
public class SiteConfigServiceImpl implements SiteConfigService {

    @Resource
    private SiteConfigDao siteConfigDao;

    @Override
    public SiteConfig getSiteConfig() {
        return siteConfigDao.getSiteConfig();
    }


}
