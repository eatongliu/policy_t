package com.gpdata.wanyou.base.service;

import com.gpdata.wanyou.sp.entity.SpiderBaseInfo;

import java.util.Set;

public interface RedisOperateService {

    /**
     * 创建一个 Token 记录并保存到 Redis 中
     * <p>
     * 在 redis 服务器中创建一个记录, 指定过期时间,
     *
     * @param prefix        自定义前缀
     * @param expireSeconds 过期时间(秒)
     * @return
     */
    public String createRedisToken(String prefix, int expireSeconds);

    /**
     * 使用默认前缀 (trade:token:), 创建一个 Token 记录并保存到 Redis 中
     *
     * @param expireSeconds 过期时间(秒)
     * @return
     */
    public String createRedisToken(int expireSeconds);

    /**
     * 使用默认前缀 (trade:token:), 默认过期时间 (360000 = 10分钟) <br />
     * 创建一个 Token 记录并保存到 Redis 中
     *
     * @return
     */
    public String createRedisToken();

    /**
     * 检查这个 token 是否有效
     *
     * @param prefix 自定义前缀
     * @param key    Token 的 key
     * @return
     */
    public boolean hasRedisToken(String prefix, String key);

    /**
     * 检查这个 token 是否有效 使用默认前缀 (trade:token:)
     *
     * @param key
     * @return
     */
    public boolean hasRedisToken(String key);

    /**
     * 初始化在线用户人数, 每次系统重新启动时候调用一次
     */
    public void initOnlineUsers();

    /**
     * 更新在线人数
     *
     * @param v (大于等于 0 :　增加一个, 小于 0 : 减少一个)
     * @return
     */
    public long updateOnlineUsers(int v);

    /**
     * 向rds存手机验证码verify
     */
    public void sendVFCodeToRds(String rediskey, String regcode, String SEED);

    /**
     * 从rds接收手机验证码verify
     *
     * @return
     */
    public String rcvVFCodeFromRds(String rediskey);


    /**
     * 存一个值
     *
     * @param key
     * @param value
     * @return
     */
    public String setValue(String key, String value);

    /**
     * 取一个值
     *
     * @param key
     * @return
     */
    public String getValue(String key);

    /**
     * 删除一个指定的key 的值
     *
     * @param key
     * @return
     */
    public Long delete(String key);

    /**
     * 查询 key
     *
     * @param key
     * @return
     */
    public Set<String> keys(String keysPattern);

    /**
     * 向 Redis 中写入访问量
     *
     * @param string
     * @param remoteIp
     */
    public void saveVisitCountTrace(String path, String remoteIp);

    /**
     * 从 Redis 中读出访问量
     *
     * @param path
     * @param dateString
     */
    public Long getVisitCountTrace(String path, String dateString);

    public String savaSpiderCache(SpiderBaseInfo baseInfo, int expiration);

    public SpiderBaseInfo getSpiderCache(String key);


}
