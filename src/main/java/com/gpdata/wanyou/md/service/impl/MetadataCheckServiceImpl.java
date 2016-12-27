package com.gpdata.wanyou.md.service.impl;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.md.dao.MetadataCheckDao;
import com.gpdata.wanyou.md.entity.MetadataCheck;
import com.gpdata.wanyou.md.service.MetadataCheckService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chengchao on 16-9-30.
 */
@Service
public class MetadataCheckServiceImpl extends BaseService implements MetadataCheckService {

    @Autowired
    private MetadataCheckDao metadataCheckDao;


    /**
     * 说明：显示具体某个元数据的验证信息。
     * 参数：metadataid元数据id（必填）
     * 成功：表metadata_check中指定metadataid的记录
     * 失败：[“error”:”错误原因”]
     *
     * @param metadataCheck
     * @return
     */
    @Override
    public MetadataCheck getMetadataCheck(MetadataCheck metadataCheck) {
        return metadataCheckDao.getMetadataCheck(metadataCheck);
    }

    /**
     * 说明：在元数据验证信息表中新增记录。
     * 参数1：metadataid元数据id（必填）
     * 参数2：checkformula验证公式
     * 成功：[“id”:元数据验证信息的id]
     * 失败：[“error”:”错误原因”]
     *
     * @param metadataCheck
     * @return
     */
    @Override
    public MetadataCheck addMetadataCheck(MetadataCheck metadataCheck) {
        return metadataCheckDao.addMetadataCheck(metadataCheck);
    }

    /**
     * 说明：修改元数据验证信息
     * 参数1：checkid元数据验证id（必填）
     * 参数2：checkformula验证公式
     * 成功：”success”
     * 失败：[“error”:”错误原因”]
     *
     * @param metadataCheck
     */
    @Override
    public void updateMetadataCheck(MetadataCheck metadataCheck) {

        MetadataCheck bean = this.getMetadataCheck(metadataCheck);
        String checkformula = metadataCheck.getCheckformula();

        if (StringUtils.isNotBlank(checkformula)) {
            bean.setCheckformula(checkformula);
        }

        int metadataid = metadataCheck.getMetadataid();

        if (metadataid != 0) {
            bean.setMetadataid(metadataid);
        }

        this.metadataCheckDao.updateMetadataCheck(bean);

    }

    /**
     * 说明：删除某个元数据验证信息
     * 参数：checkid验证id（必填）
     * 成功：”success”
     * 失败：[“error”:”错误原因”]
     *
     * @param metadataCheck
     */
    @Override
    public void deleteMetadataCheck(MetadataCheck metadataCheck) {

        this.metadataCheckDao.deleteMetadataCheck(metadataCheck);

    }

    /**
     * 说明：检索元数据验证信息
     * 参数：metadataid元数据id
     * 成功：检索元数据验证信息集合
     * 失败：[“error”:”错误原因”]
     *
     * @param metadataCheck
     * @param offset
     * @param size
     * @return
     */
    @Override
    public Pair<Integer, List<MetadataCheck>> queryMetadataCheck(MetadataCheck metadataCheck, Integer offset, Integer size) {

        Integer total = this.metadataCheckDao.queryMetadataCheckTotal(metadataCheck);
        List<MetadataCheck> rows = this.metadataCheckDao.queryMetadataCheckRows(metadataCheck, offset, size);

        return Pair.of(total, rows);

    }
}
