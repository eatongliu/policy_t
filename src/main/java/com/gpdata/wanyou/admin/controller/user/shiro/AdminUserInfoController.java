package com.gpdata.wanyou.admin.controller.user.shiro;

import com.alibaba.fastjson.JSON;
import com.gpdata.wanyou.admin.entity.AdminUser;
import com.gpdata.wanyou.admin.service.AdminRoleService;
import com.gpdata.wanyou.admin.service.AdminUserService;
import com.gpdata.wanyou.base.controller.AdminBaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员
 *
 * @author guoxy
 */
@Controller
@RequestMapping("/admin")
public class AdminUserInfoController extends AdminBaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminUserInfoController.class);
    @Autowired
    private AdminUserService adminService;
    @Autowired
    private AdminRoleService roleService;


    /**
     * 管理员模块 管理员信息列表
     */
    @RequestMapping(value = "/g/admins", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public void queryForAdminByPage(HttpServletRequest request, HttpServletResponse response) {
        // 排序 desc asc等 暂时不用
        String seq = request.getParameter("order");
        String adminName = request.getParameter("adminname");
        // 分页
        long beginIndex = Long.parseLong(request.getParameter("offset"));
        long pageSize = Long.parseLong(request.getParameter("limit"));
        LOGGER.debug("用户分页" + ":beginIndex=" + beginIndex + "  pageSize=" + pageSize);
        Map<String, Object> adminMap = new HashMap<String, Object>();
        adminMap.put("seq", seq);
        adminMap.put("beginIndex", beginIndex);
        adminMap.put("pageSize", pageSize);
        if (adminName != null && adminName.length() != 0) {
            adminMap.put("adminname", adminName);
        }
        // 分页内容
        List<Map<String, Object>> adminList = adminService.findAdminList(adminMap);

        // 查询总数
        int count = adminService.findAdminListCount(adminMap);
        // 页面接收的
        adminMap.clear();
        adminMap.put("rows", adminList);
        adminMap.put("total", count);
        LOGGER.debug("return " + ":rows=" + adminList + "  total=" + count);
        try {
            // 设置response格式
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(adminMap));
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
    }

    /**
     * 新增管理员
     *
     * @param request
     * @param response
     * @author guoxy
     */
    @RequiresPermissions("role:create")
    @RequestMapping(value = "/a/createadmin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    public void createAdminUser(@ModelAttribute AdminUser admin, HttpServletRequest request,
                                HttpServletResponse response) {
        try {
            // 转义特殊符号防止恶意注入
//            admin = HtmlParseUtils.htmlParseCode(admin, AdminUser.class);
            AdminUser newadmin = null;
            if (admin.getAdminId() != null) {
                // 修改
                newadmin = adminService.updateUser(admin);
            } else {
                // 新增
                newadmin = adminService.createUser(admin);
            }
            if (newadmin != null) {
                response.getWriter().write(JSON.toJSONString(newadmin));
            }
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
    }

    @RequiresPermissions("role:create")
    @RequestMapping(value = "/{adminId}/deladmin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.DELETE)
    public void deleteAdminUser(@PathVariable Long adminId, HttpServletRequest request,
                                HttpServletResponse response) {
        try {
            int flag = adminService.deleteUser(adminId);
            if (flag == 1) {
                response.getWriter().write("SUCCESS");
            } else {
                response.getWriter().write("ERROR");
            }

        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
    }

}
