package com.gpdata.wanyou.md.service.impl;

import com.gpdata.wanyou.base.dao.SimpleDao;
import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.md.dao.MetadataBeanDao;
import com.gpdata.wanyou.md.dao.MetadataEntityDao;
import com.gpdata.wanyou.md.entity.MetadataBean;
import com.gpdata.wanyou.md.entity.MetadataEntity;
import com.gpdata.wanyou.md.service.MetadataBeanService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by chengchao on 2016/10/28.
 */
@Service
public class MetadataBeanServiceImpl extends BaseService implements MetadataBeanService {

    @Autowired
    private MetadataBeanDao metadataBeanDao;

    @Autowired
    private MetadataEntityDao metadataEntityDao;



    @Override
    public Pair<Integer, List<MetadataBean>> getByConditions(Map<String, Object> params) {
        return metadataBeanDao.getByConditions(params);
    }

    @Override
    public void updateMetadataEntities(Integer metadataId, Set<MetadataEntity> metadataEntitySet) {
        metadataBeanDao.updateMetadataEntities(metadataId, metadataEntitySet);
    }

    @Override
    public MetadataBean getMetadataEntitiesByMetadataId(Integer metadataId) {
        MetadataBean metadataBean = this.simpleDao.getById(MetadataBean.class, metadataId);

        List<MetadataEntity> metadataEntityList = this.metadataEntityDao
                .getMetadataEntitiesByMetadataId(metadataId);

        metadataBean.setMetadataEntityList(metadataEntityList);

        return metadataBean;
    }
}
