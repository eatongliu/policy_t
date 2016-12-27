package com.gpdata.wanyou.md.service.impl;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.md.dao.EffectiveRangeDao;
import com.gpdata.wanyou.md.entity.MetadataInfo;
import com.gpdata.wanyou.md.service.EffectiveRangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EffectiveRangeServiceImpl extends BaseService implements EffectiveRangeService {

    @Autowired
    private EffectiveRangeDao effectiveRangeDao;

    /**
     * 元数据有效范围   -- 浏览
     *
     * @param metadataid
     * @return
     */
    @Override
    public MetadataInfo getviewRangeById(int metadataid) {

        return effectiveRangeDao.getviewRangeById(metadataid);
    }

    /**
     * 元数据有效范围   -- 修改
     *
     * @param bean
     * @return
     */
    @Override
    public MetadataInfo updateRange(MetadataInfo bean) {

        return effectiveRangeDao.UpdataRange(bean);
    }

}
