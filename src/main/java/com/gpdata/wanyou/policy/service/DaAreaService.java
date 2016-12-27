package com.gpdata.wanyou.policy.service;

import java.util.List;

import com.gpdata.wanyou.policy.entity.DaArea;

public interface DaAreaService {

	/**
	 * 获取省级菜单
	 */
	List<DaArea> queryClass();
	
	/**
	 * 市、区
	 */
	List<DaArea> querycity(Long codeid);
}
