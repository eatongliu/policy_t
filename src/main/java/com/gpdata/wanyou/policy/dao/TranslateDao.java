package com.gpdata.wanyou.policy.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gpdata.wanyou.policy.entity.PolicyTags;
@Repository
public interface TranslateDao {

	/**
	 * 权重
	 */
	List<PolicyTags> getPolicytags(Long pdId);
}
