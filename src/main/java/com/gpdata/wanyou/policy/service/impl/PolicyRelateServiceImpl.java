package com.gpdata.wanyou.policy.service.impl;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.policy.dao.PolicyRelateDao;
import com.gpdata.wanyou.policy.entity.PolicyIndex;
import com.gpdata.wanyou.policy.service.PolicyRelateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by acer_liuyutong on 2016/12/17.
 */
@Service
public class PolicyRelateServiceImpl extends BaseService implements PolicyRelateService {

    @Autowired
    private PolicyRelateDao policyRelateDao;
    /**
     *
     * @param pdId 文章Id
     * @return
     * String[] indexs = {"政策解读","建议提案","政策法规","政策文件"};
     */
    @Override
    public Map<String,Object> getRelatedDocs(Long pdId,String[] indexs){
        Map<String,Object> resultMap = new HashMap<>();
        //政策解读
        List<PolicyIndex> listEnjoy = policyRelateDao.getRelatedDocs(pdId, indexs[0]);
        //建议提案
        List<PolicyIndex> listEnjoy_ = policyRelateDao.getRelatedDocs(pdId, indexs[1]);
        //政策法规
        List<PolicyIndex> listRelate = policyRelateDao.getRelatedDocs(pdId, indexs[2]);
        //政策文件
        List<PolicyIndex> listRelate_ = policyRelateDao.getRelatedDocs(pdId, indexs[3]);
        listEnjoy.addAll(listEnjoy_);
        listRelate.addAll(listRelate_);
        resultMap.put("enjoy",listEnjoy);
        resultMap.put("relate",listRelate);
        return resultMap;
    }
}
