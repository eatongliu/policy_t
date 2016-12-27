package com.gpdata.wanyou.policy.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.policy.dao.PolicyTagsDao;
import com.gpdata.wanyou.policy.entity.PolicyDocument;
import com.gpdata.wanyou.policy.entity.PolicyTags;
import com.gpdata.wanyou.policy.util.FtRetrievalUtil;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ligang on 2016/12/16.
 */
@Repository
public class PolicyTagsDaoImpl extends BaseDao implements PolicyTagsDao {
    @Override
    public List<PolicyTags> getTags(String pdId) {
        String sql = "SELECT t.* from policy_tags t  WHERE t.pdId = :pdId ";
        Query query = this.getCurrentSession().createSQLQuery(sql).addEntity(PolicyTags.class).setLong("pdId", Long.valueOf(pdId));
        List<PolicyTags> policyTagsList = query.list();
        return policyTagsList;
    }

    @Override
    public List<String> getRelationPolicyName(String pdId,Integer matchNum) {
        String sql = "SELECT CONCAT(b.pdId,'') FROM( SELECT t.* FROM policy_tags  t WHERE ( SELECT COUNT(*)FROM policy_tags a " +
                "WHERE t.pdId = a.pdId AND t.weight < a.weight ) < :matchNum ) b INNER JOIN ( SELECT t.pdId, t.tag FROM policy_tags t " +
                "WHERE(SELECT COUNT(1) FROM policy_tags a WHERE t.pdId = a.pdId AND t.weight < a.weight ) < :matchNum AND t.pdId = :pdId) c " +
                "ON b.tag = c.tag WHERE b.pdId != :pdId GROUP BY  b.pdId HAVING COUNT(1) = :matchNum";
        Query query = this.getCurrentSession().createSQLQuery(sql).setLong("pdId", Long.valueOf(pdId)).setInteger("matchNum",matchNum);
        List<String> pdIdList = query.list();
        List<String> returnList = new ArrayList<>();
        PolicyDocument policyDocument = new PolicyDocument();
        if(pdIdList.size()==0){
            return null;
        }else {
            pdIdList.forEach(
                    s -> {
                        JSONObject jsonObject = (JSONObject) JSON.parse(FtRetrievalUtil.get(s));
                        JSONObject hits = (JSONObject) jsonObject.get("hits");
                        Integer total = hits.getInteger("total");
                        if(total != null && total != 0){
                            JSONArray subHits = (JSONArray) hits.get("hits");
                            JSONArray parseArray = JSON.parseArray(subHits.toJSONString());
                            parseArray.forEach(
                                    p ->{
                                        JSONObject json = (JSONObject) p;
                                        JSONObject jsonSource = (JSONObject) json.get("_source");
                                        String pdName = String.valueOf(jsonSource.get("pdName"));
                                        returnList.add(pdName);
                                    }
                            );
                        }
                    }
            );
        }
        return returnList;
    }
}
