package com.gpdata.wanyou.policy.service;

import java.util.List;

import com.gpdata.wanyou.policy.entity.PolicyClassification;

public interface PolicyClassificationService {

	/**
	 * 获取主题分类一级菜单
	 * @rutern
	 */
	List<PolicyClassification> queryClass();
	
	/**
	 * 获取主题分类二级菜单
	 * @param Id
	 * @rutern
	 */
	List<PolicyClassification> queryClass2(Long Id);
}
