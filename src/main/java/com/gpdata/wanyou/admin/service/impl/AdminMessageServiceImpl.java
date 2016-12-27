package com.gpdata.wanyou.admin.service.impl;

import com.gpdata.wanyou.admin.dao.AdminMessageDao;
import com.gpdata.wanyou.admin.service.AdminMessageService;
import com.gpdata.wanyou.user.entity.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by guoxy on 2016/12/10.
 */
@Service
public class AdminMessageServiceImpl implements AdminMessageService {
    @Autowired
    private AdminMessageDao messageDao;

    /**
     * 多线程提交
     *
     * @param msg      插入内容
     * @param useridls
     * @author guoxy<br/>
     * 2016年7月12日上午10:12:13<br/>
     */
    @Override
    public void multiThreadImport(UserMessage msg, List<String> useridls) {
        messageDao.multiThreadImport(msg, useridls);

    }

    /**
     * 获取用户id列表
     */
    @Override
    public List<String> getUseridList() {
        return messageDao.getUseridList();
    }
}
