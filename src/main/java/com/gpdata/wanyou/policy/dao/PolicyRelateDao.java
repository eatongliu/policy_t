package com.gpdata.wanyou.policy.dao;

import com.gpdata.wanyou.policy.entity.PolicyIndex;

import java.util.List;

/**
 * Created by acer_liuyutong on 2016/12/17.
 */
public interface PolicyRelateDao {

    /**
     * 每种index获取3个相关的文章
     * @param pdId
     * @param index
     * @return
     */
    public List<PolicyIndex> getRelatedDocs(Long pdId, String index);
}
