package com.gpdata.wanyou.subscriber.service;

import com.gpdata.wanyou.subscriber.entity.SubWithPolicy;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

/**
 * 订阅关联的文章
 * Created by guoxy on 2016/12/14.
 */
public interface SubWithPolicyService {
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

    /**
     * 手动 es的全文检索
     * 1. 把id和词组 丢给ES  ES返回 订阅id 政策id、政策标题、政策类型
     * 2. 把返回结果存入SubWithPolicy
     * 3. 返回Pair<Integer, List<Long>>总数和存入的结果idList
     *
     * @param resourceId 订阅id
     * @param keyWord    关键词组
     * @param offset
     * @param limit
     */
    public Pair<Integer, List<Long>> ftRetrieval(Long resourceId, Long uid, String keyWord, int limit, int offset);

    /**
     * 自动 es的全文检索
     * 1. 把id和词组 丢给ES  ES返回 订阅id 政策id、政策标题、政策类型
     * 2. 把返回结果存入SubWithPolicy
     * 3. 返回Pair<Integer, List<Long>>总数和存入的结果idList
     *
     */
    public void autoFtRetrieval();

}
