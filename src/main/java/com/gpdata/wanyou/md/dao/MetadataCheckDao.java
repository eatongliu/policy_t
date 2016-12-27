package com.gpdata.wanyou.md.dao;

import com.gpdata.wanyou.md.entity.MetadataCheck;

import java.util.List;

/**
 * Created by chengchao on 16-9-30.
 */
public interface MetadataCheckDao {


    /**
     * 说明：显示具体某个元数据的验证信息。
     * 参数：metadataid元数据id（必填）
     * 成功：表metadata_check中指定metadataid的记录
     * 失败：[“error”:”错误原因”]
     *
     * @param metadataCheck
     * @return
     */
    MetadataCheck getMetadataCheck(MetadataCheck metadataCheck);

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
    MetadataCheck addMetadataCheck(MetadataCheck metadataCheck);

    /**
     * 说明：修改元数据验证信息
     * 参数1：checkid元数据验证id（必填）
     * 参数2：checkformula验证公式
     * 成功：”success”
     * 失败：[“error”:”错误原因”]
     *
     * @param metadataCheck
     */
    void updateMetadataCheck(MetadataCheck metadataCheck);

    /**
     * 说明：删除某个元数据验证信息
     * 参数：checkid验证id（必填）
     * 成功：”success”
     * 失败：[“error”:”错误原因”]
     *
     * @param metadataCheck
     */
    void deleteMetadataCheck(MetadataCheck metadataCheck);


    /**
     * 说明：检索元数据验证信息
     * 参数：metadataid元数据id
     * 成功：总数
     *
     * @param metadataCheck
     * @return
     */
    Integer queryMetadataCheckTotal(MetadataCheck metadataCheck);

    /**
     * 说明：检索元数据验证信息
     * 参数：metadataid元数据id
     * 成功：列表
     *
     * @param metadataCheck
     * @param metadataCheck
     * @param offset
     * @param size
     * @return
     */
    List<MetadataCheck> queryMetadataCheckRows(MetadataCheck metadataCheck, Integer offset, Integer size);
}
