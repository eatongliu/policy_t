package com.gpdata.wanyou.md.service;

import com.gpdata.wanyou.md.entity.MetadataInfo;

public interface EffectiveRangeService {

    /**
     * 元数据有效范围   -- 浏览
     *
     * @param metadataInfo
     * @return
     */
    public MetadataInfo getviewRangeById(int metadataid);

    /**
     * 元数据有效范围   -- 修改
     *
     * @param Info
     * @return
     */
    public MetadataInfo updateRange(MetadataInfo info);
}
