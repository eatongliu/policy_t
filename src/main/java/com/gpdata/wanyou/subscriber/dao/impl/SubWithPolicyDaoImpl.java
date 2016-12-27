package com.gpdata.wanyou.subscriber.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.subscriber.dao.SubWithPolicyDao;
import com.gpdata.wanyou.subscriber.entity.SubWithPolicy;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guoxy on 2016/12/14.
 */
@Repository
public class SubWithPolicyDaoImpl extends BaseDao implements SubWithPolicyDao {
    /**
     * 根据主键得到一个政策关联
     *
     * @param pid
     * @return
     */
    @Override
    public SubWithPolicy getPolicy(Long pid) {
        return (SubWithPolicy) this.getCurrentSession().get(SubWithPolicy.class, pid);
    }

    /**
     * 新增政策关联
     *
     * @param policy
     * @return
     */
    @Override
    public Long addPolicy(SubWithPolicy policy) {
        this.getCurrentSession().save(policy);
        return policy.getPid();
    }

    @Override
    public void updatePolicy(SubWithPolicy policy) {
        this.getCurrentSession().merge(policy);
    }

    @Override
    public void deletePolicy(SubWithPolicy policy) {
        this.getCurrentSession().delete(policy);
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
        String hql = "from SubWithPolicy where isDelete=1";
        String hqlTotal = "SELECT count(*) from SubWithPolicy where isDelete=1";
        if (uid != null) {
            hql += " and uid =:uid";
            hqlTotal += " and uid =:uid";
        }
        if (pid != null) {
            hql += " and pid =:pid";
            hqlTotal += " and pid =:pid";
        }
        if (sid != null) {
            hql += " and sid =:sid";
            hqlTotal += " and sid =:sid";
        }
        if (title != null && title.length() != 0) {
            hql += " and title  like :title";
            hqlTotal += " and title  like :title";
        }
        hql += " order by sendDate desc";
        Query query = getCurrentSession().createQuery(hql);
        Query totalQuery = getCurrentSession().createQuery(hqlTotal);
        if (uid != null) {
            query.setLong("uid", uid);
            totalQuery.setLong("uid", uid);
        }
        if (pid != null) {
            query.setLong("pid", pid);
            totalQuery.setLong("pid", pid);
        }
        if (sid != null) {
            query.setLong("sid", sid);
            totalQuery.setLong("sid", sid);
        }
        if (title != null && title.length() != 0) {
            query.setString("title", "%" + title + "%");
            totalQuery.setString("title", "%" + title + "%");
        }
        List<SubWithPolicy> subscriberList = null;
        if (offset != null && limit != null) {
            subscriberList = query
                    .setFirstResult(offset)
                    .setMaxResults(limit).list();
        } else {
            subscriberList = query.list();
        }
        String totalResult = totalQuery.uniqueResult().toString();
        Integer total = Integer.valueOf(totalResult, 10);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", subscriberList);
        result.put("total", total);
        return result;
    }

    /**
     * 用户增加读书笔记
     *
     * @Author yaz
     * @Date 2016/12/14 15:23
     */
    @Override
    public void updateContext(Long pid, Long sid, String context) {
        getCurrentSession().createSQLQuery("update sub_with_policy sp set sp.remark =:remark where sp.pid =:pid and sp.sid=:sid")
                .setString("remark", context)
                .setLong("pid", pid)
                .setLong("sid", sid).executeUpdate();
    }

    @Override
    public void deleteSub(Long pid, Long sid) {
        getCurrentSession().createSQLQuery("update sub_with_policy sp set sp.isDelete = -1 where sp.pid =:pid and sp.sid=:sid")
                .setLong("pid", pid)
                .setLong("sid", sid).executeUpdate();
    }
}
