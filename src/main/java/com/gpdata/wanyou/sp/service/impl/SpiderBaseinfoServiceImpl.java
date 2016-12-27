package com.gpdata.wanyou.sp.service.impl;

import com.gpdata.wanyou.base.cache.RedisCache;
import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.sp.dao.SpiderBaseinfoDao;
import com.gpdata.wanyou.sp.entity.Spider;
import com.gpdata.wanyou.sp.service.SpiderBaseinfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@SuppressWarnings("rawtypes")
@Service
public class SpiderBaseinfoServiceImpl extends BaseService implements SpiderBaseinfoService {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SpiderBaseinfoDao spiderBaseinfoDao;
    private RedisCache redisCache;

    @Resource
    public void setRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    public List getSpider(int spiderid, int taskid) {
        return spiderBaseinfoDao.getSpider(spiderid, taskid);
    }


    @Override
    public int addSpider(int taskid, String caption, String name,
                         String creator, int depth, int threads, int topn, String remark) {
        Spider spider = new Spider();
        Date now = new Date();
        java.sql.Date date = new java.sql.Date(now.getTime()); //转换为sql.Date对象
        spider.setCaption(caption);
        spider.setName(name);
        spider.setDepth(depth);
        spider.setCreator(creator);
        spider.setThreads(threads);
        spider.setRemark(remark);
        spider.setTopn(topn);
        spider.setCreatedate(date);
        return spiderBaseinfoDao.addSpider(spider);
    }


    @Override
    public void updateSpider(int spiderid, String caption, String name,
                             int depth, int threads, int topn, String remark) {
        Spider spider = (Spider) getSpider(spiderid, 0).get(0);
        Date now = new Date();
        java.sql.Date date = new java.sql.Date(now.getTime()); //转换为sql.Date对象
        if (!caption.equals(""))
            spider.setCaption(caption);
        if (!name.equals(""))
            spider.setName(name);
        if (depth >= 0)
            spider.setDepth(depth);
        if (threads >= 0)
            spider.setThreads(threads);
        if (!remark.equals(""))
            spider.setRemark(remark);
        if (topn >= 0)
            spider.setTopn(topn);
        spider.setRevisedate(date);
        spiderBaseinfoDao.updateSpider(spider);
    }


    @Override
    public void deleteSpider(int spiderid) {
        spiderBaseinfoDao.deleteSpider(spiderid);
    }


    @Override
    public List searchSpider(int taskid, String caption) {
        return spiderBaseinfoDao.searchSpider(taskid, caption);
    }


    @Override
    public int addSpider(Spider spider) {
        Date now = new Date();
        java.sql.Date date = new java.sql.Date(now.getTime()); //转换为sql.Date对象
        spider.setCreatedate(date);
        return spiderBaseinfoDao.addSpider(spider);
    }


    @Override
    public void updateSpider(Spider spider) {
        Date now = new Date();
        java.sql.Date date = new java.sql.Date(now.getTime()); //转换为sql.Date对象
        spider.setRevisedate(date);
        spiderBaseinfoDao.updateSpider(spider);
    }


}
