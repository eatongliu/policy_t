package com.gpdata.wanyou.md.dao;

import java.util.List;

import com.gpdata.wanyou.ds.entity.DataSourceField;
import com.gpdata.wanyou.md.entity.StandSourceMap;

/**
 * @author acer_liuyutong
 */
public interface StandSourceMapDao {
	
	/**
	 * 通过id获取一个对象
	 */
	public StandSourceMap getById(String ssmId);
	
	/**
	 * 查询映射信息
	 */
	public Integer queryTotal(String caption ,int limit,int offset);
	
	public List<StandSourceMap> queryRows(String caption,int limit,int offset);
	
	/**
	 * 使用数据标准和数据源生成映射对象, 同时生成元数据实体和元数据
	 */
	public String save(StandSourceMap standSourceMap);
	
	/**
	 * 删除一个映射, 如果有于此对象相关联的对象存在,不许删除.
	 */
	public boolean delete(StandSourceMap standSourceMap);

	/**
	 * 获取所有字段
	 */
	List<DataSourceField> getAllFields(Integer sourceId);

	/**
	 * 通过字段id获取tableId和totalField
	 */
	Object[] getValArrByFieldId(Integer fieldId);

}