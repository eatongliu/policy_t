package com.gpdata.wanyou.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gpdata.wanyou.base.cache.RedisCache;
import com.gpdata.wanyou.base.common.utils.AESCoder;
import com.gpdata.wanyou.base.common.utils.CodeUtil;
import com.gpdata.wanyou.base.controller.BaseController;
import com.gpdata.wanyou.base.service.RedisOperateService;
import com.gpdata.wanyou.base.vo.BeanResult;
import com.gpdata.wanyou.system.entity.User;
import com.gpdata.wanyou.system.service.UserService;
import com.gpdata.wanyou.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping
public class UserRegistController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private RedisOperateService redisOperateService;
    @Resource(name = "redisCache")
    private RedisCache redisCache;

    /**
     * 注册账户页面
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/reg-log-in")
    public String RegPage(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.debug("/reg-log-in");
        // 注册页面跳转 如果是弹出层方式则此方法不需要

        return "users/user-log-in";
    }

    /**
     * 账户重复判断
     */
    @RequestMapping(value = "/isexistphone/{phoneNumber}", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult getIsExistPhoneNo(@PathVariable String phoneNumber) {
        BeanResult result = null;
        try {
//            String loginname = request.getParameter("loginname");
            User userInfo = userService.getUserByName(phoneNumber);
            if (userInfo != null) {
                // 如果该用户手机号存在则重复
                result = BeanResult.error("手机号已被使用");
            } else {
                result = BeanResult.success("手机号未被使用");
            }
        } catch (Exception e) {
            LOGGER.error("Exception:{}", e);
            result = BeanResult.error("出现异常");
        }
        return result;
    }

    /**
     * 注册或更换------手机验证码发送 或许需要确定短信模板话术<br/>
     * SMS_8266244 验证码${code}，您正在注册成为${product}用户，感谢您的支持！
     *
     * @param request
     */
    @RequestMapping(value = "/regcode", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult userRegSMSCode(HttpServletRequest request, @RequestParam String phoneNumber) {
        BeanResult result = null;
        try {
            // 发送前先判断该cookie是否存在 如果存在就不发送短信 否则发送
            String rediskey = "";
            String regcode = "";
            Cookie[] cookies = request.getCookies();
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                LOGGER.debug(cookie.getName());
                if (cookie.getName().equalsIgnoreCase("_gp_webid")) {
                    LOGGER.debug("regcode {}", cookie.getValue());
                    rediskey = cookie.getValue();
                }
            }
            regcode = redisOperateService.rcvVFCodeFromRds(rediskey);
            LOGGER.debug("value {}", regcode);
            if ("".equals(regcode) || regcode == null) {
                // 密钥
                String SEED = "wwwccbdecn" + new StringBuffer(phoneNumber).reverse().toString()
                        + "datatrade";
                // 生成6位随机验证码
                regcode = CodeUtil.getCode(6);
                LOGGER.debug("验证手机号passPhone：" + phoneNumber + "  验证码regcode：" + regcode);

                // 短信模板参数 例：验证码${code}，您正在进行${product}身份验证
                Map<String, String> map = new HashMap<String, String>();
                map.put("code", regcode);
                map.put("product", "政查查");
                String sms_param = JSON.toJSONString(map);

                //发送验证码
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("signname", "注册验证");
                maps.put("rec_num", phoneNumber);
                maps.put("sms_param", sms_param);
                maps.put("template_code", "SMS_10651200");
                LOGGER.debug("configAliSmssend:{}", maps);
                String rspBody = ALISMSClient.createSMSCode(maps);
                LOGGER.debug("rspBody:{}", rspBody);

                JSONObject root = JSONObject.parseObject(rspBody);
                JSONObject resultJson = root.getJSONObject("alibaba_aliqin_fc_sms_num_send_response").getJSONObject("result");
                //验证码发送成功后存入redis
                if (resultJson.getBoolean("success")) {
                    // 对cookie参数进行res加密, 放入cookie等待验证 ,失效时间1分钟
                    redisOperateService.sendVFCodeToRds(rediskey, regcode, SEED);
                }
                result = BeanResult.success(rspBody);
            }
        } catch (Exception e) {
            LOGGER.error("Exception", e);
            result = BeanResult.error("发送验证码失败");
        }

        return result;

    }

    /**
     * 快速注册账户 包括 ：昵称,手机号,密码
     *
     * @param request
     */
    @RequestMapping(value = "/reg", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult userRegist(@RequestBody User userInfo, @RequestParam String verifyCode, HttpServletRequest request) {
        BeanResult result = null;
        try {
            LOGGER.debug("/reg");

            /***
             * 手机号短信验码证步骤
             */
            String rediskey = "";
            String regcode = "";
            Cookie[] cookies = request.getCookies();
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                LOGGER.debug(cookie.getName());
                if (cookie.getName().equalsIgnoreCase("_gp_webid")) {
                    LOGGER.debug("regcode {}", cookie.getValue());
                    rediskey = cookie.getValue();
                }
            }
            regcode = redisOperateService.rcvVFCodeFromRds(rediskey);
            LOGGER.debug("value {}", regcode);
            String SEED = "wwwccbdecn" + new StringBuffer(userInfo.getUserName()).reverse().toString()
                    + "datatrade";
            // 验证码regcode 密钥seed 解密后AfterAES
            if (AESCoder.decryptBase64ToString(regcode, SEED).equals(verifyCode)) {
                LOGGER.debug("regcode: " + regcode + "  SEED: " + SEED + "   AfterAES: "
                        + AESCoder.decryptBase64ToString(regcode, SEED));

                userInfo.setPassword(MD5.sign(userInfo.getPassword(), ConfigUtil.getConfig("regkey"), "utf-8"));
                // 默认支付密码
                userInfo.setPayPwd(MD5.sign(userInfo.getPassword(), ConfigUtil.getConfig("regkey"), "utf-8"));
                // 创建时间
                userInfo.setCreateDate(DateUtils.getNowToTimeStamp("yyyy-MM-dd HH:mm:ss"));
                // 默认手机号
                userInfo.setPhone(userInfo.getUserName());
                // 默认固话
                userInfo.setTel(userInfo.getUserName());
                // 注册
                userInfo = HtmlParseUtils.htmlParseCode(userInfo, User.class);
                Long userId = userService.addUser(userInfo);
                if (userId != null) {
                    result = BeanResult.success(userId);
                } else {
                    result = BeanResult.error("注册失败");
                }
            } else {
                result = BeanResult.error("注册失败");
            }
        } catch (Exception e) {
            LOGGER.error("Exception", e);
            result = BeanResult.error("发生错误");
        }
        return result;
    }

    /**
     * 绑定或修改 ---邮箱 验证码发送 或许需要确定邮件模板话术
     *
     * @param request
     */
    @RequestMapping(value = "/user/sendemailcode", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult sendEmailCode(HttpServletRequest request) {
        BeanResult result = null;
        try {
            // 验证码提交邮箱 暂定名 email
            // 从cookie得到用户名
            User userInfo = getCurrentUser(request);
            String email = request.getParameter("email");
            String rediskey = "";
            String regcode = "";
            Cookie[] cookies = request.getCookies();
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                LOGGER.debug(cookie.getName());
                if (cookie.getName().equalsIgnoreCase("_gp_webid")) {
                    LOGGER.debug("regcode {}", cookie.getValue());
                    rediskey = cookie.getValue();
                }
            }
            regcode = redisOperateService.rcvVFCodeFromRds(rediskey);
            LOGGER.debug("value {}", regcode);
            if ("".equals(regcode) || regcode == null) {
                // 密钥
                String SEED = "wwwccbdecn" + new StringBuffer(email).reverse().toString() + "datatrade";
                // 生成6位随机验证码
                regcode = CodeUtil.getCode(6);
                LOGGER.debug("验证邮箱地址email：" + email + "  验证码regcode：" + regcode);

                // 邮件发送的用户名抬头，验证码，邮件相关装饰图片
                EmailUtil.sendHtmlEmail(userInfo, regcode, "", "政查查");
                // 对cookie参数进行res加密, 放入cookie等待验证 ,失效时间2分钟
                redisOperateService.sendVFCodeToRds(rediskey, regcode, SEED);
                result = BeanResult.success("SUCCESS");
            }
        } catch (Exception e) {
            LOGGER.error("Exception:{}", e);
            result = BeanResult.error("邮件发送失败");
        }
        return result;
    }

    /**
     * 验证邮件验证码
     */
    @RequestMapping(value = "/user/checkemailcode", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult checkemailcode(HttpServletRequest request) {
        BeanResult result = null;
        LOGGER.debug("/user/regemail");
        // 获取用户提交的邮箱 和 验证码
        String email = request.getParameter("email");
        String verifyCode = request.getParameter("verifyCode");
        try {
            /***
             * 邮箱绑定验码证步骤
             */
            String rediskey = "";
            String regcode = "";
            Cookie[] cookies = request.getCookies();
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                LOGGER.debug(cookie.getName());
                if (cookie.getName().equalsIgnoreCase("_gp_webid")) {
                    LOGGER.debug("regcode {}", cookie.getValue());
                    rediskey = cookie.getValue();
                }
            }
            regcode = redisOperateService.rcvVFCodeFromRds(rediskey);
            LOGGER.debug("value {}", regcode);
            String SEED = "wwwccbdecn" + new StringBuffer(email).reverse().toString()
                    + "datatrade";
            if (AESCoder.decryptBase64ToString(regcode, SEED).equals(verifyCode)) {
                // 验证码regcode 密钥seed 解密后AfterAES
                LOGGER.debug("regcode: " + regcode + "  SEED: " + SEED + "   AfterAES: "
                        + AESCoder.decryptBase64ToString(regcode, SEED));
                result = BeanResult.success("验证成功");
            } else {
                result = BeanResult.error("验证失败");
            }
        } catch (Exception e) {
            LOGGER.error("Exception", e);
            result = BeanResult.error("验证出现异常，请重新进行验证");
        }
        return result;
    }

    /**
     * 绑定邮箱——验证完成后
     *
     * @param request
     * @param email
     * @return
     */
    @RequestMapping(value = "/user/bindemail", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult bindEmail(HttpServletRequest request, @RequestParam String email) {
        BeanResult result = null;
        try {
            User user = getCurrentUser(request);

            if (StringUtils.isNotBlank(email)) {
                user.setEmail(email);
            }

            user = HtmlParseUtils.htmlParseCode(user, User.class);
            userService.updateUser(user);
            result = BeanResult.success("绑定成功");
        } catch (Exception e) {
            LOGGER.error("Exception", e);
            result = BeanResult.error("出现异常");
        }
        return result;

    }

    /**
     * 忘记密码流程开始
     */

    /**
     * 忘记密码发送短信<br/>
     * 根据账户名称得到用户手机号给手机发送验证码,<br/>
     * 一般默认手机号是账号<br/>
     * SMS_8266241 验证码${code}，您正在尝试变更${product}重要信息，请妥善保管账户信息。
     */
    @RequestMapping(value = "/forgetsendcode", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult forgetPwdSMSCode(@RequestParam String phoneNumber, HttpServletRequest request) {
        BeanResult result = null;
        try {
            // 得到用户信息，获取手机号
            User user = userService.getUserByName(phoneNumber);
            if (user == null) {
                return BeanResult.error("该手机号未关联账号");
            }

            // 发送前先判断该cookie是否存在 如果存在就不发送短信 否则发送
            String rediskey = "";
            String regcode = "";
            Cookie[] cookies = request.getCookies();
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                LOGGER.debug(cookie.getName());
                if (cookie.getName().equalsIgnoreCase("_gp_webid")) {
                    LOGGER.debug("regcode {}", cookie.getValue());
                    rediskey = cookie.getValue();
                }
            }
            regcode = redisOperateService.rcvVFCodeFromRds(rediskey);
            LOGGER.debug("value {}", regcode);

            if ("".equals(regcode) || regcode == null) {
                // 密钥
                if (StringUtils.isBlank(phoneNumber)) {
                    phoneNumber = "ccbde";
                }
                String SEED = "wwwccbdecn" + new StringBuffer(phoneNumber).reverse().toString()
                        + "datatrade";
                // 生成6位随机验证码
                regcode = CodeUtil.getCode(6);
                LOGGER.debug("验证手机号passphone：" + phoneNumber + "  验证码regcode：" + regcode);

                // 短信模板参数 例：验证码${code}，您正在进行${product}身份验证
                Map<String, String> map = new HashMap<String, String>();
                map.put("code", regcode);
                map.put("product", "政查查");
                String sms_param = JSON.toJSONString(map);

                boolean flag = false;
                String check = "^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(user.getUserName());
                flag = matcher.matches();
                if (flag) {
                    /**
                     * 发送验证码
                     **/
                    Map<String, String> maps = new HashMap<String, String>();
                    maps.put("signname", "变更验证");
                    maps.put("rec_num", phoneNumber);
                    maps.put("sms_param", sms_param);
                    maps.put("template_code", "SMS_10651197");
                    LOGGER.debug("configAliSmssend:{}", maps);
                    String rspBody = ALISMSClient.createSMSCode(maps);
                    LOGGER.debug("rspBody:{}", rspBody);

                    JSONObject root = JSONObject.parseObject(rspBody);
                    JSONObject resultJson = root.getJSONObject("alibaba_aliqin_fc_sms_num_send_response").getJSONObject("result");
                    //验证码发送成功后存入redis
                    if (resultJson.getBoolean("success")) {
                        // 对cookie参数进行res加密, 放入cookie等待验证 ,失效时间1分钟
                        redisOperateService.sendVFCodeToRds(rediskey, regcode, SEED);
                    }

                    result = BeanResult.success(rspBody);
                } else {
                    EmailUtil.sendHtmlEmail(user, regcode, "", "政查查");
                    result = BeanResult.success("重新发送验证邮件");
                }
            }
            request.setAttribute("revpass", user);
        } catch (Exception e) {
            LOGGER.error("Exception:{}", e);
            result = BeanResult.error("发生异常");
        }
        return result;
    }

    /**
     * 忘记支付密码流程开始
     */

    /**
     * 忘记支付密码发送短信<br/>
     * 根据账户名称得到用户手机号给手机发送验证码,<br/>
     * 一般默认手机号是账号<br/>
     * SMS_8266241 验证码${code}，您正在尝试变更${product}重要信息，请妥善保管账户信息。
     */
    @RequestMapping(value = "/user/sendtopay", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult forgetPaySMSCode(@ModelAttribute User userInfo, HttpServletRequest request,
                                       HttpServletResponse response) {
        BeanResult result = null;
        try {
            // 发送前先判断该cookie是否存在 如果存在就不发送短信 否则发送
            /***
             * 手机号短信验码证步骤
             */
            String rediskey = "";
            String regcode = "";
            Cookie[] cookies = request.getCookies();
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                LOGGER.debug(cookie.getName());
                if (cookie.getName().equalsIgnoreCase("_gp_webid")) {
                    LOGGER.debug("regcode {}", cookie.getValue().toString());
                    rediskey = cookie.getValue().toString();
                }
            }
            regcode = redisOperateService.rcvVFCodeFromRds(rediskey);
            LOGGER.debug("value {}", regcode);

            // 得到用户信息，获取手机号
            String loginname = userInfo.getUserName();
            if (loginname != null && loginname.length() != 0) {
                userInfo = userService.getUserByName(loginname);
                String phone = userInfo.getPhone();

                if ("".equals(regcode) || regcode == null) {
                    // 密钥
                    if ("".equals(phone) || phone == null) {
                        phone = "ccbde";
                    }
                    String SEED = "wwwccbdecn" + new StringBuffer(phone).reverse().toString()
                            + "datatrade";
                    // 生成6位随机验证码
                    regcode = CodeUtil.getCode(6);
                    LOGGER.debug("验证手机号passphone：" + phone + "  验证码regcode：" + regcode);

                    // 短信模板参数 例：验证码${code}，您正在进行${product}身份验证
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("code", regcode);
                    map.put("product", "政查查");
                    String sms_param = JSON.toJSONString(map);
                    // 对cookie参数进行res加密, 放入cookie等待验证 ,失效时间2分钟
                    redisOperateService.sendVFCodeToRds(rediskey, regcode, SEED);
                    boolean flag = false;
                    String check = "^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";
                    Pattern regex = Pattern.compile(check);
                    Matcher matcher = regex.matcher(loginname);
                    flag = matcher.matches();
                    if (flag) {
                        /**
                         * 发送验证码 暂时不需要
                         **/
                        Map<String, String> maps = new HashMap<String, String>();
                        maps.put("signname", "变更验证");
                        maps.put("rec_num", phone);
                        maps.put("sms_param", sms_param);
                        maps.put("template_code", "SMS_10651197");
                        LOGGER.debug("configAliSmssend:{}", maps);
                        String rspBody = ALISMSClient.createSMSCode(maps);
                        LOGGER.debug("rspBody:{}", rspBody);
                        result = BeanResult.success(rspBody);
                    } else {
                        EmailUtil.sendHtmlEmail(userInfo, regcode, "", "政查查");
                        result = BeanResult.success("重新发送验证邮件");
                    }

                }

            }
            request.setAttribute("revpass", userInfo);
        } catch (Exception e) {
            LOGGER.error("Exception:{}", e);
            result = BeanResult.error("发生异常");
        }
        return result;
    }

    /***
     * 忘记密码----验证短信验证码
     */
    @RequestMapping(value = "/forgetpasscheck", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult forgetCheckSMSCode(HttpServletRequest request, @RequestParam String phoneNumber, @RequestParam String verifyCode) {
        BeanResult result = null;
        try {

            String rediskey = "";
            String regcode = "";
            Cookie[] cookies = request.getCookies();
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                LOGGER.debug(cookie.getName());
                if (cookie.getName().equalsIgnoreCase("_gp_webid")) {
                    LOGGER.debug("regcode {}", cookie.getValue());
                    rediskey = cookie.getValue();
                }
            }
            regcode = redisOperateService.rcvVFCodeFromRds(rediskey);
            LOGGER.debug("value {}", regcode);
            // 密钥
            if (StringUtils.isBlank(phoneNumber)) {
                phoneNumber = "ccbde";
            }
            String SEED = "wwwccbdecn" + new StringBuffer(phoneNumber).reverse().toString()
                    + "datatrade";
            if (AESCoder.decryptBase64ToString(regcode, SEED).equals(verifyCode)) {
                // 验证码regcode 密钥seed 解密后AfterAES0
                LOGGER.debug("regcode: " + regcode + "  SEED: " + SEED + "   AfterAES: "
                        + AESCoder.decryptBase64ToString(regcode, SEED));
                result = BeanResult.success("验证码正确");
            } else {
                result = BeanResult.error("验证码错误");
            }
        } catch (Exception e) {
            LOGGER.error("Exception", e);
            result = BeanResult.error("发生异常");
        }
        return result;
    }

    /**
     * 手机号修改密码, <br/>
     * 并跳转提示成功页之后跳转到首页或登录页
     */
    @RequestMapping(value = "/revisepwd", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult revisePwdByPhone(@RequestParam String phoneNum, @RequestParam String password) {
        BeanResult result = null;
        try {
            //通过手机号找到用户
            User user = userService.getUserByName(phoneNum);
            if (user == null) {
                return BeanResult.error("手机号未关联账号");
            }
            if (StringUtils.isNotBlank(password)) {
                user.setPassword(MD5.sign(password, ConfigUtil.getConfig("regkey"), "utf-8"));
            }
            if (StringUtils.isNotBlank(user.getPayPwd())) {
                user.setPayPwd(MD5.sign(password, ConfigUtil.getConfig("regkey"), "utf-8"));
            }
            user = HtmlParseUtils.htmlParseCode(user, User.class);
            userService.updateUser(user);
            result = BeanResult.success("修改成功");
        } catch (Exception e) {
            LOGGER.error("Exception", e);
            result = BeanResult.error("出现异常");
        }
        return result;

    }

    /**
     * 普通方式修改密码, <br/>
     * 并跳转提示成功页之后跳转到首页或登录页
     */
    @RequestMapping(value = "/user/revisepwd", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult revisePassword(HttpServletRequest request, @RequestParam String password, @RequestParam String newPwd) {
        BeanResult result = null;
        try {
            User user = getCurrentUser(request);
            String pwd_ = MD5.sign(password, ConfigUtil.getConfig("regkey"), "utf-8");
            //判断密码是否输入正确
            if (user.getPassword().equals(pwd_)) {
                user.setPassword(MD5.sign(newPwd, ConfigUtil.getConfig("regkey"), "utf-8"));
                if (StringUtils.isNotBlank(user.getPayPwd())) {
                    user.setPayPwd(MD5.sign(newPwd, ConfigUtil.getConfig("regkey"), "utf-8"));
                }
            } else {
                return BeanResult.error("原密码输入错误");
            }
            user = HtmlParseUtils.htmlParseCode(user, User.class);
            userService.updateUser(user);
            result = BeanResult.success("修改成功");
        } catch (Exception e) {
            LOGGER.error("Exception", e);
            result = BeanResult.error("出现异常");
        }
        return result;

    }

    /**
     * 用户身份验证接口
     * 判断当前登录用户输入的密码是否正确
     *
     * @Author yaz
     * @Date 2016/12/17 10:31
     */
    @RequestMapping(value = "/user/judgepwd", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BeanResult judgePwd(HttpServletRequest request, @RequestParam String password) {
        try {
            User user = getCurrentUser(request);
            String pwd_ = MD5.sign(password, ConfigUtil.getConfig("regkey"), "utf-8");
            if (user.getPassword().equals(pwd_)) {
                return BeanResult.success("验证成功！");
            }
        } catch (Exception e) {
            logger.error("验证密保报错！异常：{}", e);
            return BeanResult.error("验证失败！");
        }
        return BeanResult.error("密码错误！");
    }
}