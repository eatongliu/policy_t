package com.gpdata.wanyou.user;

import com.gpdata.wanyou.user.service.UserFavoriteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * @author create by lyt on 1900
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext.xml"
})
public class FavoriteTest {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteTest.class);
    @Autowired
    private UserFavoriteService userFavoriteService;

    @Test
    public void testGetList() {
        Map<String, Object> map = userFavoriteService.searchUserFavorite(1L, 1, null, 0, 10);
        System.out.println(map.get("total"));
        System.out.println(map.get("rows"));
    }


}
