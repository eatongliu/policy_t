package com.gpdata.wanyou.sp.service;

import com.gpdata.wanyou.sp.entity.SpiderBaseInfo;

import java.util.List;

/**
 * Created by guoxy on 2016/11/1.
 */
public interface SpiderWorkService {
    SpiderBaseInfo getSpider(int spiderid);

    String addSpider(SpiderBaseInfo spider);

    void updateSpider(SpiderBaseInfo spider);

    void deleteSpider(int spiderid);

    List searchSpider(int taskid, String caption);

    /**
     * Redis的cache中一律采用Object类型。
     * Key：spiderid
     * Value：Spider对象（3个属性：taskname任务名，depth:深度，remark:备注）
     * （2）spiderid生成规则：
     * 任务名的拼音首字母(全小写)_自增数。
     * 如：任务名：天涯博客
     * Spiderid：tybk_001
     * 如果spiderid为空则新增，否则覆盖原对象
     *
     * @param key
     * @param baseInfo
     * @param expiration
     * @return
     */
    public String savaSpiderCache(SpiderBaseInfo baseInfo, int expiration);

    public SpiderBaseInfo getSpiderCache(String key);
}