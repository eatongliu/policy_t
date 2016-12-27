package com.gpdata.wanyou.subscriber.controller;

import com.gpdata.wanyou.ansj.util.AnsjUtil;
import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.subscriber.constant.SubscriberConstant;
import com.gpdata.wanyou.subscriber.entity.Subscriber;
import com.gpdata.wanyou.subscriber.service.SubWithPolicyService;
import com.gpdata.wanyou.subscriber.service.SubscriberService;
import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.utils.SimpleBeanPropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 订阅前台管理
 * Created by guoxy on 2016/11/29.
 */
@Controller
@RequestMapping()
public class SubscriberController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SubscriberController.class);

    @Autowired
    private SubscriberService subscriberService;
    @Autowired
    private AnsjUtil ansj;
    @Autowired
    private SubWithPolicyService subWithPolicyService;

    /**
     * 1.1 分词 /su/c
     */
    @RequestMapping(value = "/su/c", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult getSubscriberParam(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        String subscriber = params.get("subscriber").toString();
        if (null == subscriber) {
            return BeanResult.error("请输入文字后再进行解析！");
        }
        if ("".equals(subscriber)) {
            return BeanResult.error("请输入文字后再进行解析！");
        }

        BeanResult result = null;
        try {
            result = BeanResult.success(ansj.parseString(subscriber, true));
        } catch (Exception e) {
            logger.error("分词解析失败！{}", e);
            result = BeanResult.error("未能正确解析，请更换常用词！");
        }
        return result;
    }

    /**
     * 1.2	订阅提交 /su/a
     */
    @RequestMapping(value = "/su/a", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult addSubscriber(@RequestBody Subscriber subscriber, HttpServletRequest request) {
        BeanResult result = null;
        try {
            User user = getCurrentUser(request);
            subscriber.setCreateDate(new Date());
            subscriber.setMobile(user.getUserName());
            subscriber.setUid(user.getUserId());
            subscriber.setStatus(SubscriberConstant.SUBSCRIBER_CHECK);
            Long sid = subscriberService.addSubscriber(subscriber);
            result = BeanResult.success(sid);
        } catch (Exception e) {
            logger.error("保存订阅信息失败！{}", e);
            result = BeanResult.error("出错了，保存失败！请稍后再试或联系网站管理员！");
        }
        return result;
    }

    /**
     * 1.3	订阅列表 /su/ls/{offset}/{limit}
     */
    @RequestMapping(value = "/su/ls/{offset}/{limit}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult listSubscriber(@PathVariable Integer offset,
                                     @PathVariable Integer limit,
                                     @RequestParam(required = false, defaultValue = "") String keyWord,
                                     HttpServletRequest request) {
        BeanResult result = null;
        User user = (User) request.getAttribute("currentUser");
        try {
            String mobile = user.getUserName();
            result = BeanResult.success(subscriberService.searchSubscriber(Integer.valueOf(String.valueOf(user.getUserId())), null, mobile, null, keyWord, offset, limit));
        } catch (Exception e) {
            logger.error("订阅信息列表查询报错！，当前用户id{}，异常信息{}", user.getUserId(), e);
            result = BeanResult.error("查询出错！请稍后再试！");
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
            Subscriber subscriber = subscriberService.getSubscriber(sid);
            data.put("data", subscriber);
            result = BeanResult.success(data);
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            result = BeanResult.error("异常 ： " + e);
        }

        return result;
    }

    /**
     * 1.5	订阅关联推送 /su/c/{sid}
     */
    @RequestMapping(value = "/su/c/{sid}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult sendSubscriber(@PathVariable String sid, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        try {
            /*TODO 从后台获取推送信息*/
            result = BeanResult.success("");
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
            User user = (User) request.getAttribute("currentUser");
            Subscriber oldSubscriber = subscriberService.getSubscriber(sid);
            if (!oldSubscriber.getUid().equals(user.getUserId())) {
                result = BeanResult.error("异常 ： 修改错误,您没有订阅该内容");
            } else {
                /*里面的 copyNotnullProperites 方法， 可以将一个对象中的非空的值复制给另一个对象*/
                SimpleBeanPropertiesUtil.copyNotNullProperties(subscriber, oldSubscriber);
                subscriberService.updateSubscriber(subscriber);
                result = BeanResult.success("SUCCESS");
            }

        } catch (Exception e) {
            logger.error("Exception:{}", e);
            result = BeanResult.error("异常 ： " + e);
        }

        return result;
    }

    /**
     * 1.7	订阅删除 /su/d/{sid}
     */
    @RequestMapping(value = "/su/d/{sid}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult deleteSubscriber(@PathVariable Long sid, HttpServletRequest request) {
        BeanResult result = null;
        Map<String, Object> data = new HashMap<>();
        try {
            subscriberService.deleteSubscriber(sid);
            result = BeanResult.success("SUCCESS");
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            result = BeanResult.error("异常 ： " + e);
        }

        return result;
    }

    /**
     * 订阅政策 列表
     *
     * @Author yaz
     * @Date 2016/12/14 13:46
     */
    @RequestMapping(value = "/su/sp/{offset}/{limit}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult getSubWithPolicyList(@PathVariable Integer offset,
                                           @PathVariable Integer limit,
                                           @RequestParam(required = false, defaultValue = "0") Long sid,
                                           @RequestParam(required = false, defaultValue = "") String title,
                                           HttpServletRequest request) {
        User user = getCurrentUser(request);
        if (0 == sid) {
            sid = null;
        }
        try {
            return BeanResult.success(subWithPolicyService.searchPolicy(user.getUserId(), null, sid, title, offset, limit));
        } catch (Exception e) {
            logger.error("订阅书房列表查询错误！异常信息：{}", e);
            return BeanResult.error("系统繁忙，请稍后再试！");
        }
    }

    /**
     * 个人中心页面右下角推送文章
     * 新用户获取默认的订阅列表
     *
     * @Author yaz
     * @Date 2016/12/15 11:26
     */
    @RequestMapping(value = "/su/sp/n/{offset}/{limit}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult getNoSubWithPolicyList(@PathVariable Integer offset,
                                             @PathVariable Integer limit,
                                             HttpServletRequest request) {
        User user = getCurrentUser(request);
        try {
            Map<String, Object> rs = subWithPolicyService.searchPolicy(user.getUserId(), null, null, null, offset, limit);
            if ((int) rs.get("total") == 0) {
                //取默认列表
                rs = subWithPolicyService.searchPolicy(-100L, null, -1L, null, offset, limit);
            }
            return BeanResult.success(rs);
        } catch (Exception e) {
            logger.error("订阅书房列表查询错误！异常信息：{}", e);
            return BeanResult.error("系统繁忙，请稍后再试！");
        }
    }

    /**
     * 用户增加读书笔记
     *
     * @Author yaz
     * @Date 2016/12/14 14:29
     */
    @RequestMapping(value = "/su/context/{pid}/{sid}", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult updateUcontext(@RequestBody Map<String, Object> params,
                                     @PathVariable Long sid, @PathVariable Long pid) {
        try {
            String context = params.get("context").toString();
            subWithPolicyService.updateContext(pid, sid, context);
            return BeanResult.success("保存成功");
        } catch (Exception e) {
            logger.error("保存用户笔记！ 异常信息：{}", e);
            return BeanResult.error("保存失败！");
        }
    }

    /**
     * 用户删除推送文章
     *
     * @Author yaz
     * @Date 2016/12/14 14:29
     */
    @RequestMapping(value = "/su/sp/{pid}/{sid}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult deleteSubWithPolicy(@PathVariable Long sid, @PathVariable Long pid) {
        try {
            subWithPolicyService.deleteSub(pid, sid);
            return BeanResult.success("保存成功");
        } catch (Exception e) {
            logger.error("删除失败！ 异常信息：{}", e);
            return BeanResult.error("删除失败！");
        }
    }

}
