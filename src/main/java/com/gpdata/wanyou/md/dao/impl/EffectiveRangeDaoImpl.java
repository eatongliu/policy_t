package com.gpdata.wanyou.md.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.md.dao.EffectiveRangeDao;
import com.gpdata.wanyou.md.entity.MetadataInfo;
import org.springframework.stereotype.Repository;

@Repository
public class EffectiveRangeDaoImpl extends BaseDao implements EffectiveRangeDao {


    /**
     * 元数据有效范围   -- 浏览
     *
     * @param metadataid
     * @return
     */
    @Override
    public MetadataInfo getviewRangeById(int metadataid) {
        return (MetadataInfo) this.getCurrentSession().get(MetadataInfo.class, metadataid);
    }

    /**
     * 元数据有效范围   -- 修改
     *
     * @param info
     * @return
     */
    @Override
    public MetadataInfo UpdataRange(MetadataInfo info) {
        this.getCurrentSession().update(info);
        return info;
    }


}
