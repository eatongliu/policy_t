package com.gpdata.wanyou.sp.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.sp.dao.SpiderWorkDao;
import com.gpdata.wanyou.sp.entity.SpiderBaseInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by guoxy on 2016/11/1.
 */
@Repository
public class SpiderWorkDaoImpl extends BaseDao implements SpiderWorkDao {
    @Override
    public SpiderBaseInfo getSpider(int spiderid) {
        return (SpiderBaseInfo) this.getCurrentSession().get(SpiderBaseInfo.class, spiderid);
    }

    @Override
    public String addSpider(SpiderBaseInfo spider) {
        getCurrentSession().save(spider);
        return spider.getSpiderId();
    }

    @Override
    public void updateSpider(SpiderBaseInfo spider) {
        getCurrentSession().merge(spider);
    }

    @Override
    public void deleteSpider(int spiderid) {
        SpiderBaseInfo spider = (SpiderBaseInfo) getCurrentSession().get(SpiderBaseInfo.class, spiderid);
        getCurrentSession().delete(spider);
    }

    @Override
    public List searchSpider(int taskid, String caption) {
        return null;
    }
}
