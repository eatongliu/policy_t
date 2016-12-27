package com.gpdata.wanyou.head.dao;

import java.util.List;
import java.util.Map;

/**
 * 首页图片dao
 *
 * @author yaz
 * @create 2016-12-16 11:05
 */
public interface PictureDao {
    /**
     * 获取3张图所有数据，分别1,2,3表示
     * 后期可删
     */
    Map<String, List> getAll();
}
