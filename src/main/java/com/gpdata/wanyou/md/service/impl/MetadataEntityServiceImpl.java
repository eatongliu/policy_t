package com.gpdata.wanyou.md.service.impl;

import com.gpdata.wanyou.base.dao.SimpleDao;
import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.md.dao.MetadataEntityDao;
import com.gpdata.wanyou.md.entity.MetadataBean;
import com.gpdata.wanyou.md.entity.MetadataEntity;
import com.gpdata.wanyou.md.service.MetadataEntityService;
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
public class MetadataEntityServiceImpl extends BaseService implements MetadataEntityService {

    @Autowired
    private MetadataEntityDao metadataEntityDao;

    @Autowired
    private SimpleDao simpleDao;

    /**
     * "ssmId" (数据源和数据标准相互关联的 id) 肯定有
     * "offset" 肯定有
     * "limit" 肯定有
     * <p>
     * "caption" (英文名)
     * "cName"  (中文名)
     * "matchStatus" (匹配状态)
     *
     * @param params
     * @return
     */
    @Override
    public Pair<Integer, List<MetadataEntity>> getByConditions(Map<String, Object> params) {
        return metadataEntityDao.getByConditions(params);
    }


    /**
     * 修改元数据实体和元数据映射关系
     * @param metadataId
     * @param entityIdSet
     */
    @Override
    public void updateMetadataEntityMath(Integer metadataId, Set<Integer> entityIdSet) {

        MetadataBean metadataBean = this.simpleDao.getById(MetadataBean.class, metadataId);

        if (metadataBean == null) {
            return;
        }
        List<MetadataEntity> entityList = metadataEntityDao.getMetadataEntitiesByMetadataId(metadataId);
        entityList.stream()
                .map(MetadataEntity::getEntityId)
                .filter(entityId -> !entityIdSet.contains(entityId))
                .forEach(entityId ->
                    this.metadataEntityDao.deleteEntityMatch(entityId));

        entityIdSet.forEach(entityId -> {
            this.metadataEntityDao.addEntityMatch(metadataId, entityId);
        });

    }


    /**
     *
     * @param tableId
     * @return
     */
    @Override
    public List<MetadataEntity> getMetadataEntityListByTableId(Integer tableId) {
        return metadataEntityDao.getMetadataEntityListByTableId(tableId);
    }
}
