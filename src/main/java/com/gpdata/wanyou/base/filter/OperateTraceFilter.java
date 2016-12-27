package com.gpdata.wanyou.base.filter;

import com.gpdata.wanyou.system.entity.OperateTrace;
import com.gpdata.wanyou.system.service.OperateTraceService;
import com.gpdata.wanyou.system.service.SystemUrlService;
import com.gpdata.wanyou.system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by chengchao on 2016/10/25.
 */
public class OperateTraceFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperateTraceFilter.class);

    @Autowired
    private UserService userService;

    @Autowired
    private OperateTraceService operateTraceService;

    @Autowired
    private SystemUrlService systemUrlService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        OperateTrace operateTrace = new OperateTrace();
        /*
        operateTrace.setBeginTime(new Date());
        operateTrace.setExecStatus();
        operateTrace.setIpAddr();
        operateTrace.setOperateName();
        operateTrace.setRequestParameter();
        operateTrace.setRequestUrl();
        */

        chain.doFilter(request, response);

        /*
        response.get
        operateTrace.setEndTime(new Date());
        */


    }

    @Override
    public void destroy() {

    }
}
