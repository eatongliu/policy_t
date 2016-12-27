package com.gpdata.wanyou.base.core;

public final class Keys {

    private Keys() {
        // 避免实例化
    }
    
    /**
     * 保存在每一个 Request 中的当前用户的 KEY 名称。
     * 
     * <br />用法： <br />
     * 
     * UserBaseinfo result = (UserBaseinfo) request.getAttribute(Keys.CURRENT_USER_AT_REQUEST);
     */
    public static final String CURRENT_USER_AT_REQUEST = "currentUser";
    
    /**
     * 未登录用户被拦截器过滤前访问的地址，如果登录成功后再跳转到这个地址。
     */
    public static final String LASTLY_URL = "_gp_lastly_url";
    
    /**
     * 去充值前记录当前操作的url, 待充值完成后回跳.
     */
    public static final String CHARING_BOUND = "_gp_charing_bound";
    
    /**
     * 放到 redis 中的当前用户缓存的 key 的前缀 <br />
     * 值一般是 {@code trade:curr:user:123 } 这样的
     */
    public static final String REDIS_CURRENT_USER_KEY_PREFIX = "trade:curr:user:";
    
}
