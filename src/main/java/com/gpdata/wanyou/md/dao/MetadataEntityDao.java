package com.gpdata.wanyou.md.dao;

import com.gpdata.wanyou.md.entity.MetadataEntity;
import com.gpdata.wanyou.md.entity.StandSourceMap;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

/**
 * Created by chengchao on 2016/10/28.
 */
public interface MetadataEntityDao {

    Pair<Integer, List<MetadataEntity>> getByConditions(Map<String, Object> params);

    /**
     * 字段、表连表查询生成MetadataEntityList
     * @param standSourceMap
     */
	void generateMetadataEntities(StandSourceMap standSourceMap, String creator);

    /**
     * 根据元数据 id 获取与之相关联的元数据实体列表
     * @param metadataId
     * @return
     */
    List<MetadataEntity> getMetadataEntitiesByMetadataId(Integer metadataId);

    /**
     * 删除元数据实体中和元数据的关系
     * @param entityId
     */
    void deleteEntityMatch(Integer entityId);

    /**
     * 添加元数据实体中和元数据的关系
     * @param metadataId
     * @param entityId
     */
    void addEntityMatch(Integer metadataId, Integer entityId);


    /**
     *
     * @param tableId
     * @return
     */
    List<MetadataEntity> getMetadataEntityListByTableId(Integer tableId);

}
