package com.gpdata.wanyou.user;

import com.gpdata.wanyou.base.service.SimpleService;
import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.user.service.UserKeywordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer_liuyutong on 2016/12/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext.xml"
})
public class UserKeywordTest {
    private static final Logger logger = LoggerFactory.getLogger(UserKeywordTest.class);
    @Autowired
    private UserKeywordService userKeywordService;
    @Autowired
    private SimpleService simpleService;
    @Test
    public void testAppendKeyWords(){
        User user = simpleService.getById(User.class, 1L);
        user = null;
        List<String> list = new ArrayList<>();
        list.add("创新");
        list.add("工业");
        list.add("农业");
        userKeywordService.appendKeywords(user,list);

    }
    @Test
    public void testGetKeywords(){
        userKeywordService.getKeyWords(1L).forEach(keyWord ->
                System.out.println(keyWord)
        );
    }
    @Test
    public void test1(){

    }
}
