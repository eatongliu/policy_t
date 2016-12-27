package com.gpdata.wanyou.md.service;

import com.gpdata.wanyou.md.entity.MetadataBean;
import com.gpdata.wanyou.md.entity.MetadataEntity;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by chengchao on 2016/10/28.
 */
public interface MetadataBeanService {

    Pair<Integer, List<MetadataBean>> getByConditions(Map<String, Object> params);

    void updateMetadataEntities(Integer metadataId, Set<MetadataEntity> metadataBeans);

    MetadataBean getMetadataEntitiesByMetadataId(Integer metadataId);
}
