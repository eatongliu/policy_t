package com.gpdata.wanyou.admin.service;

import com.gpdata.wanyou.user.entity.UserMessage;

import java.util.List;

/**
 * Created by guoxy on 2016/12/10.
 */
public interface AdminMessageService {
    /**
     * 多线程提交
     *
     * @param msg 插入内容
     * @author guoxy<br/>
     * 2016年7月12日上午10:12:13<br/>
     */
    public void multiThreadImport(UserMessage msg, List<String> useridls);
    /**
     * 获取用户id列表
     */
    public List<String> getUseridList();
}
