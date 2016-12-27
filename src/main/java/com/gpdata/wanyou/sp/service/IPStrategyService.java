package com.gpdata.wanyou.sp.service;

import com.gpdata.wanyou.sp.entity.IPStrategy;

/**
 * IP策略
 * Created by guoxy on 2016/10/14.
 */
public interface IPStrategyService {
    /**
     * 4.3.3.1浏览 /sp/ip/g
     * 说明：获取IP策略
     * 读取Nutch配置文件的内容，包括 代理服务器地址，代理服务器端口，代理服务器用户名，代理服务器密码；如果IP策略设置为不可用，
     * 则将配置信息保存到同名的.bak文件中。
     * 参数1：spiderid爬虫id（必填）
     * 成功：IP策略配置文件内容的JSON串
     * 失败：[“error”:”错误原因”]
     *
     * @param ipStrategy
     * @return
     */
    String getIPStrategy(IPStrategy ipStrategy);

    /**
     * 4.3.3.2更新 /sp/ip/u
     * 说明：更新IP策略XML
     * IP策略配置XML文件，保存在/config/{爬虫标识}文件夹中。
     * 参数1：spiderid爬虫id（必填）
     * 参数2：ipconfig内容（必填），传递格式为JSON，保存格式为XML，格式请参看《爬虫处理流程及界面.vsd》中“爬取源”
     * 成功：success
     * 失败：[“error”:”错误原因”]
     */
    String setIPStrategy(IPStrategy ipStrategy);

    /**
     * 如果IP策略设置为不可用，
     * 则将配置信息保存到同名的.bak文件中。
     */
    String saveIPStrategy(IPStrategy ipStrategy);


}
