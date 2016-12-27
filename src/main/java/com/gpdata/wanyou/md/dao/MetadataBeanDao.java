package com.gpdata.wanyou.md.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.gpdata.wanyou.md.entity.MetadataBean;
import com.gpdata.wanyou.md.entity.MetadataEntity;

/**
 * Created by chengchao on 2016/10/28.
 */
public interface MetadataBeanDao {

    Pair<Integer, List<MetadataBean>> getByConditions(Map<String, Object> params);

    void updateMetadataEntities(Integer metadataId, Set<MetadataEntity> metadataBeans);

    /**
     * 元数据实体、标准实体连表查询并进行比对生成元数据
     * @param standId
     * @param sourceId
     * @return 
     */
	List<MetadataBean> generateMetadataBeanList(Integer standId, Integer sourceId);

	/**
	 * 保存并刷新
	 */
	Integer saveMetadataBean(MetadataBean metadataBean);

	/**
	 * 使用本体 id 获取与之相关联的元数据
	 * @param ontologyId
	 * @return
	 */
    List<MetadataBean> getMetadataBeansByOntologyId(Integer ontologyId);

    /**
     * 删除元数据中关联本体的关系数据
     * @param metadataId
     */
	void deleteBeanMatch(Integer metadataId);

    /**
     * 添加元数据中本体的关联数据
     * @param ontologyId
     * @param metadataId
     */
	void addBeanMatch(Integer ontologyId, Integer metadataId);
}
