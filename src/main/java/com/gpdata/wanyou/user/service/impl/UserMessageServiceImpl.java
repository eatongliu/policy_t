package com.gpdata.wanyou.user.service.impl;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.user.dao.UserMessageDao;
import com.gpdata.wanyou.user.entity.UserMessage;
import com.gpdata.wanyou.user.service.UserMessageService;
import com.gpdata.wanyou.utils.HtmlParseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class UserMessageServiceImpl extends BaseService implements UserMessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserMessageServiceImpl.class);
    @Autowired
    private UserMessageDao messageDao;

    @Override
    public UserMessage getUserMessage(Integer aid) {
        return messageDao.getUserMessage(aid);
    }

    @Override
    public Long addUserMessage(UserMessage message) {
        /*防止恶意注入*/
        message = HtmlParseUtils.htmlParseCode(message, UserMessage.class);
        return messageDao.addUserMessage(message);
    }

    @Override
    public void updateUserMessage(UserMessage message) {
        /*防止恶意注入*/
        message = HtmlParseUtils.htmlParseCode(message, UserMessage.class);
        messageDao.updateUserMessage(message);
    }

    @Override
    public void deleteUserMessage(UserMessage message) {

        messageDao.deleteUserMessage(message);
    }

    /**
     * @param uid     用户id
     * @param id      消息id
     * @param keyword 问题关键词
     * @param isShow  是否显示0 不展示 1 展示
     * @param offset  起始
     * @param limit   偏移量
     * @return Map<String ,Object>  key--> rows  total
     */
    @Override
    public Map<String, Object> searchUserMessage(Long uid, Integer id, String keyword,String isShow,String qType, Integer offset, Integer limit) {
        return messageDao.searchUserMessage(uid, id, keyword,isShow,qType, offset, limit);
    }

    @Override
    public Map<String, Object> getUserMessageList(String startDate, String endDate, String keyWord, String qType, String qStatus, Integer limit, Integer offset) {
        return messageDao.getUserMessageList(startDate, endDate, keyWord, qType, qStatus, limit, offset);
    }

    /**
     * @param id      信息编号
     * @param isPoint 信息是否标记为重点 0 否 1 是
     */
    @Override
    public void uploadMsgIsPoint(Integer id, String isPoint) {
        messageDao.uploadMsgIsPoint(id, isPoint);
    }
    /**
     * @param id      信息编号
     * @param isResolve 信息是否标记为已解决 0 否 1 是
     */
    @Override
    public void uploadMsgIsResolve(Integer id, String isResolve) {
        messageDao.uploadMsgIsResolve(id, isResolve);
    }
    /**
     * @param id      信息编号
     * @param qStatus 信息状态 0 待查阅 1 已查看 2 待处理 3 已备注 4 已处理
     * @param remark  信息备注
     */
    @Override
    public void uploadMsgStatus(Integer id, String qStatus, String remark) {
        messageDao.uploadMsgStatus(id,qStatus,remark);
    }
}
