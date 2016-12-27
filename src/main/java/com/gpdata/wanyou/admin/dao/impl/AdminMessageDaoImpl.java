package com.gpdata.wanyou.admin.dao.impl;

import com.gpdata.wanyou.admin.dao.AdminMessageDao;
import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.user.entity.UserMessage;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by guoxy on 2016/12/10.
 */
@Repository
public class AdminMessageDaoImpl extends BaseDao implements AdminMessageDao {
    private static final Logger logger = LoggerFactory.getLogger(AdminMessageDaoImpl.class);

    /**
     * 多线程提交(在考虑是否用批处理)
     *
     * @param msg        插入内容
     * @param List<Long> useridls  正常用户的id集合
     * @author guoxy<br/>
     * 2016年7月12日上午10:12:13<br/>
     */
    @Override
    public void multiThreadImport(UserMessage msg, List<String> useridls) {
        long starttime = System.currentTimeMillis();

//        String sql = "insert user_message(id,aTime,answer,answerMan,isAnswer,isShow,qTime,userId,userName,userQuestion,qType,remark)" +
//                " VALUES (null,CURRENT_TIMESTAMP,?,?,?,?,CURRENT_TIMESTAMP,?,?,?,?,?)";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < useridls.size(); i++) {
                        msg.setUserId(useridls.get(i));
                        getCurrentSession().save(msg);
                        if (i % 30 == 0) {
                            getCurrentSession().flush();
                            getCurrentSession().clear();
                        }
                    }

                } catch (Exception e) {
                    logger.error("IOException", e);
                } finally {
                }
            }
        }).start();
        long spendtime = System.currentTimeMillis() - starttime;
        System.out.println(1 + "个线程花费时间:" + spendtime);

    }

    /**
     * 获取用户id列表
     */
    @Override
    public List<String> getUseridList() {
        String hql = "select userId from User where userstatus =0";
        Query query = getCurrentSession().createQuery(hql);
        List<String> idList = query.list();
        return null;
    }
}
