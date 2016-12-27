package com.gpdata.wanyou.sp.dao;

import com.gpdata.wanyou.sp.entity.SpiderBaseInfo;

import java.util.List;

/**
 * Created by guoxy on 2016/11/1.
 */
public interface SpiderWorkDao {
    SpiderBaseInfo getSpider(int spiderid);

    String addSpider(SpiderBaseInfo spider);

    void updateSpider(SpiderBaseInfo spider);

    void deleteSpider(int spiderid);

    List searchSpider(int taskid, String caption);
}
