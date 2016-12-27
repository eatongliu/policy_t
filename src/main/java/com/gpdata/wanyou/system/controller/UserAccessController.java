package com.gpdata.wanyou.system.controller;

import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.service.RedisOperateService;
import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.system.service.UserService;
import com.gpdata.wanyou.utils.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by chengchao on 2016/11/11.
 */
@Controller
@RequestMapping
public class UserAccessController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisOperateService redisOperateService;

    @RequestMapping(value="/user-log-in", method= RequestMethod.GET)
    public String showUserLogIn(HttpServletRequest request, Model model) {

        model.addAttribute("projectTitle", "政查查");

        return "user-access/user-log-in";
    }

    @RequestMapping(value="/user-log-in", method= RequestMethod.POST)
    public String execUserLogIn(Model model,HttpServletRequest request) {

        //获取用户信息
//        User user = (User) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();

        User user = getCurrentUser(request);
        //把上次登录的时间、IP和地点存入数据库
        user.setLastTime(new Date(Long.parseLong(redisOperateService.getValue("lastTime"))));
        user.setLastIp(redisOperateService.getValue("lastIp"));
        user.setLastSite(redisOperateService.getValue("lastSite"));
        userService.updateUser(user);
        logger.debug("登陆用户：{}",user);

        //获取登陆地信息
        String strIp = request.getRemoteAddr();
        String site = IpUtil.getAddressByIP(strIp);
        logger.debug("登陆IP: {}",strIp);
        logger.debug("登陆地信息：{}",site);

        //将本次登录的时间、IP和地点放入redis缓存
        Date date = new Date();
        redisOperateService.setValue("lastTime",date.getTime() + "");
        redisOperateService.setValue("lastIp",strIp);
        redisOperateService.setValue("lastSite",site);

        return "user-access/user-log-in";
    }

    @RequestMapping(value="/user-log-out")
    public String execUserLogOut() {

        return "redirect:/user-log-in";
    }
}
