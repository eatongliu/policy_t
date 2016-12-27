package com.gpdata.wanyou.policy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.policy.dao.PolicyRegionDao;
import com.gpdata.wanyou.policy.entity.PolicyRegion;
import com.gpdata.wanyou.policy.service.PolicyRegionService;
/**
 * 
 * @author wenjie
 *
 */
@Service
public class PolicyRegionServiceImpl extends BaseService implements PolicyRegionService {
	
	@Autowired
	private PolicyRegionDao policyRegionDao;

	/**
	 * 获取颁布地区一级菜单
	 */
	@Override
	public List<PolicyRegion> queryClass() {
		
		return this.policyRegionDao.queryClass();
	}

	/**
	 * 获取颁布地区二级菜单
	 */
	@Override
	public List<PolicyRegion> queryClass2(Long Id) {
		
		return this.policyRegionDao.queryClass2(Id);
	}
	
}
