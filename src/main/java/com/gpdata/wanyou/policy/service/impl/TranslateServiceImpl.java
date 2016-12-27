package com.gpdata.wanyou.policy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.policy.dao.TranslateDao;
import com.gpdata.wanyou.policy.entity.PolicyTags;
import com.gpdata.wanyou.policy.service.TranslateService;

@Service
public class TranslateServiceImpl extends BaseService implements TranslateService{
	@Autowired
	private TranslateDao translateDao;

	@Override
	public List<PolicyTags> getPolicyTags(long pdId) {
		
		return this.translateDao.getPolicytags(pdId);
	}

	
}
