package com.gpdata.wanyou.md.dao;

import com.gpdata.wanyou.md.entity.MetadataInfo;

public interface EffectiveRangeDao {

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
    public MetadataInfo UpdataRange(MetadataInfo info);
}
