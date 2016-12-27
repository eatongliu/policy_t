package com.gpdata.wanyou.policy.service;

import java.util.List;

import com.gpdata.wanyou.policy.entity.PolicyTags;

public interface TranslateService {

	List<PolicyTags> getPolicyTags(long pdId);
}
