package com.gpdata.wanyou.policy.service;

import com.gpdata.wanyou.policy.entity.PolicyTags;

import java.util.List;

/**
 * Created by ligang on 2016/12/16.
 */
public interface PolicyTagsService {
    List<PolicyTags> getTags(String pdId);

    List<String>  getRelationPolicyName(String pdId,Integer matchNum);
}
