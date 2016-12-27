package com.gpdata.wanyou.md.service.impl;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.md.dao.MetadataDao;
import com.gpdata.wanyou.md.entity.MetadataDialect;
import com.gpdata.wanyou.md.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MetadataServiceImpl extends BaseService implements MetadataService {

    @Autowired
    private MetadataDao metadataDao;

    /**
     * 方言--显示具体某个方言的详细信息
     *
     * @param dialectid
     * @return
     */
    @Override
    public MetadataDialect getMetadataDialectById(int dialectid) {

        return metadataDao.getMetadataDialectById(dialectid);
    }


    /**
     * 方言--新增
     *
     * @param caption remark
     * @return
     */
    @Override
    public MetadataDialect addMetadataDialect(MetadataDialect metadataDialect) {
        return metadataDao.addMetadataDialect(metadataDialect);
    }

    /**
     * 方言--修改方言信息
     *
     * @param map
     * @return
     */
    @Override
    public MetadataDialect updateMetadataDialect(MetadataDialect metadataDialect) {

        return metadataDao.updateMetadataDialect(metadataDialect);
    }

    /**
     * 方言--删除某个方言
     *
     * @param dialectid方言id
     * @return
     */
    @Override
    public void deleteMetadataDialect(MetadataDialect metadataDialect) {

        metadataDao.deleteMetadataDialect(metadataDialect);
    }

    /**
     * 方言 -- 检索方言信息
     *
     * @param caption标题 成功：检索方言信息列表，caption模糊匹配
     *                  失败：[“error”:”错误原因”]
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Map<String, List> qMetadata(String caption, int limit, int offset) {
        return metadataDao.qMetadata(caption, limit, offset);
    }


}
