package com.gpdata.wanyou.subscriber.service.impl;

import com.gpdata.wanyou.subscriber.dao.SubscriberDao;
import com.gpdata.wanyou.subscriber.entity.Subscriber;
import com.gpdata.wanyou.subscriber.service.SubscriberService;
import com.gpdata.wanyou.utils.HtmlParseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by guoxy on 2016/12/1.
 */
@Service
public class SubscriberServiceImpl implements SubscriberService {
    @Autowired
    private SubscriberDao subscriberDao;

    @Override
    public Subscriber getSubscriber(Long sid) {
        return subscriberDao.getSubscriber(sid);
    }

    @Override
    public Long addSubscriber(Subscriber subscriber) {

        /*防止恶意注入*/
        subscriber = HtmlParseUtils.htmlParseCode(subscriber, Subscriber.class);
        return subscriberDao.addSubscriber(subscriber);
    }

    @Override
    public void updateSubscriber(Subscriber subscriber) {
        /*防止恶意注入*/
        subscriber = HtmlParseUtils.htmlParseCode(subscriber, Subscriber.class);
        subscriberDao.updateSubscriber(subscriber);
    }

    @Override
    public void deleteSubscriber(Long sid) {
        subscriberDao.deleteSubscriber(sid);
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
        return subscriberDao.searchSubscriber(uid, sid, mobile, status, keyWord, offset, limit);
    }
}
