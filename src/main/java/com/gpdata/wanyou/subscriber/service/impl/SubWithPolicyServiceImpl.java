package com.gpdata.wanyou.subscriber.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.policy.entity.PolicyDocument;
import com.gpdata.wanyou.policy.util.FtRetrievalUtil;
import com.gpdata.wanyou.subscriber.constant.SubscriberConstant;
import com.gpdata.wanyou.subscriber.dao.SubWithPolicyDao;
import com.gpdata.wanyou.subscriber.dao.SubscriberDao;
import com.gpdata.wanyou.subscriber.entity.SubWithPolicy;
import com.gpdata.wanyou.subscriber.entity.Subscriber;
import com.gpdata.wanyou.subscriber.service.SubWithPolicyService;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订阅关联的文章
 * Created by guoxy on 2016/12/14.
 */
@Service
public class SubWithPolicyServiceImpl extends BaseService implements SubWithPolicyService {
    private static final Logger logger = LoggerFactory.getLogger(SubWithPolicyServiceImpl.class);
    /*订阅关联*/
    @Autowired
    private SubWithPolicyDao withPolicyDao;
    /*订阅*/
    @Autowired
    private SubscriberDao subscriberDao;

    /**
     * 根据主键得到一个政策关联
     *
     * @param pid
     * @return
     */
    @Override
    public SubWithPolicy getPolicy(Long pid) {
        return withPolicyDao.getPolicy(pid);
    }

    /**
     * 新增政策关联
     *
     * @param policy
     * @return
     */
    @Override
    public Long addPolicy(SubWithPolicy policy) {
        return withPolicyDao.addPolicy(policy);
    }

    @Override
    public void updatePolicy(SubWithPolicy policy) {
        withPolicyDao.updatePolicy(policy);
    }

    @Override
    public void deletePolicy(SubWithPolicy policy) {
        withPolicyDao.deletePolicy(policy);
    }

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
    @Override
    public Map<String, Object> searchPolicy(Long uid, Long pid, Long sid, String title, Integer offset, Integer limit) {
        return withPolicyDao.searchPolicy(uid, pid, sid, title, offset, limit);
    }

    @Override
    public void deleteSub(Long pid, Long sid) {
        withPolicyDao.deleteSub(pid, sid);
    }

    @Override
    public void updateContext(Long pid, Long sid, String context) {
        withPolicyDao.updateContext(pid, sid, context);
    }

    /**
     * es的全文检索
     * 1. 把id和词组 丢给ES  ES返回 订阅id 政策id、政策标题、政策类型
     * 2. 把返回结果存入SubWithPolicy
     * 3. 返回Pair<Integer, List<Long>>总数和存入的结果idList
     *
     * @param resourceId 订阅id
     * @param keyWord    关键词组
     * @param limit
     * @param offset
     */
    @Override
    public Pair<Integer, List<Long>> ftRetrieval(Long resourceId, Long uid, String keyWord, int limit, int offset) {
        List<Long> idList = new ArrayList<>();

        /*查询条件*/
        PolicyDocument document = new PolicyDocument();
        document.setKeyWords(keyWord);
        //document.setIndex("zcfg/"+sdate);
        document.setIndex("");
        /*TODO  分页指定问题*/
        String result = FtRetrievalUtil.search(document, 0, 20);
        //Pair<Integer, List<Map<String, Object>>> resultPair = FtRetrievalUtil.parseHits(result);
        logger.debug("ES查询结果 result:{}", result);
        //logger.debug("ES查询结果 resultPair:{}", resultPair);
        JSONObject jsonObject = (JSONObject) JSON.parse(result);
        JSONObject hits = (JSONObject) jsonObject.get("hits");
        Integer total = hits.getInteger("total");
        JSONArray subHits = (JSONArray) hits.get("hits");
        JSONArray parseArray = JSON.parseArray(subHits.toJSONString());


        parseArray.forEach(policyMap -> {
            JSONObject json = (JSONObject) policyMap;
            JSONObject jsonSource = (JSONObject) json.get("_source");
            String jsonIndex = json.get("_index").toString();
            PolicyDocument document2 = JSON.parseObject(JSON.toJSONString(jsonSource), PolicyDocument.class);
            String fType = "";
            switch (jsonIndex) {
                //"pdType":"", 文件类型
                // 1 政策法规 zcfg
                // 2 政策文件 zcwj
                // 3 政策解读 zcjd
                // 4 建议提案 jyta
                case "zcfg":
                    fType = "政策法规";
                    break;
                case "zcwj":
                    fType = "政策文件";
                    break;
                case "zcjd":
                    fType = "政策解读";
                    break;
                case "jyta":
                    fType = "建议提案";
                    break;
                default:
                    fType = "暂无";
            }
             /*存入数据库实体拼写*/
            SubWithPolicy withPolicy = new SubWithPolicy();
            withPolicy.setSid(resourceId);
            withPolicy.setKeyWord(keyWord);
            withPolicy.setSendDate(new Date());
            withPolicy.setIsDelete(1);
            withPolicy.setUid(uid);
            withPolicy.setFileType(fType);
            withPolicy.setPid(document2.getPdId());
            withPolicy.setTitle(document2.getPdName());
            withPolicy.setTypeId(document2.getIssuedNum());
            withPolicy.setFileType(document2.getTopicClassify());
            //withPolicy.setSendDate(DateUtils.strToTimestamp(document2.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
            if (withPolicyDao.addPolicy(withPolicy) != null) {
                idList.add(withPolicyDao.addPolicy(withPolicy));
            }

        });
        logger.debug("ES手动存储SubWithPolicy结果 idList:{}", idList);
        return Pair.of(total, idList);
    }


    /**
     * 自动 es的全文检索
     * 1. 获取所有已审核通过并且能正常推送的订阅id
     * 2. 把id和词组 丢给ES  ES返回 订阅id 政策id、政策标题、政策类型
     * 3. 把返回结果存入SubWithPolicy
     * 4. 返回Pair<Integer, List<Long>>总数和存入的结果idList
     * <p>
     * resourceId 订阅id
     * uid
     * keyWord    关键词组
     * limit
     * offset
     */
    @Override
    public void autoFtRetrieval() {
        /*获取所有的成功订阅的数据*/
        Map<String, Object> result = subscriberDao.searchSubscriber(null, null, null, SubscriberConstant.SUBSCRIBER_SUCCESS, null, null, null);
        Integer total = Integer.valueOf(result.get("total").toString());
        List<Subscriber> subscriberList = (List<Subscriber>) result.get("rows");
        subscriberList.forEach(subs -> {
            Pair<Integer, List<Long>> resultPair = ftRetrieval(subs.getSid(), subs.getUid(), subs.getKeyWords().replaceAll(",", " "), 0, 20);
            logger.debug("ES定时存储SubWithPolicy结果 resultPair:{}", resultPair);
        });
    }
}
