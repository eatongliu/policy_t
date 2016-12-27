package com.gpdata.wanyou.sp.service;

import com.gpdata.wanyou.sp.entity.Spider;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface SpiderBaseinfoService {


    List getSpider(int spiderid, int taskid);

    int addSpider(int taskid, String caption, String name,
                  String creator, int depth, int threads, int topn,
                  String remark);

    void updateSpider(int spiderid, String caption, String name,
                      int depth, int threads, int topn,
                      String remark);

    void deleteSpider(int spiderid);

    List searchSpider(int taskid, String caption);

    int addSpider(Spider spider);

    void updateSpider(Spider spider);


}
