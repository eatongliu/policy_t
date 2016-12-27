package com.gpdata.wanyou.utils;

import com.gpdata.wanyou.system.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.Random;


/**
 * 所有参数准备存入数据库中管理（未进行）
 *
 * @author guoxiaoyang
 * @date 2016年5月5日 下午1:51:05
 */
public class EmailUtil {
    // ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // 邮件发送协议
    private final static String PROTOCOL = ConfigUtil.getConfig("email.protocol");

    // SMTP邮件服务器
    private final static String HOST = ConfigUtil.getConfig("email.host");

    // SMTP邮件服务器默认端口
    private final static String PORT = ConfigUtil.getConfig("email.port");

    // 是否要求身份认证
    private final static String IS_AUTH = ConfigUtil.getConfig("email.isauth");

    // 是否启用调试模式（启用调试模式可打印客户端与服务器交互过程时一问一答的响应消息）
    private final static String IS_ENABLED_DEBUG_MOD = ConfigUtil.getConfig("email.isenableddebugmod");

    // 授权用户
    private final static String USERNAME = ConfigUtil.getConfig("email.username");
    // 授权密码
    private final static String PASSWORD = ConfigUtil.getConfig("email.password");
    // 发件人 *******待改
    private static String from = ConfigUtil.getConfig("email.from");

    // 收件人
    // private static String toMessage = "984711782@qq.com";

    // 初始化连接邮件服务器的会话信息
    private static Properties props = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtil.class);

    static {
        props = new Properties();
        props.setProperty("mail.transport.protocol", PROTOCOL);
        props.setProperty("mail.smtp.host", HOST);
        props.setProperty("mail.smtp.port", PORT);
        props.setProperty("mail.smtp.auth", IS_AUTH);
        props.setProperty("mail.debug", IS_ENABLED_DEBUG_MOD);
    }

    public static String getCode(int length) { // length表示生成字符串的长度
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 发送简单的文本邮件
     */
    public static void sendTextEmail(String toMessage) throws Exception {
        // 创建Session实例对象
        Session session = Session.getDefaultInstance(props);

        // 创建MimeMessage实例对象
        MimeMessage message = new MimeMessage(session);
        // 设置发件人
        message.setFrom(new InternetAddress(from));
        // 设置邮件主题
        message.setSubject("使用javamail发送简单文本邮件");
        // 设置收件人
        message.setRecipient(RecipientType.TO, new InternetAddress(toMessage));
        // 设置发送时间
        message.setSentDate(new Date());
        // 设置纯文本内容为邮件正文
        message.setText("使用POP3协议发送文本邮件测试!!复制不起来证明你手机不行。快告诉我，我的手机到底行不行!");
        // 保存并生成最终的邮件内容
        message.saveChanges();

        // 获得Transport实例对象
        Transport transport = session.getTransport();
        // 打开连接
        transport.connect(USERNAME, PASSWORD);
        // 将message对象传递给transport对象，将邮件发送出去
        transport.sendMessage(message, message.getAllRecipients());
        // 关闭连接
        transport.close();
    }

    /**
     * 发送简单的html邮件
     */
    public static void sendHtmlEmail(User user, String ActiCode, String path, String company)
            throws Exception {
        // 创建Session实例对象
        Session session = Session.getInstance(props, new MyAuthenticator());

        // 创建MimeMessage实例对象
        MimeMessage message = new MimeMessage(session);
        // 设置邮件主题
        message.setSubject("变更验证");
        // 设置发送人
        message.setFrom(new InternetAddress(from));
        // 设置发送时间
        message.setSentDate(new Date());
        // 设置收件人
        message.setRecipients(RecipientType.TO, InternetAddress.parse(user.getEmail()));
        // 设置html内容为邮件正文，指定MIME类型为text/html类型，并指定字符编码为gbk
        message.setContent(
                "<div style=\"width: 550px;height: 500px;margin: auto;padding-bottom:10px;border: 1px solid #e2e2e2;border-radius: 6px;-webkit-box-shadow: 0px 0px 24px 1px  rgba(0, 0, 0, 0.2);-moz-box-shadow: 0px 0px 24px 1px  rgba(0, 0, 0, 0.2);box-shadow: 0px 0px 24px 1px  rgba(0, 0, 0, 0.2);\">"
                        + "<div style=\"height: 84px;background: #0288d1;border-radius:6px 6px 0 0;\"><div><img style=\"margin-left: 25px;vertical-align:middle;\"alt=\"\" src=\"http://img.ccbde.cn/images/mail_logo.png\"><img alt=\"\"style=\"margin-left: 25px;vertical-align:middle;\" src=\"http://img.ccbde.cn/images/mail_fz.png\"></div><div style=\"clear: both;\"></div></div><div style=\"margin: 48px 42px 48px 42px;color: #7f7f7f;font-size: 16px;line-height: 26px;\"><p style=\"text-indent: 45px;\">亲爱的<span style=\"color: #0288d1;\">"
                        + user.getUserName()
                        + "</span>:</p><p style=\"text-indent: 45px;\">欢迎使用"
                        + company
                        + ",本次的验证码是：<b style=\"color:#455fff;font-weight:blod;border-bottom: thin solid;\">"
                        + ActiCode
                        + "</b>,请在<b style=\"color:#ff4c4c;font-weight:blod;\">一分钟</b>内使用此验证码。如果您并未发送过此请求,则可以放心的忽略此邮件,无需进一步采取任何操作。</p><p style=\"text-indent: 45px;\">祝您使用愉快。如有任何疑惑,欢迎与我们联系。</p></div><div class=\"mail_foot\"><img alt=\"\"style=\"float: left;margin: 0 0 45px 40px;vertical-align:middle;\" src=\"http://img.ccbde.cn/images/mail_qr.png\"><ul class=\"mail_tel\"style=\"float: left;margin:0 0 45px 42px;color: #b4b4b4;font-size: 14px;padding: 0;list-style-type:none;\"><li style=\"list-style-type:none;\">政查查</li><li style=\"list-style-type:none;\">客服：400-840-0257</li><li style=\"list-style-type:none;\">咨询：010-62662628</li><li style=\"list-style-type:none;\">邮箱：hz@ccbde.cn</li><li style=\"list-style-type:none;\">地址:北京市海淀区知春路紫金数码园3号楼8层</li><li style=\"list-style-type:none;\">东华光普大数据技术有限公司</li></ul></div></div>",
                "text/html;charset=utf-8");

        // 保存并生成最终的邮件内容
        message.saveChanges();

        // 发送邮件
        Transport.send(message);
    }

    /**
     * 发送本地已经生成好的email文件
     */
    public static void sendMailForEml(File eml) throws Exception {
        // 获得邮件会话
        Session session = Session.getInstance(props, new MyAuthenticator());
        // 获得邮件内容,即发生前生成的eml文件
        InputStream is = new FileInputStream(eml);
        MimeMessage message = new MimeMessage(session, is);
        // 发送邮件
        Transport.send(message);
    }

    /**
     * 向邮件服务器提交认证信息
     */
    static class MyAuthenticator extends Authenticator {

        private String username = USERNAME;

        private String password = PASSWORD;

        public MyAuthenticator() {
            super();
        }

        public MyAuthenticator(String username, String password) {
            super();
            this.username = username;
            this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {

            return new PasswordAuthentication(username, password);
        }
    }

    public static void main(String[] args) {
        try {
            String actiCode = EmailUtil.getCode(6);
            // sendHtmlEmail("836088466@qq.com",actiCode);
            // String toMessage = "984711782@qq.com";
            // sendTextEmail(toMessage);

            // 发送简单的html邮件
            User user = new User();
            user.setEmail("836088466@qq.com");
            user.setUserName("18612981628");
            sendHtmlEmail(
                    user,
                    actiCode,
                    "",
                    "ccbde");
            // 发送已经生成的eml邮件
            // sendMailForEml();
        } catch (Exception e) {
            LOGGER.error("Exception", e);
            e.printStackTrace();
        }

    }
}
