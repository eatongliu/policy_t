package com.gpdata.wanyou.base.filter;

import com.alibaba.fastjson.JSON;
import com.gpdata.wanyou.base.core.Keys;
import com.gpdata.wanyou.base.core.TicketInfo;
import com.gpdata.wanyou.base.core.TicketTokenHandler;
import com.gpdata.wanyou.base.core.Base64;
import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.utils.ConfigUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 检查用户是否有权限。 简单的说， 就是检查用户是否登录过。
 */
public class UserAuthorizationFilter extends BaseFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthorizationFilter.class);

    private static final Pair<Boolean, User> NEED_LOG_IN = Pair.of(Boolean.TRUE, null);

    /**
     * 一个不可变的,持有要过滤的前缀的正则表达式的 Set
     */
    private final List<Pattern> includePrefixPattern;

    /**
     * 一个不可变的,持有<em>不</em>过滤的前缀的正则表达式的 Set
     */
    private final List<Pattern> excludePrefixPattern;

    /**
     * 用户，Cookie，票据加密的处理工具
     */
    private TicketTokenHandler ticketTokenHandler;

    /**
     * 系统登录的 url 不过滤
     */
    private String loginUrl;

    /**
     * 显示失败的 url 不过滤
     */
    private String unauthorizedUrl;

    public void setTicketTokenHandler(TicketTokenHandler ticketTokenHandler) {
        this.ticketTokenHandler = ticketTokenHandler;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    /**
     * 构造函数 参数是一个需要过滤的路径的正则表达式，例如 {@code ^/test/ }
     *
     * @param includePrefix
     */
    public UserAuthorizationFilter(Set<String> includePrefix, Set<String> excludePrefix) {
        super();
        List<Pattern> includePatterns = null;
        if (includePrefix == null || includePrefix.isEmpty()) {
            includePatterns = Arrays.asList();
        } else {
            includePatterns = includePrefix.stream()
                    .filter(StringUtils::isNotBlank)
                    .sorted(Comparator.comparing(String::length))
                    .map(Pattern::compile)
                    .collect(Collectors.toList());

        }
        this.includePrefixPattern = Collections.unmodifiableList(includePatterns);

        List<Pattern> excludePatterns = null;
        if (excludePrefix == null || excludePrefix.isEmpty()) {
            excludePatterns = Arrays.asList();
        } else {
            excludePatterns = excludePrefix
                    .stream()
                    .filter(StringUtils::isNotBlank)
                    .sorted(Comparator.comparing(String::length))
                    .map(Pattern::compile)
                    .collect(Collectors.toList());
        }
        this.excludePrefixPattern = Collections.unmodifiableList(excludePatterns);
    }


    /**
     * 判断是否<strong style="color: red">需要</strong>过滤的 url 请求
     *
     * @param url
     * @return false : 不处理， true : 要处理
     */
    private boolean isIncludeFilterUrl(String url) {

        if (loginUrl != null && url.startsWith(loginUrl)) {
            return false;
        }

        if (unauthorizedUrl != null && url.startsWith(unauthorizedUrl)) {
            return false;
        }

        Matcher matcher = null;

        for (Pattern pattern : excludePrefixPattern) {
            matcher = pattern.matcher(url);
            if (matcher.find()) {
                return false;
            }
        }

        for (Pattern pattern : includePrefixPattern) {
            matcher = pattern.matcher(url);
            if (matcher.find()) {
                /* LOGGER.debug("检查用户是否登录。地址 : {} -> {}", matcher.group(), url); */
                return true;
            }
        }
        return false;
    }


    /**
     * <p>检查是否需要登录</p>
     * Pair<Boolean, UserBaseinfo> NeedLogInAndUserBaseinfo;
     *
     * @param request
     * @param response
     * @return
     */
    private Pair<Boolean, User> needDoLogIn(HttpServletRequest request
            , HttpServletResponse response) {


        Optional<TicketInfo> ticketInfoOptional = this.ticketTokenHandler.getTicketInfoFromRequest(request);

        if (!ticketInfoOptional.isPresent()) {
            return NEED_LOG_IN;
        }

        TicketInfo ticketInfo = ticketInfoOptional.get();

        long thatTimeMillis = ticketInfo.getThatTimeMillis();
        long currentTimeMillis = System.currentTimeMillis();

        if (thatTimeMillis + this.ticketTokenHandler.getExpireTimeSecond() * 1000 < currentTimeMillis) {
            LOGGER.debug("cookie 时间过期");
            return NEED_LOG_IN;
        }
/*
        LOGGER.debug("ticketInfo.getRemoteHost() -> : {}", ticketInfo.getRemoteHost());
        LOGGER.debug("req.getRemoteHost() -> : {}", request.getRemoteHost());
        LOGGER.debug("ticketInfo.getxRealIp() -> : {}", ticketInfo.getxRealIp());
        LOGGER.debug("req.getHeader (nginx X-Real-ip) -> : {}", request.getHeader("X-Real-ip"));
        LOGGER.debug("remoteHost equals ? -> : {}", ticketInfo.getRemoteHost().equals(request.getRemoteHost()));
*/
        
        /*
         * 两次主机不相等,
         */
        String xRealIpOnHeader = request.getHeader("X-Real-ip");
        String xRealIpOnTicket = ticketInfo.getxRealIp();
        StringUtils.isNoneEmpty(xRealIpOnHeader, xRealIpOnTicket);

        if (StringUtils.isNoneEmpty(xRealIpOnHeader, xRealIpOnTicket)) {
            //LOGGER.debug("两次主机是否相等 : '{}' === '{}' ?", xRealIpOnHeader, xRealIpOnTicket);
            boolean ipEequals = StringUtils.equals(xRealIpOnTicket.trim(), xRealIpOnHeader.trim());
            if (!ipEequals) {
                return NEED_LOG_IN;
            }
        }

        Optional<User> userBaseinfoOptional = this.ticketTokenHandler
                .getCurrentUser(ticketInfo);

        if (!userBaseinfoOptional.isPresent()) {
            LOGGER.debug("cookie 中不存在用户信息");
            return NEED_LOG_IN;
        }

        /*
         * 如果活动时间超过了过期时间的一半， 就更新票据
         */
        if ((currentTimeMillis - thatTimeMillis) / 1000 > this.ticketTokenHandler
                .getExpireTimeSecond() / 2) {

            ticketInfo.setThatTimeMillis(System.currentTimeMillis());
            this.ticketTokenHandler.update(request, response, ticketInfo);
        }
        return Pair.of(Boolean.FALSE, userBaseinfoOptional.get());

    }

    /**
     * 判断是否是 XMLHttpRequest 请求。 <br />
     * 已知在浏览器中调用 XMLHttpRequest。 会产生 <br />
     * {@code X-Requested-With: XMLHttpRequest} 的头信息
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unused")
    private final boolean isXMLHttpRequest(HttpServletRequest request) {
        // X-Requested-With: XMLHttpRequest
        String xRequestedWith = request.getHeader("X-Requested-With");
        return (xRequestedWith != null);

    }

    /**
     * 记住用户上次访问的地址 <br />
     * 保存到 cookie 中。在用户登录后，取出这个地址并跳转到。<br />
     * 如果没有。跳转到首页 (在登录 controller 中实现) <br />
     *
     * @param request
     * @param response
     */
    private void saveLastlyUrl(HttpServletRequest request, HttpServletResponse response) {

        StringBuilder buff = new StringBuilder();

        /*
         * 结果形式：http://localhost:8080/dts/test/testjson
         */
        StringBuffer url = request.getRequestURL();
        LOGGER.debug("url: {}", url);
        buff.append(url);
        /*
         * 结果形式：a=1&b=2&b=3&c=4
         */
        String queryString = request.getQueryString();
        LOGGER.debug("queryString: {}", queryString);
        if (queryString != null) {
            buff.append("?");
            buff.append(queryString);
        }

        String result = Base64.encode(buff.toString().getBytes());
        Cookie cookie = new Cookie(Keys.LASTLY_URL, result);
        cookie.setPath("/");
        cookie.setMaxAge(-1);

        response.addCookie(cookie);

    }

    private String getHostUrl(HttpServletRequest request) {

        return new StringBuilder(128)
                .append(request.getScheme())
                .append("://")
                .append(request.getServerName())
                .append(":")
                .append(request.getServerPort())
                .append(request.getContextPath())
                .toString();
    }

    /**
     * <p>
     * 过滤器主方法
     */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        request.setAttribute("basePath", this.getRequestContextPath(request));
        request.setAttribute("staticfilePath", ConfigUtil.getConfig("static_resource_file"));

        String hostUrl = getHostUrl(request);

        request.setAttribute("hostUrl", hostUrl);

        String servletPath = request.getServletPath();

        if (this.isIncludeFilterUrl(servletPath)) {

            Pair<Boolean, User> needLogInAndUserBaseinfo
                    = this.needDoLogIn(request, response);
            Boolean needLogIn = needLogInAndUserBaseinfo.getLeft();

            User userBaseinfo = needLogInAndUserBaseinfo.getRight();


            if (needLogIn.booleanValue() == true) {
                StringBuffer url = request.getRequestURL();
                LOGGER.debug("url: {}", url);
                // X-Requested-With: XMLHttpRequest
                String xRequestedWith = request.getHeader("X-Requested-With");
                String targetUrl = request.getContextPath() + this.unauthorizedUrl;

                if (xRequestedWith != null) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("state", "ERROR");
                    result.put("cause", "用户未登录");
                    result.put("proposal", "toTragetUrl");
                    result.put("targetUrl", targetUrl);

                    String json = JSON.toJSONString(result);
                    response.getWriter().write(json);
                } else {
                    /*
                     * 将遭到拦击的请求保存起来，等登录成功后再访问。
                     */
                    if (request.getMethod().equalsIgnoreCase("GET")) {
                        this.saveLastlyUrl(request, response);
                    }
                    response.sendRedirect(targetUrl);
                }
                return;
            } else {
                request.setAttribute(Keys.CURRENT_USER_AT_REQUEST, userBaseinfo);
            }
        }


        chain.doFilter(req, resp);

    }

}
