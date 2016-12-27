package com.gpdata.wanyou.md.service;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.gpdata.wanyou.md.dto.MetadataCompareDto;

/**
 * @author acer_liuyutong
 */
public interface MetadataCompareService {
	
    /**
     * 获取所有的元数据比对结果
     * @param limit
     * @param offset
     */
    Pair<Integer, List<MetadataCompareDto>> getMetadataCompareList(Integer resourceId,Integer tableId,Integer offset, Integer limit);

}