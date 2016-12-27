package com.gpdata.wanyou.md.dao;

import java.util.List;

import com.gpdata.wanyou.md.dto.MetadataCompareDto;
import com.gpdata.wanyou.md.entity.MetadataCompare;

/**
 * @author acer_liuyutong
 */
public interface MetadataCompareDao {

	/**
	 * 通过tableId获取MetadataCompare
	 * @param tableId
	 */
	MetadataCompare getMcByTableId(Integer tableId);

	/**
	 * 获取MetadataCompare的总数
	 * @param limit
	 * @param offset
	 */
	Integer getTotal(Integer resourceId,Integer tableId,Integer offset, Integer limit);

	/**
	 * 获取MetadataCompare的list
	 * @param limit
	 * @param offset
	 */
	List<MetadataCompareDto> getRows(Integer resourceId,Integer tableId,Integer offset, Integer limit);

	/**
	 * 字段、表 连表查询生成MetadataCompare
	 * @param sourceId
	 */
	void generateMetadataCompares(Integer sourceId);

	/**
	 * 根据字段id获取MetadataCompare
	 */
	MetadataCompare getMcByFieldId(Integer fieldId);

	/**
	 * MetadataCompare的标准字段加1
	 */
	void incrStandField(MetadataCompare mc);
	
}
