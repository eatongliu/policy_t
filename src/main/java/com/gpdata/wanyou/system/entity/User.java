package com.gpdata.wanyou.system.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户表
 * Created by chengchao on 2016/10/25.
 */
@Entity
@Table(name = "sys_user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    /**
     * 用户名 登录名 手机号
     */
    @Column(unique = true, nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 用户账户的余额初始状态是没有钱的. <br />
     * BigDecimal == 0
     */
    public static final BigDecimal NO_MONEY = new BigDecimal(0);
    /**
     * 乐观锁
     */
    private Integer optiVersion;
    /**
     * 注册时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createDate;
    /**
     * 上次登录时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date lastTime;
    /**
     * 上次登录Ip
     */
    private String lastIp;
    /**
     * 上次登录地点
     */
    private String lastSite;
    /**
     * 用户实名
     */
    private String realName;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 生日
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date birthday;
    /**
     * 行业id
     */
    private Integer categoryParentId;

    /**
     * 子行业id
     */
    private Integer categoryId;

    /**
     * 头像
     */
    private String headPic;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 固定电话
     */
    private String tel;

    /**
     * 绑定邮箱
     */
    private String email;

    /**
     * QQ
     */
    private String qq;

    /**
     * 微信
     */
    private String weixin;

    /**
     * 省
     */
    private Integer province;

    /**
     * 市
     */
    private Integer city;

    /**
     * 支付密码
     */
    private String payPwd;

    /**
     * 认证状态
     */
    private Integer certifyStatus;

    /**
     * 认证类型 0:未认证；1：个人认证；2：服务商认证
     */
    private Integer certifyType;

    /**
     * 专家用户
     */
    private Integer expertUser;

    /**
     * 账户金额
     */
    private BigDecimal money = NO_MONEY;

    /**
     * 用户状态(0,1,2)开启关闭 禁用
     */
    private Integer userStatus;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public static BigDecimal getNoMoney() {
        return NO_MONEY;
    }

    public Integer getOptiVersion() {
        return optiVersion;
    }

    public void setOptiVersion(Integer optiVersion) {
        this.optiVersion = optiVersion;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public String getLastSite() {
        return lastSite;
    }

    public void setLastSite(String lastSite) {
        this.lastSite = lastSite;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getCategoryParentId() {
        return categoryParentId;
    }

    public void setCategoryParentId(Integer categoryParentId) {
        this.categoryParentId = categoryParentId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    public Integer getCertifyStatus() {
        return certifyStatus;
    }

    public void setCertifyStatus(Integer certifyStatus) {
        this.certifyStatus = certifyStatus;
    }

    public Integer getCertifyType() {
        return certifyType;
    }

    public void setCertifyType(Integer certifyType) {
        this.certifyType = certifyType;
    }

    public Integer getExpertUser() {
        return expertUser;
    }

    public void setExpertUser(Integer expertUser) {
        this.expertUser = expertUser;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                ", optiVersion=" + optiVersion +
                ", createDate=" + createDate +
                ", lastTime=" + lastTime +
                ", lastSite=" + lastSite +
                ", lastIp=" + lastIp +
                ", realName='" + realName + '\'' +
                ", sex=" + sex +
                ", birthday=" + birthday +
                ", categoryParentId=" + categoryParentId +
                ", categoryId=" + categoryId +
                ", headPic='" + headPic + '\'' +
                ", phone='" + phone + '\'' +
                ", tel='" + tel + '\'' +
                ", email='" + email + '\'' +
                ", qq='" + qq + '\'' +
                ", weixin='" + weixin + '\'' +
                ", province=" + province +
                ", city=" + city +
                ", payPwd='" + payPwd + '\'' +
                ", certifyStatus=" + certifyStatus +
                ", certifyType=" + certifyType +
                ", expertUser=" + expertUser +
                ", money=" + money +
                ", userStatus=" + userStatus +
                '}';
    }
}
