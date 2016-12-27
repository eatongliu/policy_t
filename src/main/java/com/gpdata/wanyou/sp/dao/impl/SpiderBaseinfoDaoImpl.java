package com.gpdata.wanyou.sp.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.sp.dao.SpiderBaseinfoDao;
import com.gpdata.wanyou.sp.entity.Spider;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
@Repository
public class SpiderBaseinfoDaoImpl extends BaseDao implements SpiderBaseinfoDao {


    @SuppressWarnings("unchecked")
    @Override
    public List getSpider(int spiderid, int taskid) {
        List<Spider> list = new ArrayList<Spider>();
        if (spiderid >= 0) {
            list.add((Spider) this.getCurrentSession().get(Spider.class, spiderid));
            return list;
        } else if (spiderid == -1 && taskid >= 0) {
            String sql = "select * from spider_baseinfo where taskid=" + taskid;
            list = getCurrentSession().createSQLQuery(sql).addEntity(Spider.class).list();
            return list;
        }
        return null;
    }

    @Override
    public int addSpider(Spider spider) {
        getCurrentSession().save(spider);
        return spider.getSpiderid();
    }

    @Override
    public void updateSpider(Spider spider) {
        this.getCurrentSession().update(spider);
    }

    @Override
    public void deleteSpider(int spiderid) {
        Spider spider = (Spider) getCurrentSession().get(Spider.class, spiderid);
        getCurrentSession().delete(spider);
    }

    @Override
    public List searchSpider(int taskid, String caption) {
        String sql = "select * from spider_baseinfo where taskid=" + taskid;
        if (!caption.isEmpty()) {
            sql += " and caption like '%" + caption + "%' ";
        }
        return getCurrentSession().createSQLQuery(sql).addEntity(Spider.class).list();
    }

}
