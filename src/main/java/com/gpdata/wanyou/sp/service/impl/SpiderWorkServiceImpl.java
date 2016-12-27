package com.gpdata.wanyou.sp.service.impl;

import com.alibaba.fastjson.JSON;
import com.gpdata.wanyou.base.cache.RedisCache;
import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.sp.dao.SpiderWorkDao;
import com.gpdata.wanyou.sp.entity.SpiderBaseInfo;
import com.gpdata.wanyou.sp.service.SpiderWorkService;
import com.gpdata.wanyou.utils.PinyinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.List;

/**
 * 爬虫任务
 * Created by guoxy on 2016/11/1.
 */
@Service
public class SpiderWorkServiceImpl extends BaseService implements SpiderWorkService {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private SpiderWorkDao spiderWorkDao;

    @Override
    public SpiderBaseInfo getSpider(int spiderid) {
        return spiderWorkDao.getSpider(spiderid);
    }

    @Override
    public String addSpider(SpiderBaseInfo spider) {
        return spiderWorkDao.addSpider(spider);
    }

    @Override
    public void updateSpider(SpiderBaseInfo spider) {
        spiderWorkDao.updateSpider(spider);
    }

    @Override
    public void deleteSpider(int spiderid) {
        spiderWorkDao.deleteSpider(spiderid);
    }

    @Override
    public List searchSpider(int taskid, String caption) {
        /*TODO*/
        return null;
    }

    @Override
    public String savaSpiderCache(SpiderBaseInfo baseInfo, int expiration) {
        redisCache.add(baseInfo.getSpiderId(), baseInfo, 30000);
        return baseInfo.getSpiderId();
    }

    @Override
    public SpiderBaseInfo getSpiderCache(String key) {
        SpiderBaseInfo sp1 = (SpiderBaseInfo) redisCache.get(key, SpiderBaseInfo.class);
        LOGGER.debug("sp1:{}", sp1);
        return (SpiderBaseInfo) redisCache.get(key, SpiderBaseInfo.class);


    }
}
