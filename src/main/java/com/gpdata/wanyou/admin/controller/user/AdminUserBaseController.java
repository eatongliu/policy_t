package com.gpdata.wanyou.admin.controller.user;

import com.gpdata.wanyou.base.controller.AdminBaseController;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.system.service.UserService;
import com.gpdata.wanyou.utils.SimpleBeanPropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户后台管理
 * Created by guoxy on 2016/12/12.
 */
@Controller
@RequestMapping("/admin")
public class AdminUserBaseController extends AdminBaseController {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserBaseController.class);
    @Autowired
    private UserService userService;

    /**
     * 用户列表
     *
     * @param offset  起始量
     * @param limit   偏移量
     * @param map     key-->userId 用户Id,userName 用户名 ，phone 手机号
     * @param request
     * @return
     */
    @RequestMapping(value = "/user/ls/{offset}/{limit}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public BeanResult getUserList(@PathVariable Integer offset, @PathVariable Integer limit, @RequestBody Map<String, Object> map, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        try {
              /*TODO 判断管理登录*/
            // AdminUser admin = getCurrentAdminUser(request);
            String userId = map.get("userId").toString();
            String userName = map.get("userName").toString();
            String phone = map.get("phone").toString();
            /*TODO 获取后台管理员id*/
            Map<String, Object> resultMap = userService.listUser(userId, userName, phone, offset, limit);
            result = BeanResult.success(resultMap);
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            result = BeanResult.error("异常 ： " + e);
        }
        return result;
    }

    /**
     * 用户查看
     *
     * @param userId
     * @param request
     * @return
     */
    @RequestMapping(value = "/user/g/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public BeanResult getUserList(@PathVariable Long userId, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        try {
               /*TODO 判断管理登录*/
            // AdminUser admin = getCurrentAdminUser(request);
            /*TODO 获取后台管理员id*/
            User user = userService.getUserById(userId);
            result = BeanResult.success(user);
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            result = BeanResult.error("异常 ： " + e);
        }
        return result;
    }

    /**
     * 用户修改
     *
     * @param userId
     * @param user
     * @param request
     * @return
     */
    @RequestMapping(value = "/user/u/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
    @ResponseBody
    public BeanResult getUserList(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        try {
               /*TODO 判断管理登录*/
            // AdminUser admin = getCurrentAdminUser(request);
            /*TODO 获取后台管理员id*/
            User oldUser = userService.getUserById(userId);
            SimpleBeanPropertiesUtil.copyNotNullProperties(user, oldUser);
            userService.updateUser(oldUser);
            result = BeanResult.success("SUCCESS");
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            result = BeanResult.error("异常 ： " + e);
        }
        return result;
    }

}
