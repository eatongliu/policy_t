package com.gpdata.wanyou.subscriber.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.subscriber.dao.SubscriberDao;
import com.gpdata.wanyou.subscriber.entity.Subscriber;
import com.gpdata.wanyou.utils.SimpleBeanPropertiesUtil;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订阅
 * Created by guoxy on 2016/11/30.
 */
@Repository
public class SubscriberDaoImpl extends BaseDao implements SubscriberDao {
    @Override
    public Subscriber getSubscriber(Long sid) {
        return (Subscriber) this.getCurrentSession().get(Subscriber.class, sid);
    }

    @Override
    public Long addSubscriber(Subscriber subscriber) {
        this.getCurrentSession().save(subscriber);
        return subscriber.getSid();
    }

    @Override
    public void updateSubscriber(Subscriber subscriber) {
        Subscriber oldSub = getSubscriber(subscriber.getSid());
        /*里面的 copyNotnullProperites 方法， 可以将一个对象中的非空的值复制给另一个对象*/
        SimpleBeanPropertiesUtil.copyNotNullProperties(subscriber, oldSub);
        this.getCurrentSession().merge(oldSub);
    }

    @Override
    public void deleteSubscriber(Long sid) {
        Subscriber oldSub = getSubscriber(sid);
        this.getCurrentSession().delete(oldSub);
    }

    /**
     * @param sid
     * @param mobile
     * @param status
     * @param offset
     * @param limit
     * @return Map<String ,Object>  key--> rows  total
     */
    @Override
    public Map<String, Object> searchSubscriber(Integer uid, Long sid, String mobile, String status, String keyWord, Integer offset, Integer limit) {
        String hql = "from Subscriber where 1=1";
        String hqlTotal = "SELECT count(*) from Subscriber where 1=1";
        if (uid != null) {
            hql += " and uid =:uid";
            hqlTotal += " and uid =:uid";
        }
        if (sid != null) {
            hql += " and sid =:sid";
            hqlTotal += " and sid =:sid";
        }
        if (mobile != null && mobile.length() != 0) {
            hql += " and mobile  like :mobile";
            hqlTotal += " and mobile  like :mobile";
        }
        if (status != null && status.length() != 0) {
            hql += " and status  = :status";
            hqlTotal += " and status  = :status";
        }
        if (keyWord != null && keyWord.length() != 0) {
            hql += " and keyWords  like :keyWord";
            hqlTotal += " and keyWords  like :keyWord";
        }
        Query query = getCurrentSession().createQuery(hql);
        Query totalQuery = getCurrentSession().createQuery(hqlTotal);
        if (uid != null) {
            query.setInteger("uid", uid);
            totalQuery.setInteger("uid", uid);
        }
        if (sid != null) {
            query.setLong("sid", sid);
            totalQuery.setLong("sid", sid);
        }
        if (mobile != null && mobile.length() != 0) {
            query.setString("mobile", "%" + mobile + "%");
            totalQuery.setString("mobile", "%" + mobile + "%");
        }
        if (status != null && status.length() != 0) {
            query.setString("status", status);
            totalQuery.setString("status", status);
        }
        if (keyWord != null && keyWord.length() != 0) {
            query.setString("keyWord", "%" + keyWord + "%");
            totalQuery.setString("keyWord", "%" + keyWord + "%");
        }
        List<Subscriber> subscriberList = null;
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


}
