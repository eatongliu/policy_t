package com.gpdata.wanyou.md.service;

import com.gpdata.wanyou.md.entity.MetadataEntity;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by chengchao on 2016/10/28.
 */
public interface MetadataEntityService {

    /**
     * "mmsId" (数据源和数据标准相互关联的 id)
     * "offset"
     * "limit"
     * "caption" (英文名)
     * "cName"  (中文名)
     * "matchStatus" (匹配状态)
     *
     * @param params
     * @return
     */
    Pair<Integer, List<MetadataEntity>> getByConditions(Map<String, Object> params);

    /**
     * 修改元数据实体和元数据映射关系
     *
     * @param metadataId
     * @param entityIdSet
     */
    void updateMetadataEntityMath(Integer metadataId, Set<Integer> entityIdSet);

    /**
     * 根据数据表 id 获取元数据实体
     * @param tableId
     * @return
     */
    List<MetadataEntity> getMetadataEntityListByTableId(Integer tableId);
}
