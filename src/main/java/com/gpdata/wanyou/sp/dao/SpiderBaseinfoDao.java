package com.gpdata.wanyou.sp.dao;

import com.gpdata.wanyou.sp.entity.Spider;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface SpiderBaseinfoDao {


    List getSpider(int spiderid, int taskid);

    int addSpider(Spider spider);

    void updateSpider(Spider spider);

    void deleteSpider(int spiderid);

    List searchSpider(int taskid, String caption);

}
