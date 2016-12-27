package com.gpdata.wanyou.md.dao;

import java.util.List;
import java.util.Map;

import com.gpdata.wanyou.md.entity.MetadataDialect;

public interface MetadataDao {

	/**
	 * 方言--显示具体某个方言的详细信息
	 * @param dialectid
	 * @return
	 */
	public MetadataDialect  getMetadataDialectById(int dialectid);
	
	/**
	 * 方言--新增
	 * @param   caption   remark
	 * @return 
	 */
	
	public MetadataDialect addMetadataDialect(MetadataDialect metadataDialect) ;
	
	/**
	 * 方言--修改方言信息
	 * @param   map
	 * @return 
	 */
	public MetadataDialect updateMetadataDialect(MetadataDialect metadataDialect);

	
	
	/**
	 * 方言--删除某个方言
	 * @param dialectid方言id
	 * @return 
	 * 
	 */
	
	public void deleteMetadataDialect(MetadataDialect metadataDialect);
	
	
	/**
	 * 方言 -- 检索方言信息
	 * @param caption标题
	 * 成功：检索方言信息列表，caption模糊匹配
	 * 失败：[“error”:”错误原因”]
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, List> qMetadata(String caption, int limit,int offset) ;
		
	
}