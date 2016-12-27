package com.gpdata.wanyou.md.dao;

import com.gpdata.wanyou.md.entity.DataStandEntity;

import java.io.File;
import java.util.Map;

/**
 * 数据标准实体
 * <p>
 * Created by ligang on 16-10-25.
 */
public interface DataStandEntityDao {
	
	//获取指定 id 的对象
	public DataStandEntity getById(Integer standentid);

	//保存新对象
	public int save(DataStandEntity input);
	
	//修改
	public void update(DataStandEntity input);
	
	//删除 
	public void delete(Integer standentid);
	
	//query 查询
	public Map<String, Object> query(DataStandEntity input,Integer limit,Integer offset);
	
	//获取指定数据标准下的所有数据标准实体
	public Map<String, Object> getAllByStandId(Integer standId,Integer limit,Integer offset);
}
