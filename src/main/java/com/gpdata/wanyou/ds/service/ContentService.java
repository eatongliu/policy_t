package com.gpdata.wanyou.ds.service;

import com.gpdata.wanyou.ds.entity.NewsDto;

@SuppressWarnings("rawtypes")
public interface ContentService {

    /**
     * presto插入文本
     */
    boolean insertDB(NewsDto newsDto);

    /**
     * presto清空表
     */
    boolean clear(NewsDto newsDto);

}