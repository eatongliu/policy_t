package com.gpdata.wanyou.admin.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.admin.dao.SiteConfigDao;
import com.gpdata.wanyou.admin.entity.SiteConfig;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

/**
 * Created by ligang on 2016/12/10.
 */
@Repository

public class SiteConfigDaoImpl extends BaseDao implements SiteConfigDao {

    @Override
    public SiteConfig getSiteConfig() {
        String sql = "SELECT t.* from site_config t limit 0,1";
        SQLQuery query = getCurrentSession().createSQLQuery(sql);
        SiteConfig siteConfig = (SiteConfig) query.uniqueResult();
        return siteConfig;
    }

}
