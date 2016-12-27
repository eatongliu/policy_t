package com.gpdata.wanyou.md.service;

import com.gpdata.wanyou.md.entity.MetadataCheck;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * 2.2.3元数据验证（交叉对比）
 * <p>
 * Created by chengchao on 16-9-30.
 */
public interface MetadataCheckService {

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
     * 成功：检索元数据验证信息集合
     * 失败：[“error”:”错误原因”]
     *
     * @param metadataCheck
     * @param offset
     * @param size
     * @return
     */
    Pair<Integer, List<MetadataCheck>> queryMetadataCheck(MetadataCheck metadataCheck, Integer offset, Integer size);


}
