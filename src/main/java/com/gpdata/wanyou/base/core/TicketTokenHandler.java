package com.gpdata.wanyou.base.core;

import com.alibaba.fastjson.JSON;
import com.gpdata.wanyou.base.cache.RedisCache;
import com.gpdata.wanyou.base.common.utils.AESCoder;
import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.system.service.UserService;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

/**
 * 
 * @author chengchaos
 *
 */
public class TicketTokenHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketTokenHandler.class);
    
   
    
    /**
     * 写 cookie 的名称
     */
    private String cookieName = "_gp_sess";

    /**
     * 秘钥
     */
    private String secretKey = "Don't mistake the pointing finger for the moon";
    
    /**
     * 将设置值放到 properties 文件中
     * 过期时间 （秒） 默认是 12 个小时 = 43200
     */
    private int expireTimeSecond = 60 * 60 * 12;
    
    
    /**
     * 使用缓存的过期时间
     */
    private long cacheExpireTimeMillis = 5 * 60 * 1000L;
    
    /**
     * 用户
     */
    private UserService userService;
    
    /**
     * Redis 的缓存
     */
    private RedisCache redisCache;
    

    /**
     * 写 cookie 的名称
     * @param cookieName
     */
    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }
    
    /**
     * 秘钥
     * @param secretKey
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * cookie 过期时间
     * @return
     */
    public int getExpireTimeSecond() {
        return expireTimeSecond;
    }

    /**
     * cookie 过期时间
     * @param expireTimeSecond
     */
    public void setExpireTimeSecond(int expireTimeSecond) {
        this.expireTimeSecond = expireTimeSecond;
    }


    
    /**
     * 使用缓存的过期时间
     * @param cacheExpireTimeMillis
     */
    public void setCacheExpireTimeMillis(long cacheExpireTimeMillis) {
        this.cacheExpireTimeMillis = cacheExpireTimeMillis;
    }

    @Resource
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    @Resource
    public void setRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }


    /**
     * 使用用户信息创建 TicketInfo
     * @param request
     * @param userBaseinfo
     * @return
     */
    public TicketInfo createTicketInfo(HttpServletRequest request, User userBaseinfo) {
        
        String xRealIp = (request.getHeader("X-Real-ip"));
        
        TicketInfo result = new TicketInfo();
        result.setAccount(userBaseinfo.getUserId());
        result.setThatTimeMillis(System.currentTimeMillis());
        result.setRemoteHost(request.getRemoteAddr());
        result.setxRealIp( xRealIp != null ? xRealIp : "" );
        
        return result;
    }
    
    /**
     * 使用 TicketInfo 信息创建 TicketTokenString
     * @param ticketInfo
     * @return
     */
    public String createTicketTokenString(TicketInfo ticketInfo) {
        
        String json = null;
        /*json = JSON.toJSONString(ticketInfo);*/
        /*
         * 使用 toString 比 JSON 略微快一点。
         */
        json = ticketInfo.toString();
        String encrptText = AESCoder.encryptStringToBase64(json, secretKey);
        
        return encrptText;
    }
    

    /**
     * 使用用户信息创建 TicketTokenString
     * @param userBaseinfo
     * @param request
     * @return
     */
    public String createTicketTokenString(HttpServletRequest request, User userBaseinfo) {
        
        TicketInfo result = this.createTicketInfo(request, userBaseinfo);
        
        return this.createTicketTokenString(result);
    }
    
    /**
     * 从前端获取加密票据 (TicketTokenString) 信息
     * 
     * @param request
     * @return
     */
    public Optional<String> getTicketTokenString(HttpServletRequest request) {
        
        Cookie[] cookieArray =  request.getCookies();
        
        if (cookieArray == null || cookieArray.length == 0) {
            return Optional.empty();
        }
        
      
        Optional<String> optional = Arrays.stream(cookieArray)
                .filter(cookie -> StringUtils.equals(cookieName, cookie.getName()))
                .map(Cookie::getValue)
                .findAny();
        
        return optional;
    }
    
    /**
     * 从加密票据 (TicketTokenString) 中获取用户登录信息
     * @param input
     * @return
     */
    public Optional<TicketInfo> getTicketInfoFromToken(String input) {
        
        if (input == null) {
            return Optional.empty();
        }
        
        TicketInfo result = null;
        try {
            String json = AESCoder.decryptBase64ToString(input, secretKey);
            result = JSON.parseObject(json, TicketInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return Optional.ofNullable(result);
    }
    
    
    /**
     * 从前端获取 TicketInfo 信息
     * @param request
     * @return
     */
    public Optional<TicketInfo> getTicketInfoFromRequest(HttpServletRequest request) {
        
        String ticketToken = this.getTicketTokenString(request).orElse(null);
        Optional<TicketInfo> result = this.getTicketInfoFromToken(ticketToken);
        
        return result;
    }
    
    
    
    /**
     * 使用 TicketInfo 获取 UserBaseinfo 信息
     * @param ticketInfo
     * @return
     */
    public Optional<User> getCurrentUser(TicketInfo ticketInfo) {
        
        if ( ticketInfo == null) {
            return Optional.empty();
        }

        User userBaseinfo = null;
        Long account = ticketInfo.getAccount();
        //userBaseinfo = this.userService.getById(account);
        
        userBaseinfo = this.redisCache
                .retrieveOrPut(Keys.REDIS_CURRENT_USER_KEY_PREFIX + account
                        , () -> this.userService.getUserById(account)
                        , cacheExpireTimeMillis);
                
        return Optional.ofNullable(userBaseinfo);
        
    }
    
    /**
     * 从请求中获取 UserBaseinfo 信息 <br />
     * 默认从 Request 中获取，若 Request 中没有， <br />
     * 从 cookie 中的加密票据中获取。 <br />
     * 如果取得了用户信息， 放到 Request 中。 <br />
     * 
     * @param request
     * @return
     */
    public Optional<User> getCurrentUser(HttpServletRequest request) {

        User currentUser = (User) request.getAttribute(Keys.CURRENT_USER_AT_REQUEST);
        if (currentUser != null) {
            return Optional.of(currentUser);
        }
        TicketInfo ticketInfo = this.getTicketInfoFromRequest(request).orElse(null);
        Optional<User> result = this.getCurrentUser(ticketInfo);
        
        if (result.isPresent()) {
            request.setAttribute(Keys.CURRENT_USER_AT_REQUEST, result.get());
        }
        return result;
        
    }
    
    /**
     * 
     * @param request
     * @param response
     * @param userBaseinfo
     */
    public void save(HttpServletRequest request, HttpServletResponse response, User userBaseinfo) {
        
        TicketInfo ticketInfo = this.createTicketInfo(request, userBaseinfo);
        this.save(request, response, ticketInfo);
    }
    
    
    /**
     * 
     * @param request
     * @param response
     * @param ticketInfo
     */
    public void save(HttpServletRequest request, HttpServletResponse response, TicketInfo ticketInfo) {
        
        this.update(request, response, ticketInfo);
    }
    
    /**
     * 
     * @param request
     * @param response
     * @param ticketInfo
     */
    public void update(HttpServletRequest request, HttpServletResponse response, TicketInfo ticketInfo) {
        
        String ticketTokenString = this.createTicketTokenString(ticketInfo);
        Cookie cookie = new Cookie(this.cookieName, ticketTokenString);
        cookie.setMaxAge(this.expireTimeSecond);
        cookie.setPath("/");
        response.addCookie(cookie);
        
    }
    
    /**
     * 删除 cookie 
     * @param request
     * @param response
     */
    public void delete(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(this.cookieName, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void main(String[] args) {
        
        TicketInfo ti = new TicketInfo();
        ti.setAccount(1L);
        ti.setThatTimeMillis(System.currentTimeMillis());
        ti.setRemoteHost("localhost");
        ti.setxRealIp("127.0.0.1");
        
        TicketTokenHandler tth = new TicketTokenHandler();
        String res = tth.createTicketTokenString(ti);
        
        Optional<TicketInfo> t2 = tth.getTicketInfoFromToken(res);
        
        LOGGER.debug("t2.isPresent() : {}", t2.isPresent());
        
        if (t2.isPresent()) {
            LOGGER.debug("t2.get() \t: {}", t2.get());
            LOGGER.debug("t2 > JSON \t: {}", JSON.toJSON(t2.get()));
        }
        
        TicketInfo o = t2.get();
        
        long begin2 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            v2 = o.toString();
        }
        
        long end2 = System.currentTimeMillis();
        
        long begin1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            v1 = JSON.toJSONString(o);
        }

        long end1 = System.currentTimeMillis();
        

        
        LOGGER.debug("V json : {}, V tosstring : {}", (end1 - begin1), (end2 - begin2));
        
        
                
    }
    
    static volatile String v1;
    static volatile String v2;
    
    

}
