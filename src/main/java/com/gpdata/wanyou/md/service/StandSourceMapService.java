package com.gpdata.wanyou.md.service;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.gpdata.wanyou.md.entity.StandSourceMap;

/**
 * @author acer_liuyutong
 */
public interface StandSourceMapService {

    /**
     * 查询映射信息
     */
    public Pair<Integer, List<StandSourceMap>> queryStandSourceMaps(String caption, int limit, int offset);

    /**
     * 使用数据标准和数据源生成映射对象, 同时生成元数据实体和元数据
     * 返回 id
     */
    String saveStandSourceMap(StandSourceMap standSourceMap);

    /**
     * 删除一个映射, 如果有于此对象相关联的对象存在,不许删除.
     */
    public boolean delete(String ssmId);

}