package com.gpdata.wanyou.base.aspect;

import com.alibaba.fastjson.JSONObject;
import com.gpdata.wanyou.system.entity.OperateTrace;
import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.system.service.OperateTraceService;
import com.gpdata.wanyou.system.service.UserService;
import com.gpdata.wanyou.utils.ConstKeyValue;
import com.gpdata.wanyou.utils.IpUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 一个切面对所有 Controller 的调用做记录
 * Created by chengchao on 2016/10/27.
 */
//@Aspect
//@Component
public class OperateTraceAspect {

    /**
     * 判断是否是 POST 或者 PUT
     */
    private static final Pattern POST_OR_PUT_METHOD_PATTERN = Pattern.compile("(?i)(?:POST|PUT)");
    private static Logger LOGGER = LoggerFactory.getLogger(OperateTraceAspect.class);
    @Autowired
    private OperateTraceService operateTraceService;

    @Autowired
    UserService userService;

    /**
     *
     */
    @Pointcut("execution(public * com.gpdata.wanyou..controller.*.*(..))")
    public void webRequestLog() {
        /* nothing ... */
    }

    private OperateTrace createOperateTrace(OperateTrace operateTrace
            , ProceedingJoinPoint joinPoint) {


        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();

        HttpServletRequest request = attributes.getRequest();

        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();

        LOGGER.debug("principal ==> {}", principal);
        String account;
        if (Objects.nonNull(principal)) {
            account = principal.toString();
        } else {
            // TODO : 开发期间使用的一个用户
            account = "admin";
        }

        LOGGER.debug("account ==> {}", account);
        User user = userService.getUserByName(account);
        if (Objects.nonNull(user)) {
            request.setAttribute(ConstKeyValue.CURRENT_USER, user);
        }

        //=========================================
        //String beanName = joinPoint.getSignature().getDeclaringTypeName();
        //String methodName = joinPoint.getSignature().getName();
        //String remoteAddr = request.getRemoteAddr();
        //String sessionId = request.getSession().getId();
        //String remoteUser = request.getRemoteUser();
        //String contextPath = request.getServletContext().getContextPath();
        //User user = (User) request.getAttribute("currentUser");

        String ipAddr = IpUtil.getIpAddr(request);

        String remoteHost = request.getRemoteHost();

        String queryString = request.getQueryString();
        String servletPath = request.getServletPath();


        String requestURI = request.getRequestURI();
        StringBuffer requestURL = request.getRequestURL();

        int remotePort = request.getRemotePort();
        String method = request.getMethod();




        String params;

        if (POST_OR_PUT_METHOD_PATTERN.matcher(method).matches()) {

            Object[] paramsArray = joinPoint.getArgs();
            params = Stream.of(paramsArray)
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

        } else {
            Map<?, ?> paramsMap = (Map<?, ?>) request
                    .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            params = paramsMap.toString();
        }


        operateTrace.setCreateTime(System.currentTimeMillis());

        operateTrace.setCh("服务频道");   // 服务频道

        operateTrace.setHn(remoteHost);   // 客户机的完整主机名
        operateTrace.setIp(ipAddr);   // 请求者IP地址
        operateTrace.setM(method);    // 客户机请求方式
        operateTrace.setN(user.getUserName());    // 登录名
        operateTrace.setPa(params);   // 请求参数
        operateTrace.setPt(remotePort);   // 网络端口号
        operateTrace.setQs(queryString);   // 请求行中的参数部分

        operateTrace.setSp(requestURI);   // 调用服务路径
        operateTrace.setUi(servletPath);   // 请求行中的资源名部分
        operateTrace.setUl(requestURL.toString());   // 发出请求时的完整URL


        return operateTrace;
    }


    /**
     * @param joinPoint
     */
    @Around(value = "webRequestLog()")
    public Object watchPerformance(ProceedingJoinPoint joinPoint) {


        long begin = System.nanoTime();
        OperateTrace operateTrace = new OperateTrace();
        Object resultObject = null;
        try {
            /* 接收到请求，记录请求内容 */
            this.createOperateTrace(operateTrace, joinPoint);

            /* 方法执行 */
            resultObject = joinPoint.proceed();

            String result;
            if (resultObject == null) {
                result = "";
            } else if (resultObject.getClass().isInstance(String.class)) {
                result = (String) resultObject;
            } else {
                result = JSONObject.toJSONString(resultObject);
            }
            operateTrace.setRe(result);   // 返回值

        } catch (Throwable t) {
            LOGGER.error("throwable ", t);
        } finally {
            operateTrace.setDt(System.nanoTime() - begin);   // 处理请求所用时间
            this.operateTraceService.saveOperateTrace(operateTrace);
        }

        return resultObject;
    }

}
