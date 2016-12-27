package com.gpdata.wanyou.policy.dao;

import java.util.List;

import com.gpdata.wanyou.policy.entity.PolicyRegion;

/**
 *颁布地区 Dao
 * @author wenjie
 *
 */
public interface PolicyRegionDao {

	//一级菜单获取
	List<PolicyRegion> queryClass();
	
	//根据ID，二级菜单获取
	List<PolicyRegion> queryClass2(Long Id);
}
