package com.gpdata.wanyou.system.dao;

import com.gpdata.wanyou.system.entity.User;

import java.util.Map;

/**
 * Created by chengchao on 2016/11/3.
 */
public interface UserDao {


    /**
     * @param username
     * @return
     */
    User getUserByUserName(String username);

    /*Guoxy*/
    User getUserById(Integer userId);

    Long addUser(User user);

    void updateUser(User user);

    void deleteUser(User user);

    /**
     * @param userId   用户id
     * @param userName 用户名
     * @param phone    关键词
     * @param offset   起始
     * @param limit    偏移量
     * @return
     */
    Map<String, Object> listUser(String userId, String userName, String phone, Integer offset, Integer limit);
}
