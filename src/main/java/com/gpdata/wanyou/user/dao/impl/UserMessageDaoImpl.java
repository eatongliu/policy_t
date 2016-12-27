package com.gpdata.wanyou.user.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.user.dao.UserMessageDao;
import com.gpdata.wanyou.user.entity.UserMessage;
import com.gpdata.wanyou.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guoxy on 2016/12/10.
 */
@Repository
public class UserMessageDaoImpl extends BaseDao implements UserMessageDao {
    @Override
    public UserMessage getUserMessage(Integer aid) {
        return (UserMessage) this.getCurrentSession().get(UserMessage.class, aid);
    }

    @Override
    public Long addUserMessage(UserMessage message) {
        this.getCurrentSession().save(message);
        return message.getId();
    }

    @Override
    public void updateUserMessage(UserMessage message) {
        this.getCurrentSession().merge(message);

    }

    @Override
    public void deleteUserMessage(UserMessage message) {
        this.getCurrentSession().delete(message);
    }

    /**
     * @param userId       用户id
     * @param id           消息id
     * @param userQuestion 问题关键词
     * @param isShow       是否显示0 不展示 1 展示 不传值 所有
     * @param offset       起始
     * @param limit        偏移量
     * @return Map<String ,Object>  key--> rows  total
     */
    @Override
    public Map<String, Object> searchUserMessage(Long userId, Integer id, String userQuestion, String isShow,String qType, Integer offset, Integer limit) {
        String hql = "from UserMessage where 1=1";
        String hqlTotal = "SELECT count(*) from UserMessage where 1=1";
        if (userId != null) {
            hql += " and userId =:userId";
            hqlTotal += " and userId =:userId";
        }
        if (id != null) {
            hql += " and id =:id";
            hqlTotal += " and id =:id";
        }
        if (userQuestion != null && userQuestion.length() != 0) {
            hql += " and userQuestion  like :userQuestion";
            hqlTotal += " and userQuestion  like :userQuestion";
        }
        if (isShow != null && isShow.length() != 0) {
            hql += " and isShow =:isShow";
            hqlTotal += " and isShow =:isShow";
        }
        if (qType != null && qType.length() != 0) {
            hql += " and qType =:qType";
            hqlTotal += " and qType =:qType";
        }

        Query query = getCurrentSession().createQuery(hql);
        Query totalQuery = getCurrentSession().createQuery(hqlTotal);
        if (userId != null) {
            query.setLong("userId", userId);
            totalQuery.setLong("userId", userId);
        }
        if (id != null) {
            query.setInteger("id", id);
            totalQuery.setInteger("id", id);
        }
        if (userQuestion != null && userQuestion.length() != 0) {
            query.setString("userQuestion", "%" + userQuestion + "%");
            totalQuery.setString("userQuestion", "%" + userQuestion + "%");
        }
        if (isShow != null && isShow.length() != 0) {
            query.setString("isShow", isShow);
            totalQuery.setString("isShow", isShow);
        }
        if (qType != null && qType.length() != 0) {
            query.setString("qType", qType);
            totalQuery.setString("qType", qType);
        }
        List<UserMessage> favoriteList = null;
        if (offset != null && limit != null) {
            favoriteList = query
                    .setFirstResult(offset)
                    .setMaxResults(limit).list();
        } else {
            favoriteList = query.list();
        }
        String totalResult = totalQuery.uniqueResult().toString();
        Integer total = Integer.valueOf(totalResult, 10);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", favoriteList);
        result.put("total", total);
        return result;
    }

    @Override
    public Map<String, Object> getUserMessageList(String startDate, String endDate, String keyWord, String qType, String qStatus, Integer limit, Integer offset) {
        String sql = "SELECT t.* FROM user_message t WHERE 1=1 ";
        String totalSql = "select count(t.id)  FROM user_message t WHERE 1=1 ";
        Date  newStartDate =  null;
        Date newEndDate = null;
        //查询起始时间
        if (StringUtils.isNotBlank(startDate)) {
            newStartDate =  new Date(Long.parseLong(DateUtils.strToTimestamp(startDate,"yyyy.MM.dd").toString()));
            sql += " AND t.qTime >= :newStartDate ";
            totalSql += " AND t.qTime >= :newStartDate ";
        }
        //查询结束时间
        if (StringUtils.isNotBlank(endDate)) {
            newEndDate = new Date(Long.parseLong(DateUtils.strToTimestamp(endDate,"yyyy.MM.dd").toString()));
            sql += " AND t.qTime <= :newEndDate ";
            totalSql += " AND t.qTime <= :newEndDate ";
        }
        //查询关键字
        if (StringUtils.isNotBlank(keyWord)) {
            sql += " AND t.userQuestion LIKE :keyWord ";
            totalSql += " AND t.userQuestion LIKE :keyWord ";
        }
        //查询消息类型
        if (StringUtils.isNotBlank(qType)) {
            sql += " t.qType= :qType ";
            totalSql += " t.qType= :qType ";
        }
        //查询消息状态
        if (StringUtils.isNotBlank(qStatus)) {
            sql += "  t.qStatus= :qStatus  ";
            totalSql += "  t.qStatus= :qStatus  ";
        }

        SQLQuery query = (SQLQuery) getCurrentSession().createSQLQuery(sql).setFirstResult(offset).setMaxResults(limit);
        SQLQuery totalQuery = getCurrentSession().createSQLQuery(totalSql);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //查询起始时间
        if (StringUtils.isNotBlank(startDate)) {
            query.setDate("newStartDate", newStartDate);
            totalQuery.setDate("newStartDate",newStartDate);
        }
        //查询结束时间
        if (StringUtils.isNotBlank(endDate)) {
            query.setDate("newEndDate", newEndDate);
            totalQuery.setDate("newEndDate", newEndDate);
        }
        //查询关键字
        if (StringUtils.isNotBlank(keyWord)) {
            query.setString("keyWord", "%"+keyWord+"%" );
            totalQuery.setString("keyWord", "%"+keyWord+"%");
        }
        //查询消息类型
        if (StringUtils.isNotBlank(qType)) {
            query.setString("qType", qType);
            totalQuery.setString("qType", qType);
        }
        //查询消息状态
        if (StringUtils.isNotBlank(qStatus)) {
            query.setString("qStatus", qStatus);
            totalQuery.setString("qStatus", qStatus);
        }
        List<UserMessage> userMsgList = query.addEntity(UserMessage.class).list();
        String totalResult = totalQuery.uniqueResult().toString();
        Integer total = Integer.valueOf(totalResult, 10);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", userMsgList);
        result.put("total", total);
        return result;
    }

    /**
     * @param id      信息编号
     * @param isPoint 信息是否标记为重点 0 否 1 是
     */
    @Override
    public void uploadMsgIsPoint(Integer id, String isPoint) {
        UserMessage um = (UserMessage) this.getCurrentSession().get(UserMessage.class, id);
        um.setIsPoint(isPoint);
        this.getCurrentSession().update(um);
    }

    /**
     * @param id      信息编号
     * @param isResolve 信息是否标记为已解决 0 否 1 是
     */
    @Override
    public void uploadMsgIsResolve(Integer id, String isResolve) {
        UserMessage um = (UserMessage) this.getCurrentSession().get(UserMessage.class, id);
        um.setIsResolve(isResolve);
        this.getCurrentSession().update(um);
    }
    /**
     * @param id      信息编号
     * @param qStatus 信息状态 0 待查阅 1 已查看 2 待处理 3 已备注 4 已处理
     * @param remark  信息备注
     */
    @Override
    public void uploadMsgStatus(Integer id, String qStatus, String remark) {
        UserMessage um = (UserMessage) this.getCurrentSession().get(UserMessage.class, id);
        um.setqStatus(qStatus);
        um.setRemark(remark);
        this.getCurrentSession().update(um);
    }
}
