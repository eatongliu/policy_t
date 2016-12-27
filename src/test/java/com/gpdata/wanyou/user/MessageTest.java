package com.gpdata.wanyou.user;

import com.gpdata.wanyou.user.entity.UserMessage;
import com.gpdata.wanyou.user.service.UserMessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * Created by guoxy on 2016/12/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext.xml"
})
public class MessageTest {
    private static final Logger logger = LoggerFactory.getLogger(MessageTest.class);
    @Autowired
    private UserMessageService messageService;

    @Test
    public void testMessage() {
        UserMessage message = new UserMessage();
        message.setUserQuestion("今天吃啥");
        Date d = new Date();
        message.setqTime(d);
        message.setUserName("王老吉");
        Long mid = messageService.addUserMessage(message);
        logger.debug("mid:{}", mid);
    }


}
