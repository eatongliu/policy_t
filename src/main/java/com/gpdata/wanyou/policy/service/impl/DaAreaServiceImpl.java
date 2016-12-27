package com.gpdata.wanyou.policy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.policy.dao.DaAreaDao;
import com.gpdata.wanyou.policy.entity.DaArea;
import com.gpdata.wanyou.policy.service.DaAreaService;
/**
 * 
 * @author wenjie
 *
 */
@Service
public class DaAreaServiceImpl extends BaseService implements DaAreaService{
	
	@Autowired
	private DaAreaDao daAreaDao;

	@Override
	public List<DaArea> queryClass() {
		
		return this.daAreaDao.queryAcea();
	}

	@Override
	public List<DaArea> querycity(Long codeid){
		
		return this.daAreaDao.querycity(codeid);
	}
}
