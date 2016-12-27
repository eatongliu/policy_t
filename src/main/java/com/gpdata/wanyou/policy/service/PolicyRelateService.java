package com.gpdata.wanyou.policy.service;

import java.util.Map;

/**
 * Created by acer_liuyutong on 2016/12/17.
 */
public interface PolicyRelateService {

    /**
     * 猜你喜欢o(^▽^)o
     * @param pdId
     * @return
     */
    public Map<String,Object> getRelatedDocs(Long pdId, String[] indexs);
}