package com.gpdata.wanyou.md.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gpdata.wanyou.base.dao.SimpleDao;
import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.ds.dao.ResourceDao;
import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.md.dao.DataStandardDao;
import com.gpdata.wanyou.md.dao.MetadataBeanDao;
import com.gpdata.wanyou.md.dao.MetadataCompareDao;
import com.gpdata.wanyou.md.dao.MetadataEntityDao;
import com.gpdata.wanyou.md.dao.StandSourceMapDao;
import com.gpdata.wanyou.md.entity.DataStandard;
import com.gpdata.wanyou.md.entity.MetadataBean;
import com.gpdata.wanyou.md.entity.MetadataCompare;
import com.gpdata.wanyou.md.entity.MetadataEntity;
import com.gpdata.wanyou.md.entity.StandSourceMap;
import com.gpdata.wanyou.md.service.StandSourceMapService;

import javax.xml.crypto.Data;

import static com.gpdata.wanyou.md.entity.StandSourceMap.DATA_STATUS_LOCK;

/**
 * 数据标准数据源映射
 *
 * @author acer_liuyutong
 */
@Service
public class StandSourceMapServiceImpl extends BaseService implements StandSourceMapService {

    @Autowired
    private StandSourceMapDao standSourceMapDao;
	
    @Autowired
    private DataStandardDao dataStandardDao;
	
    @Autowired
    private ResourceDao resourceDao;
	
    @Autowired
    private SimpleDao simpleDao;
    
    @Autowired
    private MetadataCompareDao metadataCompareDao;
    
    @Autowired
    private MetadataEntityDao metadataEntityDao;
	
    @Autowired
    private MetadataBeanDao metadataBeanDao;
    
    /**
	 * 查询映射信息
	 */
	@Override
    public Pair<Integer, List<StandSourceMap>> queryStandSourceMaps(String caption, int limit, int offset) {
		
        Integer total = standSourceMapDao.queryTotal(caption, limit, offset);
        List<StandSourceMap> rows = standSourceMapDao.queryRows(caption, limit, offset);
		
        return Pair.of(total, rows);
    }
	
    /**
     * 删除一个映射, 如果有于此对象相关联的对象存在,不许删除.
     */
    @Override
    public boolean delete(String ssmId) {

        StandSourceMap standSourceMap = standSourceMapDao.getById(ssmId);

        if (standSourceMap == null) {
            return true;
        }

        String dataStatus = standSourceMap.getDataStatus();

        if (DATA_STATUS_LOCK.equals(dataStatus)) {
            throw new RuntimeException("锁定状态不许删除！");
        }

        Integer standId = standSourceMap.getStandId();
        Integer resourceId = standSourceMap.getSourceId();

        Map<String, Object> params = new HashMap<>();
        params.put("ssmId", ssmId);
        params.put("offset", 0);
        params.put("limit", 1);


        Pair<Integer, List<MetadataEntity>> pair = metadataEntityDao.getByConditions(params);

        if (pair.getLeft() > 0) {
            throw new RuntimeException("映射相关联的数据标准仍存在，不可删除！");
        }

        /*
        DataStandard standard = dataStandardDao.getById(standId);
        DataSourceResource resource = resourceDao.getDataSourceById(resourceId);
        if (standard != null) {
            throw new RuntimeException("映射相关联的数据标准仍存在，不许删除！");
        }

        if (resource != null) {
            throw new RuntimeException("映射相关联的数据源仍存在，不许删除！");
        }
        */
        standSourceMapDao.delete(standSourceMap);

        return true;
    }
    
    /**
     * 使用数据标准和数据源生成映射对象, 同时生成元数据实体、元数据和比对结果
     */
    @Override
    public String saveStandSourceMap(StandSourceMap standSourceMap) {
		
        Integer standId = standSourceMap.getStandId();
        Integer sourceId = standSourceMap.getSourceId();


        if (simpleDao.getById(DataStandard.class, standId) == null) {
            throw new RuntimeException("数据标准 (id : "+ standId + ") 不存在!");
        }

        if (simpleDao.getById(DataSourceResource.class, sourceId) == null) {
            throw new RuntimeException("数据源 (id : "+ sourceId + ") 不存在!");
        }

        /*
         * 保存映射 (数据标准 - 数据源映射)
         */
        standSourceMap.setSsmId(standId+"_"+sourceId);
        String ssmId = standSourceMapDao.save(standSourceMap);
        
        Date date = new Date();
        String userId = standSourceMap.getUserId();
        
        //根据字段表生成MetadataCompare
        metadataCompareDao.generateMetadataCompares(sourceId);
        
        
        //根据字段表生成元数据实体
        metadataEntityDao.generateMetadataEntities(standSourceMap, userId);

		//元数据实体、标准实体连表查询并进行比对生成元数据，遍历元数据列表
        List<MetadataBean> allMetadataBeans = metadataBeanDao.generateMetadataBeanList(standId,sourceId);

		for (MetadataBean metadataBean : allMetadataBeans) {

            metadataBean.setUserId(userId);
            metadataBean.setIsStand("是");
            metadataBean.setCreateDate(date);
            
            //保存元数据
            Integer metadataBeanId = (Integer) metadataBeanDao.saveMetadataBean(metadataBean);
            
            Integer entityId = metadataBean.getStandMetadataEntityId();
            MetadataEntity metadataEntity = simpleDao.getById(MetadataEntity.class, entityId);
			
            //更新元数据实体
            metadataEntity.setIsStand("是");
            metadataEntity.setMatchStatus(20);
            metadataEntity.setMetadataId(metadataBeanId);
            simpleDao.update(metadataEntity);
            
            //比对结果标准字段加1
            MetadataCompare mc = metadataCompareDao.getMcByFieldId(metadataEntity.getFieldId());
			metadataCompareDao.incrStandField(mc);
        }	
        return ssmId;
    }
}