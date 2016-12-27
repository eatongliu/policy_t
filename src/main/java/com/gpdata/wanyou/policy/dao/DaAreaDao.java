package com.gpdata.wanyou.policy.dao;

import java.util.List;

import com.gpdata.wanyou.policy.entity.DaArea;

public interface DaAreaDao {

	/**
	 * 省
	 */
	List<DaArea> queryAcea();
	
	/**
	 * 市、区
	 */
	List<DaArea> querycity(Long codeid);
	
}
