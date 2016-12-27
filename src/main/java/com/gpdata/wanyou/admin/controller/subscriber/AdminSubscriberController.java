package com.gpdata.wanyou.admin.controller.subscriber;

import com.gpdata.wanyou.base.controller.AdminBaseController;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.subscriber.entity.Subscriber;
import com.gpdata.wanyou.subscriber.service.SubWithPolicyService;
import com.gpdata.wanyou.subscriber.service.SubscriberService;
import com.gpdata.wanyou.utils.SimpleBeanPropertiesUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 订阅前台管理
 * Created by guoxy on 2016/11/29.
 */
@Controller
@RequestMapping("/admin")
public class AdminSubscriberController extends AdminBaseController {
    private static final Logger logger = LoggerFactory.getLogger(AdminSubscriberController.class);
    @Autowired
    private SubscriberService subscriberService;
    @Autowired
    private SubWithPolicyService withPolicyService;

    /**
     * 1.3	订阅列表 /su/ls/{offset}/{limit}
     */
    @RequestMapping(value = "/su/ls/{offset}/{limit}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult adminListSubscriber(@PathVariable Integer offset, @PathVariable Integer limit, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        try {
               /*TODO 判断管理登录*/
            // AdminUser admin = getCurrentAdminUser(request);
            /*TODO 获取用户手机号和用户id  订阅id*/
            String mobile = "";
            Integer uid = null;
            Long sid = null;
            Map<String, Object> resultMap = subscriberService.searchSubscriber(uid, sid, mobile,null,null, offset, limit);
            data.put("data", resultMap);
            result = BeanResult.success(resultMap);
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            result = BeanResult.error("异常 ： " + e);
        }

        return result;
    }

    /**
     * 1.4	订阅查看 /su/s/{sid}
     */
    @RequestMapping(value = "/su/s/{sid}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult getSubscriber(@PathVariable Long sid, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        try {
               /*TODO 判断管理登录*/
            // AdminUser user = (AdminUser) request.getAttribute("currentUser");
            Subscriber subscriber = subscriberService.getSubscriber(sid);
            data.put("data", subscriber);
            logger.debug("data:{}", data);
            result = BeanResult.success(data);
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            result = BeanResult.error("异常 ： " + e);
        }

        return result;
    }


    /**
     * 1.6	订阅修改 /su/u/{sid}
     */
    @RequestMapping(value = "/su/u/{sid}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult updateSubscriber(@RequestBody Subscriber subscriber, @PathVariable Long sid, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        try {
             /*TODO 判断管理登录*/
            // AdminUser user = (AdminUser) request.getAttribute("currentUser");

            Subscriber oldSubscriber = subscriberService.getSubscriber(sid);
                /*里面的 copyNotnullProperites 方法， 可以将一个对象中的非空的值复制给另一个对象*/
            SimpleBeanPropertiesUtil.copyNotNullProperties(subscriber, oldSubscriber);
            subscriberService.updateSubscriber(subscriber);
            result = BeanResult.success("SUCCESS");

        } catch (Exception e) {
            logger.error("Exception:{}", e);
            result = BeanResult.error("异常 ： " + e);
        }

        return result;
    }


    /**
     * 订阅推送
     * 1. 审核通过后，根据订阅id得到订阅的关键词组
     * 2. 提交订阅词组和订阅id给ES搜索
     * 3. ES返回该订阅关联的政策id、政策标题、政策类型、
     * 4. 将返回结果存储到sub_with_policy表中
     * 5. 定时器每天更新推送
     */
    @RequestMapping(value = "/su/send/{sid}", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult doSubscriber(@PathVariable Long sid, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        try {
             /*TODO 判断管理登录*/
            // AdminUser user = (AdminUser) request.getAttribute("currentUser");

            Subscriber subscriber = subscriberService.getSubscriber(sid);
            String keyWords = subscriber.getKeyWords();
            keyWords = keyWords.replaceAll(",", " ");
            /*TODO  假装把id和词组 丢给ES  ES假装返回 订阅id 政策id、政策标题、政策类型*/
            /*订阅id*/
            Pair<Integer, List<Long>> arrayList = withPolicyService.ftRetrieval(subscriber.getSid(), subscriber.getUid(), keyWords, 0, 20);

            result = BeanResult.success(arrayList);

        } catch (Exception e) {
            logger.error("Exception:{}", e);
            result = BeanResult.error("异常 ： " + e);
        }

        return result;
    }

}
