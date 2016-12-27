package com.gpdata.wanyou.policy.service;

import java.util.List;

import com.gpdata.wanyou.policy.entity.PolicyRegion;

/**
 * 
 * @author wenjie
 *
 */
public interface PolicyRegionService {

	/**
	 * 获取颁布地区一级菜单
	 * @param info
	 * @rutern
	 */
	List<PolicyRegion> queryClass();
	
	/**
	 * 获取颁布地区二级菜单
	 * @param Id
	 * @rutern
	 */
	List<PolicyRegion> queryClass2(Long Id);
}
