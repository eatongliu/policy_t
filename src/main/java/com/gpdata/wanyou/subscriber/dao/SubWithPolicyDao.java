package com.gpdata.wanyou.subscriber.dao;

import com.gpdata.wanyou.subscriber.entity.SubWithPolicy;

import java.util.Map;

/**
 * 订阅关联的文章
 * Created by guoxy on 2016/12/14.
 */
public interface SubWithPolicyDao {
    /**
     * 根据主键得到一个政策关联
     *
     * @param pid
     * @return
     */
    SubWithPolicy getPolicy(Long pid);

    /**
     * 新增政策关联
     *
     * @param policy
     * @return
     */
    Long addPolicy(SubWithPolicy policy);

    void updatePolicy(SubWithPolicy policy);

    void deletePolicy(SubWithPolicy policy);

    /**
     * 政策关联列表
     *
     * @param uid    用户id
     * @param pid    政策id
     * @param sid    订阅id
     * @param title  政策标题
     * @param offset 起始量
     * @param limit  偏移量
     * @return
     */
    Map<String, Object> searchPolicy(Long uid, Long pid, Long sid, String title, Integer offset, Integer limit);

    /**
     * 用户增加读书笔记
     *
     * @Author yaz
     * @Date 2016/12/14 15:23
     */
    void updateContext(Long pid, Long sid, String context);

    /**
     * 用户删除推送的文章
     * 
     * @Author yaz
     * @Date 2016/12/14 15:23
     */
    void deleteSub(Long pid, Long sid);
}
