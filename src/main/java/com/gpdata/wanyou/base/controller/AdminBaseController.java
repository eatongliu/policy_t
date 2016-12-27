package com.gpdata.wanyou.base.controller;

import com.gpdata.wanyou.admin.entity.AdminUser;
import com.gpdata.wanyou.admin.service.AdminUserService;
import com.gpdata.wanyou.utils.ConfigUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class AdminBaseController implements ServletContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminBaseController.class);


    public static final String KEY_TOTAL = "total";
    public static final String KEY_ROWS = "rows";

    @Autowired
    protected AdminUserService adminUserInfoService;
    protected ServletContext servletContext;

    // public static final Properties prop = ConfigUtils.getConfig();

    /**
     * 实现 ServletContextAware 接口后, Spring 容器会注入一个 ServletContext 实例，我试试
     *
     * @param servletContext
     * @see org.springframework.web.context.ServletContextAware#setServletContext(javax.servlet.ServletContext)
     */
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * 获取后台管理员登录信息
     *
     * @param request
     * @return AdminUser admin
     */
    public AdminUser getCurrentAdminUser(HttpServletRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        String loginname = (String) currentUser.getPrincipal();
        LOGGER.debug("currentSession:{}" + currentUser.getSession() + "  loginname:" + loginname);
        AdminUser admin = adminUserInfoService.getAdminUserByUserName(loginname);
        return admin;
    }


    /**
     * 获取项目的部署路径.
     *
     * @return
     */
    public String getRootPath() {

        return this.servletContext.getRealPath("/");
    }

    /**
     * 获取项目的上传文件部署路径.
     *
     * @return
     */
    protected final String getFileRootPath() {
        return ConfigUtil.getConfig("filepath");
    }


}
