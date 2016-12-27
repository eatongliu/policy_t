package com.gpdata.wanyou.base.filter;

import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by chengchao on 2016/10/25.
 */
public class AccessUserFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessUserFilter.class);

    @Autowired
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        User user = userService.getUserById(1L);
        request.setAttribute("currentUser", user);
        //LOGGER.debug("获取当前用户 {} : {}", user.getUserId(), user.getUserName() );
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
