package com.gpdata.wanyou.policy.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.policy.dao.PolicyRelateDao;
import com.gpdata.wanyou.policy.entity.PolicyIndex;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by acer_liuyutong on 2016/12/17.
 */
@Repository
public class PolicyRelateDaoImpl extends BaseDao implements PolicyRelateDao {

    /**
     * 每种index获取3个相关的文章
     * @param pdId
     * @param index
     * @return
     */
    @Override
    public List<PolicyIndex> getRelatedDocs(Long pdId,String index){
        int count = 3;
        String sql = "SELECT p.pdId,p.createDate," +
                    "CASE p.index_ " +
                        "WHEN \"zcfg\" THEN \"政策法规\" " +
                        "WHEN \"zcjd\" THEN \"政策解读\" " +
                        "WHEN \"zcwj\" THEN \"政策文件\" " +
                        "WHEN \"jyta\" THEN \"建议提案\" " +
                         "ELSE \"其他\" "+
                        "END AS `index_`, " +
                    "p.pdName,p.topicClassify " +
                "FROM " +
                "(SELECT a.pdId,a.tag,a.weight FROM policy_tags a " +
                "WHERE a.pdId != :pdId ) m " +
                "INNER JOIN " +
                "(SELECT b.pdId,b.tag,b.weight FROM policy_tags b " +
                "WHERE b.pdId = :pdId ) n " +
                "ON m.tag = n.tag " +
                "LEFT JOIN policy_index p  " +
                "ON p.pdId = m.pdId " +
                "WHERE p.index_ = :index " +
                "GROUP BY m.pdId " +
                "ORDER BY m.weight DESC ";
        SQLQuery query = getCurrentSession().createSQLQuery(sql).addEntity(PolicyIndex.class);
        query.setParameter("pdId",pdId).setParameter("index",index).setFirstResult(0).setMaxResults(count);
        List<PolicyIndex> list = query.list();
        return list;
    }

}
