package com.gpdata.wanyou.policy.service.impl;

import com.gpdata.wanyou.policy.dao.PolicyTagsDao;
import com.gpdata.wanyou.policy.entity.PolicyTags;
import com.gpdata.wanyou.policy.service.PolicyTagsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ligang on 2016/12/16.
 */
@Service
public class PolicyTagsServiceImpl implements PolicyTagsService{

    @Resource
    private PolicyTagsDao policyTagsDao;
    @Override
    public List<PolicyTags> getTags(String pdId) {
        return policyTagsDao.getTags(pdId);
    }

    @Override
    public List<String> getRelationPolicyName(String pdId, Integer matchNum) {
        return policyTagsDao.getRelationPolicyName(pdId,matchNum);
    }
}
