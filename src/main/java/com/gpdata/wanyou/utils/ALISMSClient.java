package com.gpdata.wanyou.utils;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ALISMSClient {
    // ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    private static final Logger LOGGER = LoggerFactory.getLogger(ALISMSClient.class);
    /**
     * 请求地址
     */
    private static String ALI_APPKEY_SMS_URL = ConfigUtil.getConfig("sms.url");
    /**
     * API接口名称。
     */
    private static String ALI_APPKEY_SMS_METHOD = ConfigUtil.getConfig("sms.method");
    /**
     * TOP分配给应用的AppKey。
     */
    private static String ALI_APPKEY_SMS_APP_AKY = ConfigUtil.getConfig("sms.appkey");
    /**
     * App Secret
     */
    private static String ALI_APPKEY_SMS_APPSECRET = ConfigUtil.getConfig("sms.secret");
    /**
     * 当此API的标签上注明：“需要授权”，则此参数必传；“不需要授权”，则此参数不需要传；“可选授权”，则此参数为可选。
     */
    private static String ALI_APPKEY_SMS_SESSION = ConfigUtil.getConfig("sms.session");
    /**
     * 时间戳，格式为yyyy-MM-dd HH:mm:ss，时区为GMT+8，例如：2015-01-01
     * 12:00:00。淘宝API服务端允许客户端请求最大时间误差为10分钟
     */

    private static String ALI_APPKEY_SMS_TIMESTAMP = ConfigUtil.getConfig("sms.timestamp");
    /**
     * 响应格式。默认为xml格式，可选值：xml，json。
     */
    private static String ALI_APPKEY_SMS_FORMAT = ConfigUtil.getConfig("sms.format");
    /**
     * API协议版本，可选值：2.0。
     */
    private static String ALI_APPKEY_SMS_V = ConfigUtil.getConfig("sms.version");
    /**
     * 合作伙伴身份标识。
     */
    private static String ALI_APPKEY_SMS_PARTNER_ID = ConfigUtil.getConfig("sms.parentid");
    /**
     * 被调用的目标AppKey，仅当被调用的API为第三方ISV提供时有效。
     */
    private static String ALI_APPKEY_SMS_TARGET_APP_KEY = ConfigUtil.getConfig("sms.targetappkey");
    /**
     * 是否采用精简JSON返回格式，仅当format=json时有效，默认值为：false。
     */
    private static String ALI_APPKEY_SMS_SIMPLIFY = ConfigUtil.getConfig("sms.simplify");
    /**
     * 签名的摘要算法，可选值为：hmac，md5。
     */
    private static String ALI_APPKEY_SMS_SIGN_METHOD = ConfigUtil.getConfig("sms.signmethod");
    /**
     * API输入参数签名结果，签名算法介绍请点击http://open.taobao.com/doc2/detail.htm?articleId=
     * 101617&docType=1&treeId=1。
     */
    private static String ALI_APPKEY_SMS_SIGN = ConfigUtil.getConfig("sms.sign");

    // AlibabaAliqinFcSmsNumSendRequest body
    /**
     * 公共回传参数，在“消息返回”中会透传回该参数；举例：用户可以传入自己下级的会员ID，在消息返回时，该会员ID会包含在内，
     * 用户可以根据该会员ID识别是哪位会员使用了你的应用
     */
    private static String EXTEND = ConfigUtil.getConfig("sms.extend");
    /**
     * 短信类型，传入值请填写normal
     */
    private static String SMS_TYPE = ConfigUtil.getConfig("sms.type");
    /**
     * 短信签名，传入的短信签名必须是在阿里大鱼“管理中心-短信签名管理”中的可用签名。如“阿里大鱼”已在短信签名管理中通过审核，则可传入”阿里大鱼“（
     * 传参时去掉引号）作为短信签名。短信效果示例：【阿里大鱼】欢迎使用阿里大鱼服务。
     */
    private static String SMS_FREE_SIGN_NAME = ConfigUtil.getConfig("sms.signname");
    /**
     * 短信模板变量，传参规则{"key":"value"}，key的名字须和申请模板中的变量名一致，多个变量之间以逗号隔开。示例：针对模板“验证码${
     * code}，您正在进行${product}身份验证，打死不要告诉别人哦！”，传参时需传入{"code":"1234","product":
     * "alidayu"}
     */
    // private static String SMS_PARAM;
    /**
     * 短信接收号码。支持单个或多个手机号码，传入号码为11位手机号码，不能加0或+86。群发短信需传入多个号码，以英文逗号分隔，
     * 一次调用最多传入200个号码。示例：18600000000,13911111111,13322222222
     */
    // private static String REC_NUM;
    /**
     * 短信模板ID，传入的模板必须是在阿里大鱼“管理中心-短信模板管理”中的可用模板。示例：SMS_585014
     */
    private static String SMS_TEMPLATE_CODE = ConfigUtil.getConfig("sms.templatecode");

    /**
     * 短信发送方法(参数Map)
     *
     * @param signname
     * @param rec_num
     * @param sms_param
     * @param template_code
     * @return 短信验证码，使用同一个签名，对同一个手机号码发送短信验证码，允许每分钟1条，累计每小时7条。
     * 短信通知，使用同一签名、同一模板，对同一手机号发送短信通知，允许每天50条（自然日）。
     */
    public static String createSMSCode(Map<String, String> maps) {
        TaobaoClient client = new DefaultTaobaoClient(ALI_APPKEY_SMS_URL, ALI_APPKEY_SMS_APP_AKY,
                ALI_APPKEY_SMS_APPSECRET);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend(EXTEND);
        req.setSmsType(SMS_TYPE);
        req.setSmsFreeSignName(maps.get("signname"));
        req.setRecNum(maps.get("rec_num"));
        req.setSmsParamString(maps.get("sms_param"));
        req.setSmsTemplateCode(maps.get("template_code"));
        AlibabaAliqinFcSmsNumSendResponse rsp;
        try {
            rsp = client.execute(req);
            LOGGER.debug("短信返回参数rsp:{}" + "errcode:  " + rsp.getErrorCode() + "  msg: "
                    + rsp.getMsg() + "  body: " + rsp.getBody());
            return rsp.getBody();
        } catch (ApiException e) {

            LOGGER.error("ApiException:{}", e);
        }
        return null;

    }

}
