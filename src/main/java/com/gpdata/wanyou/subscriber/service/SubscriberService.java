package com.gpdata.wanyou.subscriber.service;

import com.gpdata.wanyou.subscriber.entity.Subscriber;

import java.util.Map;

/**
 * 订阅
 * Created by guoxy on 2016/11/30.
 */
public interface SubscriberService {
    Subscriber getSubscriber(Long sid);

    Long addSubscriber(Subscriber subscriber);

    void updateSubscriber(Subscriber subscriber);

    void deleteSubscriber(Long sid);

    /**
     * @param sid
     * @param mobile
     * @param status
     * @param offset
     * @param limit
     * @return Map<String ,Object>  key--> rows  total
     */
    Map<String, Object> searchSubscriber(Integer uid, Long sid, String mobile, String status, String keyWord, Integer offset, Integer limit);

}
