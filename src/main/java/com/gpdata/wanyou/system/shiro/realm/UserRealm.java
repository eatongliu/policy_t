package com.gpdata.wanyou.system.shiro.realm;

import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.system.service.UserService;
import com.gpdata.wanyou.utils.ConfigUtil;
import com.gpdata.wanyou.utils.MD5;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by chengchao on 2016/11/11.
 */
@Component
public class UserRealm extends AuthorizingRealm implements Serializable, ServletContextAware {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRealm.class);

    private ServletContext servletContext;

    @Autowired
    private SessionDAO sessionDAO;


    @Autowired
    private UserService userService;


    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        LOGGER.info("检查权限: -> doGetAuthorizationInfo ..");
        String account = (String) getAvailablePrincipal(principalCollection);

        Objects.requireNonNull(account);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        Subject subject = SecurityUtils.getSubject();

        boolean authenticated = subject.isAuthenticated();
        boolean remembered = subject.isRemembered();
        Session session = subject.getSession();

        LOGGER.debug("authenticated : {}", authenticated);
        LOGGER.debug("remembered : {}", remembered);
        LOGGER.debug("Session 名 : {}", session.getClass().getName());

        //adminUserService.findRoles(account)
        Set<String> roles = new HashSet<>();
        roles.add("");
        info.setRoles(roles);
        //adminUserService.findPermissions(account)
        Set<String> stringPermissions = new HashSet<>();
        stringPermissions.add("");
        info.setStringPermissions(stringPermissions);

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        LOGGER.info("登录验证: --> doGetAuthenticationInfo ..");

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        if (token.getUsername() == null || token.getPassword() == null) {
            throw new UnknownAccountException("请输入用户名和密码!");
        }

        String account = token.getUsername();
        String password = String.valueOf(token.getPassword());
        boolean rememberMe = token.isRememberMe();

        Subject subject = SecurityUtils.getSubject();
        boolean authenticated = subject.isAuthenticated();
        LOGGER.debug("authenticated : {}", authenticated);

        boolean remembered = subject.isRemembered();
        LOGGER.debug("remembered : {}", remembered);

        /*
         * Collection<Session> sessions= sessionDAO.getActiveSessions();
         *
         * Session session = SecurityUtils.getSubject().getSession();
         * SavedRequest savedRequest = (SavedRequest)
         * session.getAttribute(WebUtils.SAVED_REQUEST_KEY);
         *
         * if (savedRequest != null) {
         * LOGGER.debug("savedRequest getMethod is : {}",
         * savedRequest.getMethod());
         * LOGGER.debug("savedRequest getQueryString is : {}",
         * savedRequest.getQueryString());
         * LOGGER.debug("savedRequest getRequestURI is : {}",
         * savedRequest.getRequestURI());
         * LOGGER.debug("savedRequest getRequestUrl is : {}",
         * savedRequest.getRequestUrl()); } else {
         * LOGGER.debug("savedRequest  is : null ... "); }
         */

        /* 不想记住上次访问了哪里， 就这么做 */
        Session session = SecurityUtils.getSubject().getSession();
        session.removeAttribute(WebUtils.SAVED_REQUEST_KEY);

        User user = this.userService.getUserByName(account);
        if (user == null) {
            throw new UnknownAccountException("账户不存在!");
        }
        String md5pwd = MD5.sign(password, ConfigUtil.getConfig("user.md5.salt"), "utf-8");
        LOGGER.debug("md5pwd:{}", md5pwd);
        LOGGER.debug("boolean:{}", md5pwd.equals(user.getPassword()));
        if (md5pwd.equals(user.getPassword())) {
            return new SimpleAuthenticationInfo(account, password, getName());
        }
        return null;
    }

}
