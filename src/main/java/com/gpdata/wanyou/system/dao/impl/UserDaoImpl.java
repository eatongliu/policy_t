package com.gpdata.wanyou.system.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.system.dao.UserDao;
import com.gpdata.wanyou.system.entity.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chengchao on 2016/11/3.
 */
@Repository
public class UserDaoImpl extends BaseDao implements UserDao {


    @Override
    public User getUserByUserName(String userName) {

        Session session = this.getCurrentSession();
        String hql = "from User a where a.userName = :userName ";

        User user = (User) session.createQuery(hql)
                .setString("userName", userName)
                .uniqueResult();

        return user;
    }

    /*Guoxy*/
    @Override
    public User getUserById(Integer userId) {
        return (User) this.getCurrentSession().get(User.class, userId);
    }

    @Override
    public Long addUser(User user) {
        this.getCurrentSession().save(user);
        return user.getUserId();
    }

    @Override
    public void updateUser(User user) {
        this.getCurrentSession().merge(user);

    }

    @Override
    public void deleteUser(User user) {
        this.getCurrentSession().delete(user);

    }

    /**
     * @param userName 关键词
     * @param offset   起始
     * @param limit    偏移量
     * @return
     */
    @Override
    public Map<String, Object> listUser(String userId, String userName, String phone, Integer offset, Integer limit) {
        String hql = "from User where 1=1";
        String hqlTotal = "SELECT count(*) from User where 1=1";

        if (userId != null && userId.length() != 0) {
            hql += " and userId =:userId";
            hqlTotal += " and userId =:userId";
        }
        if (userName != null && userName.length() != 0) {
            hql += " and userName  like :userName";
            hqlTotal += " and userName like :userName";
        }
        if (phone != null && phone.length() != 0) {
            hql += " and phone =:phone";
            hqlTotal += " and phone =:phone";
        }

        Query query = getCurrentSession().createQuery(hql);
        Query totalQuery = getCurrentSession().createQuery(hqlTotal);

        if (userId != null && userId.length() != 0) {
            query.setString("userId", userId);
            totalQuery.setString("userId", userId);
        }
        if (userName != null && userName.length() != 0) {
            query.setString("userName", "%" + userName + "%");
            totalQuery.setString("userName", "%" + userName + "%");
        }
        if (phone != null && phone.length() != 0) {
            query.setString("phone", phone);
            totalQuery.setString("phone", phone);
        }
        List<User> favoriteList = null;
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
}
