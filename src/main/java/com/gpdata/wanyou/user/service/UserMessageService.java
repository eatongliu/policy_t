package com.gpdata.wanyou.user.service;

import com.gpdata.wanyou.user.entity.UserMessage;

import java.util.Map;

/**
 * 用户消息
 * Title:UserMessageService<br/>
 *
 * @author guoxy<br/>
 *         2016年7月11日上午9:44:36<br/>
 */
public interface UserMessageService {
    UserMessage getUserMessage(Integer aid);

    Long addUserMessage(UserMessage message);

    void updateUserMessage(UserMessage message);

    void deleteUserMessage(UserMessage message);

    /**
     * @param userId     用户id
     * @param id      消息id
     * @param keyword 问题关键词
     * @param isShow  是否显示0 不展示 1 展示
     * @param offset  起始
     * @param limit   偏移量
     * @return Map<String ,Object>  key--> rows  total
     */
    Map<String, Object> searchUserMessage(Long userId, Integer id, String keyword,String isShow,String qType, Integer offset, Integer limit);


    /**
     * @param startDate     起始日期
     * @param endDate       结束日期
     * @param keyWord       查询关键词
     * @param qType         消息类型
     * @param qStatus       消息状态
     * @param offset        起始
     * @param limit         偏移量
     * @return Map<String ,Object>  key--> rows  total
     */
    Map<String, Object> getUserMessageList(String startDate, String endDate, String keyWord, String qType,String qStatus, Integer limit, Integer offset);
    /**
     *
     * @param id      信息编号
     * @param isPoint 信息是否标记为重点 0 否 1 是
     */
    void uploadMsgIsPoint(Integer id, String isPoint);

    /**
     * @param id      信息编号
     * @param isResolve 信息是否标记为已解决 0 否 1 是
     */
    void uploadMsgIsResolve(Integer id, String isResolve);

    /**
     * @param id      信息编号
     * @param qStatus 信息状态 0 待查阅 1 已查看 2 待处理 3 已备注 4 已处理
     * @param remark  信息备注
     */
    void uploadMsgStatus(Integer id, String qStatus,String remark);

}
