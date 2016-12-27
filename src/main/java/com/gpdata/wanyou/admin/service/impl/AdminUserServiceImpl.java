package com.gpdata.wanyou.admin.service.impl;

import com.gpdata.wanyou.admin.dao.AdminUserDao;
import com.gpdata.wanyou.admin.entity.AdminUser;
import com.gpdata.wanyou.admin.service.AdminRoleService;
import com.gpdata.wanyou.admin.service.AdminUserService;
import com.gpdata.wanyou.utils.ConfigUtil;
import com.gpdata.wanyou.utils.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminUserServiceImpl.class);
    //    private static Properties prop = ConfigUtils.getConfig();
    @Autowired
    private AdminUserDao userDao;
    @Autowired
    private AdminRoleService roleService;

    /**
     * 管理员list map可以为空
     *
     * @param paraMap
     * @return
     */
    @Override
    public List<Map<String, Object>> findAdminList(Map<String, Object> adminMap) {
        return null;
    }

    /**
     * 管理员list总数 map可以为空
     *
     * @param paraMap
     * @return
     */
    @Override
    public int findAdminListCount(Map<String, Object> adminMap) {
        return 0;
    }

    /**
     * 用户详情,常用于登录
     */

    @Override
    public AdminUser getAdminUserByUserName(String loginname) {
        return null;
    }

    /**
     * 只查询一个，常用于修改
     *
     * @param id
     * @return
     */
    @Override
    public AdminUser getAdminUserById(Serializable id) {
        return null;
    }

    /**
     * 按id删除，删除一条；支持整数型和字符串类型ID
     *
     * @param id
     */
    @Override
    public int deleteById(Serializable id) {
        return 0;
    }

    /**
     * 批量删除；支持整数型和字符串类型ID
     *
     * @param ids
     */
    @Override
    public int delete(Serializable[] ids) {
        return 0;
    }

    /**
     * 新增admin用户
     *
     * @param AdminUser
     * @return
     */
    @Override
    public int insertSelective(AdminUser admin) {
        return 0;
    }

    /**
     * 修改admin用户
     *
     * @param AdminUser
     * @return
     */
    @Override
    public int updateByPrimaryKeySelective(AdminUser admin) {
        return 0;
    }

    /**
     * 创建用户
     *
     * @param user
     */
    public AdminUser createUser(AdminUser user) {
        // 加密密码
        // passwordHelper.encryptPassword(user);
        user.setAdminPassword(MD5.sign(user.getAdminPassword(), ConfigUtil.getConfig("regkey"), "utf-8"));
        return userDao.createUser(user);
    }

    @Override
    public AdminUser updateUser(AdminUser user) {
        userDao.updateUser(user);
        return null;
    }

    @Override
    public int deleteUser(Long userId) {
        AdminUser admin = userDao.findOne((int) (long) userId);
        userDao.deleteUser(admin);
        return 0;
    }

    /**
     * 修改密码
     *
     * @param userId
     * @param newPassword
     */
    public void changePassword(Long userId, String newPassword) {
        AdminUser user = userDao.findOne((int) (long) userId);
        user.setAdminPassword(newPassword);
        user.setAdminPassword(MD5.sign(user.getAdminPassword(), ConfigUtil.getConfig("regkey"), "utf-8"));
        userDao.updateUser(user);
    }

    @Override
    public AdminUser findOne(Long userId) {
        return userDao.findOne((int) (long) userId);
    }

    @Override
    public List<AdminUser> findAll(Map<String, String> maps) {

        return userDao.findAll(maps);
    }

    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    public AdminUser findByUsername(String username) {
        Map<String, String> maps = new HashMap<>();
        maps.put("username", username);
        return userDao.findAll(maps).get(0);
    }

    /**
     * 根据用户名查找其角色
     *
     * @param username
     * @return
     */
    public Set<String> findRoles(String username) {
        AdminUser user = findByUsername(username);
        if (user == null) {
            return Collections.EMPTY_SET;
        }
        return roleService.findRoles(user.getAdminRoleids().toArray(new Long[0]));
    }

    /**
     * 根据用户名查找其权限
     *
     * @param username
     * @return
     */
    public Set<String> findPermissions(String username) {
        AdminUser user = findByUsername(username);
        if (user == null) {
            return Collections.EMPTY_SET;
        }
        return roleService.findPermissions(user.getAdminRoleids().toArray(new Long[0]));
    }

}
