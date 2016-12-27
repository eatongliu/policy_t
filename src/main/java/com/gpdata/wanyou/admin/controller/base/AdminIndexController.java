package com.gpdata.wanyou.admin.controller.base;

import com.gpdata.wanyou.admin.entity.AdminResource;
import com.gpdata.wanyou.admin.entity.AdminUser;
import com.gpdata.wanyou.admin.service.AdminResourceService;
import com.gpdata.wanyou.admin.service.AdminUserService;
import com.gpdata.wanyou.base.controller.AdminBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;



@Controller
@RequestMapping("/admin")
public class AdminIndexController extends AdminBaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminIndexController.class);
    @Autowired

    private AdminResourceService resourceService;
    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String getIndex(HttpServletRequest request, HttpServletResponse response, Model model) {

        LOGGER.debug("admin index controller # getIndex ...");
        // 获取当前用户
        AdminUser admin = getCurrentAdminUser(request);
        Set<String> permissions = adminUserService.findPermissions(admin.getAdminLoginname());
        List<AdminResource> menusroot = resourceService.findMenus(permissions);
//      List<Resource> menuschild = resourceService.findChildMenus(permissions);
        model.addAttribute("menus", menusroot);
        return "index";
    }
}
