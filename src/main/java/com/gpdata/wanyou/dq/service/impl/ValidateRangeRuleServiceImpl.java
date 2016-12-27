package com.gpdata.wanyou.dq.service.impl;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.dq.dao.ValidateRangeRuleDao;
import com.gpdata.wanyou.dq.dto.ValidateRangeRuleDto;
import com.gpdata.wanyou.dq.entity.ValidateDataSource;
import com.gpdata.wanyou.dq.entity.ValidateRangeRecord;
import com.gpdata.wanyou.dq.entity.ValidateRangeRule;
import com.gpdata.wanyou.dq.service.ValidateRangeRuleService;
import com.gpdata.wanyou.dq.utils.ForeignDs2InnerDsCreator;
import com.gpdata.wanyou.ds.dao.FieldDao;
import com.gpdata.wanyou.ds.dao.ResourceDao;
import com.gpdata.wanyou.ds.dao.TableDao;
import com.gpdata.wanyou.ds.entity.DataSourceField;
import com.gpdata.wanyou.ds.entity.DataSourceResource;
import com.gpdata.wanyou.ds.entity.DataSourceTable;
import com.gpdata.wanyou.md.dao.MetadataEntityDao;
import com.gpdata.wanyou.md.entity.MetadataEntity;
import com.gpdata.wanyou.utils.ConfigUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gpdata.wanyou.dq.dao.ValidateRangeRecordDao;
import com.gpdata.wanyou.dq.dao.ValidateRangeRuleDao;
import com.gpdata.wanyou.dq.dto.ValidateRangeRuleDto;
import com.gpdata.wanyou.dq.entity.ValidateRangeRule;
import com.gpdata.wanyou.dq.service.ValidateRangeRuleService;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Predicate;


@Service
@Transactional(rollbackForClassName = { "RuntimeException", "Exception" })
public class ValidateRangeRuleServiceImpl extends BaseService implements ValidateRangeRuleService{
    
    @Resource
    private ValidateRangeRuleDao validateRangeRuleDao;
    @Resource
    private ValidateRangeRecordDao validateRangeRecordDao;
    @Resource
    private ResourceDao resourceDao;
    @Resource
    private TableDao tableDao;
    @Resource
    private FieldDao fieldDao;
    @Resource
    private MetadataEntityDao metadataEntityDao;


    private static final ConcurrentHashMap<Integer, ReentrantLock> reentrantLockHolder = new ConcurrentHashMap<>();


    /**
     *
     * 这个方法增加了对 ValidateDataSource 是否存在的判断,
     * 如果存在, 说明内部数据源已经创建. 不做任何事
     * 如果不存在, 说明内部数据源还没有创建, 则 :
     * 创建一个内部数据源,
     * 复制数据表的记录
     * 复制字段的记录, 复制元数据实体的记录, 然后,
     * 在内部创建一个数据库, 使用源数据的名字加下划线加数据源 id 拼接成的名称
     * 再创建其中的数据库表
     *
     * 如果这段代码好用, 那它就是程超写的, 如果不好用, 那就不知道是谁写的了 ...
     * 我说清楚了么?
     *
     * @param input
     * @return
     */
    @Override
    public int save(ValidateRangeRule input) {

        int id = validateRangeRuleDao.save(input);
        /* TODO : 需要重构
         * 这里增加了对 ValidateDataSource 是否存在的判断.
         * 是因为没有显式建立内部数据源的触发操作, 只能暂时在这里隐式触发.
         * 以后的重构中需要修改这里 !!!
         */

        final Integer entityId = input.getEntityId();

        ValidateDataSource validateDataSource = this.validateRangeRuleDao
                .getValidateDataSourceByEntityId(entityId);

        if (validateDataSource == null) {
            new Thread(() -> this.createInnerDataSourceAndOther(entityId))
                    .start();

        }

        return id;
    }


    @Override
    public void createInnerDataSourceAndOther(Integer entityId) {


        DataSourceResource dataSourceResource = this.resourceDao.getDataSourceByEntityId(entityId);
        if (dataSourceResource == null) {
            logger.warn("数据源不存在 ... 方法返回.");
            return;
        }

        logger.debug("dataSourceResource.position : {}", dataSourceResource.getPosition());
        if ("inner".equalsIgnoreCase(dataSourceResource.getPosition())) {
            logger.warn("数据源 (id : {}, name : {}) ... 它是一个内部数据源 ... 方法返回."
                    , dataSourceResource.getResourceId(), dataSourceResource.getCaption());
            return;
        }

        Integer resourceId = dataSourceResource.getResourceId();
        ReentrantLock lock = reentrantLockHolder
                .computeIfAbsent(resourceId, (Void) -> new ReentrantLock());

        /* 如果发现该操作已经在执行中则不再执行 */
        if (lock.tryLock()) {
            try {
                this.execute(dataSourceResource);
            } finally {
                reentrantLockHolder.remove(resourceId);
                lock.unlock();
            }
        } else {
            logger.warn("不能获取到锁 ... ");
        }
    }

    private void execute(DataSourceResource dataSourceResource) {

        Date current = new Date();
        String hostandport = ConfigUtil.getConfig("project.jdbc.hostandport");
        String userName = ConfigUtil.getConfig("project.jdbc.username");
        String passWord = ConfigUtil.getConfig("project.jdbc.password");
        String[] hostAndPort = hostandport.split(":");
        String host = hostAndPort[0].trim();
        Integer port = Integer.valueOf(hostAndPort[1].trim(), 10);

        logger.debug("创建一个内部数据源");
        DataSourceResource innerDataSource = new DataSourceResource();

        /* 使用 spring BeanUtils.copyProperties */
        logger.debug("spring BeanUtils copyProperties ...");
        BeanUtils.copyProperties(dataSourceResource, innerDataSource);
        Integer resourceId = dataSourceResource.getResourceId();
        String dbName = dataSourceResource.getDbName() + "_" + resourceId;
        innerDataSource.setReviseDate(current);
        innerDataSource.setCreateDate(current);
        innerDataSource.setHost(host);
        innerDataSource.setUserName(userName);
        innerDataSource.setPassWord(passWord);
        innerDataSource.setPort(port);

        innerDataSource.setResourceId(null);
        innerDataSource.setPosition("inner");
        innerDataSource.setDbName(dbName);


        Integer innerDataSourceId = (Integer) this.simpleDao.save(innerDataSource, true);
        logger.debug("保存新内部数据源并返回主键 ... id : {}", innerDataSourceId);

        List<DataSourceTable> dataSourceTableList = this.tableDao.getTableListByDsId(resourceId);
        logger.debug("获取数据源关联的数据表 ... size : {}", dataSourceTableList.size());

        Map<Integer, List<DataSourceField>> dataSourceFieldMap = new HashMap<>();

        for (DataSourceTable dst : dataSourceTableList) {

            List<DataSourceField> dataSourceFieldList = dataSourceFieldMap.get(dst.getTableId());

            if (dataSourceFieldList == null) {
                dataSourceFieldList = new ArrayList<>();
                dataSourceFieldMap.put(dst.getTableId(), dataSourceFieldList);
            }

            logger.debug("创建一个内部数据表");
            DataSourceTable innerTable = new DataSourceTable();
            BeanUtils.copyProperties(dst, innerTable);
            innerTable.setTableId(null);
            innerTable.setCreateDate(current);
            innerTable.setReviseDate(current);
            innerTable.setResourceId(innerDataSourceId);
            innerTable.setStatus("正常");

            Integer innerTableId = (Integer) this.simpleDao.save(innerTable, true);
            logger.debug("保存新内部数据表并返回主键 ... id : {}", innerTableId);

            List<MetadataEntity> metadataEntityList = this.metadataEntityDao
                    .getMetadataEntityListByTableId(dst.getTableId());
            logger.debug("使用数据表 id 获取元数据实体列表 ... size : {}", metadataEntityList.size());

            for (MetadataEntity metadataEntity : metadataEntityList) {

                DataSourceField dsf = this.simpleDao.getById(DataSourceField.class, metadataEntity.getFieldId());

                logger.debug("创建一个内部数据字段");
                DataSourceField innerField = new DataSourceField();
                BeanUtils.copyProperties(dsf, innerField);
                innerField.setFieldId(null);
                innerField.setTableId(innerTableId);
                Integer innerFieldId = (Integer) this.simpleDao.save(innerField, true);
                logger.debug("保存新内部数据字段并返回主键 ... id : {}", innerFieldId);

                logger.debug("创建一个新的元数据实体");
                MetadataEntity innerMetadataEntity = new MetadataEntity();
                BeanUtils.copyProperties(metadataEntity, innerMetadataEntity);
                innerMetadataEntity.setEntityId(null);
                innerMetadataEntity.setCreateDate(current);
                innerMetadataEntity.setFieldId(innerFieldId);
                innerMetadataEntity.setSourceId(innerDataSourceId);
                Integer innerMetadataEntityId = (Integer) this.simpleDao.save(innerMetadataEntity, true);
                logger.debug("保存新的元数据实体并返回主键 ... id : {}", innerMetadataEntityId);
            }

        }

        logger.debug("根据外部数据源创建内部数据库");
        boolean status = ForeignDs2InnerDsCreator.createInnerDbFromDataSource(dataSourceResource
                , dataSourceTableList, dataSourceFieldMap
                , innerDataSource);

        if (status == false) {
            logger.debug("建库失败, 也许需要人工干预, 方法返回 ...");

            return;
        }

        ValidateDataSource validateDataSource = new ValidateDataSource();
        validateDataSource.setCreateTime(current);
        validateDataSource.setForeignDbsId(dataSourceResource.getResourceId());
        validateDataSource.setForeignDbsName(dataSourceResource.getDbName());
        validateDataSource.setInnerDbsId(innerDataSourceId);
        validateDataSource.setInnerDbsName(dbName);
        validateDataSource.setId(resourceId);

        logger.debug("保存 ValidateDataSource 对象");
        this.simpleDao.save(validateDataSource);
    }


    @Override
    public void delete(Integer ruleId) throws Exception {
        ValidateRangeRecord vrr = validateRangeRecordDao.getRecordByRuleId(ruleId);
        if(vrr == null){
            validateRangeRuleDao.delete(ruleId);
        }else{
            throw new Exception("该规则已存在验证记录，不能删除。");
        }
    }

    @Override
    public void update(ValidateRangeRule input) {
        validateRangeRuleDao.update(input);        
    }

    @Override
    public ValidateRangeRuleDto getById(Integer ruleId) {
        return validateRangeRuleDao.getById(ruleId);
    }

    @Override
    public Map<String, Object> query(ValidateRangeRuleDto input, Integer limit, Integer offset) {
        return validateRangeRuleDao.query(input, limit, offset);
    }


    @Override
    public Integer getEntityIdByFieldID(Integer fieldId) {
        return validateRangeRuleDao.getEntityIdByFieldID(fieldId);
    }
}