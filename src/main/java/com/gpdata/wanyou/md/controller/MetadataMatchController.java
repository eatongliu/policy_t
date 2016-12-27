package com.gpdata.wanyou.md.controller;

import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.base.vo.KvPair;
import com.gpdata.wanyou.md.entity.MetadataEntity;
import com.gpdata.wanyou.md.service.MetadataEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 元数据实体和元数据相匹配的功能
 * Created by chengchao on 2016/11/14.
 */
@RequestMapping
@Controller
public class MetadataMatchController {

    @Autowired
    private MetadataEntityService metadataEntityService;

    /**
     * 修改元数据相关联的元数据实体
     */
    @RequestMapping(value = "/md/{metadataId}/mat"
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE
            , method = RequestMethod.PUT)
    @ResponseBody
    public BeanResult updateMatch(@PathVariable Integer metadataId
            , @RequestParam Integer[] entityIds) {

        BeanResult result;

        Set<Integer> entityIdSet = new HashSet<>();

        if (entityIds != null) {
            entityIdSet = Stream.of(entityIds).collect(Collectors.toSet());
        }

        try {
            this.metadataEntityService.updateMetadataEntityMath(metadataId, entityIdSet);
            result = BeanResult.success(metadataId);

        } catch (Exception e) {
            e.printStackTrace();
            result = BeanResult.error(e.getMessage());
        }

        return result;


    }

    /**
     * 根据数据表 ID 获取元数据实体 id 列表
     * @param tableId
     */
    @RequestMapping(value = "/md/tab/{tableId}/mes"
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE
            , method = RequestMethod.GET)
    @ResponseBody
    public List<KvPair> getEntitiesByTableId(@PathVariable Integer tableId) {


        List<MetadataEntity> metadataEntityList = this
                .metadataEntityService
                .getMetadataEntityListByTableId(tableId);

        return metadataEntityList.stream()
                .map(entity -> KvPair.of(entity.getEntityId(), entity.getMetadataEntityName()
                        + " [" +entity.getCaption() + "]"))
                .collect(Collectors.toList());


    }
}
