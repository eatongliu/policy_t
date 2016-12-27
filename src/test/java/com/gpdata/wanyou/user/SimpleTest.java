package com.gpdata.wanyou.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.user.entity.UserKeyword;
import com.gpdata.wanyou.utils.ALISMSClient;
import com.gpdata.wanyou.utils.EmailUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by acer_liuyutong on 2016/12/12.
 */
public class SimpleTest {
    private static final Logger logger = LoggerFactory.getLogger(SimpleTest.class);

    @Test
    public void testAliSMS(){

        Map<String, String> map = new HashMap<String, String>();
        map.put("code", "684952");
        map.put("product", "政查查");
        String sms_param = JSON.toJSONString(map);

        Map<String, String> maps = new HashMap<String, String>();
        maps.put("signname", "注册验证");
        maps.put("rec_num", "18210168522");
        maps.put("sms_param", sms_param);
        maps.put("template_code", "SMS_10651200");
        logger.debug("configAliSmssend:{}", maps);

        String rspBody = ALISMSClient.createSMSCode(maps);
        JSONObject root = JSONObject.parseObject(rspBody);
        JSONObject result = root.getJSONObject("alibaba_aliqin_fc_sms_num_send_response").getJSONObject("result");

        logger.debug("rspBody1:{}", result.get("success").getClass());
        logger.debug("rspBody:{}", rspBody);
    }

    @Test
    public void testSendHtmlEmail(){
        User user = new User();
        user.setEmail("654166357@qq.com");
        user.setUserName("lyt");
        try {
            EmailUtil.sendHtmlEmail(user,"816534","","gpdata");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSendTextEmail(){
        try{
            EmailUtil.sendTextEmail("806323672@qq.com");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void test2(){
        UserKeyword keyword = new UserKeyword(null,"sds");

        System.out.println(keyword);
    }

    @Test
    public void testt3(){
        String db = "jdbc:mysql://101.200.57.229:3306/ZhihuData";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(db, "root", "gplbigdata");
            System.out.println(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}