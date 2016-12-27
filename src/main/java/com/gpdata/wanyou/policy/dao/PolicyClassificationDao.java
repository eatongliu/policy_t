package com.gpdata.wanyou.policy.dao;
/**
 * 
 * @author wenjie
 *
 */

import com.gpdata.wanyou.policy.entity.PolicyClassification;

import java.util.List;

	/**
	 * 获取主题分类一级菜单
	 */
	public interface PolicyClassificationDao{
		
		List<PolicyClassification> queryClass();
		List<PolicyClassification> queryClass2(Long Id);
	}
