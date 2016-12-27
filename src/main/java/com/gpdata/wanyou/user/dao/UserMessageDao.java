package com.gpdata.wanyou.user.dao;

import com.gpdata.wanyou.user.entity.UserMessage;

import java.util.Map;

/**
 * Created by guoxy on 2016/12/10.
 */
public interface UserMessageDao {
    UserMessage getUserMessage(Integer aid);

    Long addUserMessage(UserMessage message);

    void updateUserMessage(UserMessage message);

    void deleteUserMessage(UserMessage message);

    /**
     * @param uid     用户id
     * @param id      消息id
     * @param keyword 问题关键词
     * @param isShow  是否显示0 不展示 1 展示
     * @param qType   消息类型
     * @param offset  起始
     * @param limit   偏移量
     * @return Map<String ,Object>  key--> rows  total
     */
    Map<String, Object> searchUserMessage(Long uid, Integer id, String keyword, String isShow, String qType, Integer offset, Integer limit);

    /**
     * @param startDate 起始日期
     * @param endDate   结束日期
     * @param keyWord   查询关键词
     * @param qType     消息类型
     * @param qStatus   消息状态
     * @param offset    起始
     * @param limit     偏移量
     * @return Map<String ,Object>  key--> rows  total
     */
    Map<String, Object> getUserMessageList(String startDate, String endDate, String keyWord, String qType, String qStatus, Integer limit, Integer offset);

    /**
     * @param id      信息编号
     * @param isPoint 信息是否标记为重点 0 否 1 是
     */
    void uploadMsgIsPoint(Integer id, String isPoint);

    /**
     * @param id        信息编号
     * @param isResolve 信息是否标记为已解决 0 否 1 是
     */
    void uploadMsgIsResolve(Integer id, String isResolve);

    /**
     * @param id      信息编号
     * @param qStatus 信息状态 0 待查阅 1 已查看 2 待处理 3 已备注 4 已处理
     * @param remark  信息备注
     */
    void uploadMsgStatus(Integer id, String qStatus, String remark);
}
