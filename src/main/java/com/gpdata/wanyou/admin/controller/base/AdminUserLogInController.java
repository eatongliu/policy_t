package com.gpdata.wanyou.admin.controller.base;

import com.gpdata.wanyou.base.controller.AdminBaseController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 控制用户登录登出
 * 
 * @author chengchao
 *
 */
@Controller
@RequestMapping("/admin")
public class AdminUserLogInController extends AdminBaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminUserLogInController.class);

    @RequestMapping(value = "user-log-in", method = RequestMethod.GET)
    public String getUserGetLogIn() {

        return "user-log-in";
    }

    @RequestMapping(value = "user-log-in", method = RequestMethod.POST)
    public String getUserPostLogIn() {

        return "user-log-in";
    }

    @RequestMapping(value = "user-log-out")
    public String postUserLogIn() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
            // if (LOGGER.isDebugEnabled()) {
            // LOGGER.debug("用户" + username + "退出登录");
            // }
        }

        return "/index";
    }
}
