package com.gpdata.wanyou.admin.service;

import com.gpdata.wanyou.admin.entity.AdminUser;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 管理员
 */
public interface AdminUserService {

    /**
     * 创建用户
     *
     * @param user
     */
    public AdminUser createUser(AdminUser user);

    public AdminUser updateUser(AdminUser user);

    public int deleteUser(Long userId);

    /**
     * 修改密码
     *
     * @param userId
     * @param newPassword
     */
    public void changePassword(Long userId, String newPassword);

    AdminUser findOne(Long userId);

    List<AdminUser> findAll(Map<String,String> maps);

    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    public AdminUser findByUsername(String username);

    /**
     * 根据用户名查找其角色
     *
     * @param username
     * @return
     */
    public Set<String> findRoles(String username);

    /**
     * 根据用户名查找其权限
     *
     * @param username
     * @return
     */
    public Set<String> findPermissions(String username);

    /**
     * 管理员list map可以为空
     *
     * @param paraMap
     * @return
     */
    public List<Map<String, Object>> findAdminList(Map<String, Object> adminMap);

    /**
     * 管理员list总数 map可以为空
     *
     * @param paraMap
     * @return
     */
    public int findAdminListCount(Map<String, Object> adminMap);

    /**
     * 用户详情,常用于登录
     */

    public AdminUser getAdminUserByUserName(String loginname);

    /**
     * 只查询一个，常用于修改
     *
     * @param id
     * @return
     */
    public AdminUser getAdminUserById(Serializable id);

    /**
     * 按id删除，删除一条；支持整数型和字符串类型ID
     *
     * @param id
     */
    public int deleteById(Serializable id);

    /**
     * 批量删除；支持整数型和字符串类型ID
     *
     * @param ids
     */
    public int delete(Serializable[] ids);

    /**
     * 新增admin用户
     *
     * @param AdminUser
     * @return
     */
    int insertSelective(AdminUser admin);

    /**
     * 修改admin用户
     *
     * @param AdminUser
     * @return
     */
    int updateByPrimaryKeySelective(AdminUser admin);

}
