package com.gpdata.wanyou.policy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.policy.dao.PolicyClassificationDao;
import com.gpdata.wanyou.policy.entity.PolicyClassification;
import com.gpdata.wanyou.policy.service.PolicyClassificationService;
@Service
public class PolicyClassificationServiceImpl extends BaseService implements PolicyClassificationService{

	@Autowired
	private PolicyClassificationDao policyClassificationDao;
	
	
	
	@Override
	public List<PolicyClassification> queryClass() {
		return this.policyClassificationDao.queryClass();
	}
	/**
	 * 获取主题分类二级菜单
	 */
	@Override
	public List<PolicyClassification> queryClass2(Long Id) {
		return this.policyClassificationDao.queryClass2(Id);
	}

}
