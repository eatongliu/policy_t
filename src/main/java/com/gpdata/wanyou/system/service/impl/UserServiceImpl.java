package com.gpdata.wanyou.system.service.impl;

import com.gpdata.wanyou.base.dao.SimpleDao;
import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.system.dao.UserDao;
import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.system.service.UserService;
import com.gpdata.wanyou.utils.ConfigUtil;
import com.gpdata.wanyou.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by chengchao on 2016/10/25.
 */
@Service
public class UserServiceImpl extends BaseService implements UserService {


    @Autowired
    private SimpleDao simpleDao;

    @Autowired
    private UserDao userDao;

    /**
     * @param userId
     * @return
     */
    @Override
    public User getUserById(Long userId) {

        User user = this.simpleDao.getById(User.class, userId);

        return user;
    }

    /**
     * @param username
     * @return
     */
    @Override
    public User getUserByName(String username) {

        User user = this.userDao.getUserByUserName(username);
        return user;

    }

    /**
     * @param username
     * @param password
     * @return
     */
    @Override
    public User getUserByName(String username, String password) {

        User user = this.getUserByName(username);

        if (user != null) {
            String md5pwd = MD5.sign(password, ConfigUtil.getConfig("user.md5.salt"), "utf-8");
            return (md5pwd.equals(password)) ? user : null;
        }

        return null;
    }

    @Override
    public Long addUser(User user) {
        return userDao.addUser(user);
    }


    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Override
    public void deleteUser(User user) {
        userDao.deleteUser(user);
    }

    /**
     * @param phone  关键词
     * @param offset 起始
     * @param limit  偏移量
     * @return
     */
    @Override
    public Map<String, Object> listUser(String userId,String userName,String phone, Integer offset, Integer limit) {
        return userDao.listUser(userId,userName,phone, offset, limit);
    }
}
