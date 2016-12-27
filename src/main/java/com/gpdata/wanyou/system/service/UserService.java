package com.gpdata.wanyou.system.service;

import com.gpdata.wanyou.system.entity.User;

import java.util.Map;

/**
 * Created by chengchao on 2016/10/25.
 */
public interface UserService {

    /**
     * 使用用户 id 获取用户
     *
     * @param userId
     * @return
     */
    User getUserById(Long userId);

    /**
     * 使用用户名称获取用户
     *
     * @param username
     * @return
     */
    User getUserByName(String username);


    /**
     * 使用用户名称和密码获取用户
     *
     * @param username
     * @param password
     * @return
     */
    User getUserByName(String username, String password);

    /*Guoxiaoyang*/

    Long addUser(User user);

    void updateUser(User user);

    void deleteUser(User user);

    /**
     *
     * @param userId
     * @param userName
     * @param phone
     * @param offset
     * @param limit
     * @return
     */
    Map<String, Object> listUser(String userId,String userName,String phone, Integer offset, Integer limit);


}
