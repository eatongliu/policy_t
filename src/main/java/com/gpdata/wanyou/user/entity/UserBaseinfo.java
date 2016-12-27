//package com.gpdata.wanyou.user.entity;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.util.Date;
//
///**
// * 用户信息详情
// *
// * @author guoxiaoyang
// * @date 2016年5月4日 下午6:30:04
// */
//@Entity
//@Table(name = "user_baseinfo")
//public class UserBaseinfo implements Serializable {
//
//    /**
//     *
//     */
//    private static final long serialVersionUID = 1L;
//
//    /**
//     * 用户账户的余额初始状态是没有钱的. <br />
//     * BigDecimal == 0
//     */
//    public static final BigDecimal NO_MONEY = new BigDecimal(0);
//
//    /**
//     * 用户id
//     */
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long userid;
//
//    /**
//     * 乐观锁
//     */
//    private Integer optiVersion;
//
//    /**
//     * 注册时间
//     */
//    @Temporal(value = TemporalType.TIMESTAMP)
//    private Date createdate;
//    /**
//     * 上次登录时间
//     */
//    @Temporal(value = TemporalType.TIMESTAMP)
//    private Date lasttime;
//
//    /**
//     * 登录名
//     */
//    private String loginname;
//
//    /**
//     * 密码
//     */
//    private String password;
//    /**
//     * 用户昵称
//     */
//
//    private String username;
//    /**
//     * 用户实名
//     */
//    private String realname;
//
//    /**
//     * 性别
//     */
//    private Integer sex;
//
//    /**
//     * 生日
//     */
//    @Temporal(value = TemporalType.TIMESTAMP)
//    private Date birthday;
//
//    /**
//     * 行业id
//     */
//    private Integer categoryparentid;
//
//    /**
//     * 子行业id
//     */
//    private Integer categoryid;
//
//    /**
//     * 头像
//     */
//    private String headpic;
//    /**
//     * 手机号
//     */
//    private String phone;
//    /**
//     * 固定电话
//     */
//    private String tel;
//
//    /**
//     * 绑定邮箱
//     */
//    private String email;
//
//    /**
//     * QQ
//     */
//    private String qq;
//
//    /**
//     * 微信
//     */
//    private String weixin;
//
//    /**
//     * 省
//     */
//    private Integer province;
//
//    /**
//     * 市
//     */
//    private Integer city;
//
//    /**
//     * 支付密码
//     */
//    private String paypwd;
//
//    /**
//     * 认证状态
//     */
//    private Integer certifystatus;
//
//    /**
//     * 认证类型 0:未认证；1：个人认证；2：服务商认证
//     */
//    private Integer certifytype;
//
//    /**
//     * 专家用户
//     */
//    private Integer expertuser;
//
//    /**
//     * 账户金额
//     */
//    private BigDecimal money = NO_MONEY;
//
//    /**
//     * 用户状态（0,1,2）
//     */
//    private Integer userstatus;
//
//    public Long getUserid() {
//        return userid;
//    }
//
//    public void setUserid(Long userid) {
//        this.userid = userid;
//    }
//
//    /**
//     * 乐观锁
//     *
//     * @return
//     */
//    public Integer getOptiVersion() {
//        return optiVersion;
//    }
//
//    /**
//     * 乐观锁
//     *
//     * @param optiVersion
//     */
//    public void setOptiVersion(Integer optiVersion) {
//        this.optiVersion = optiVersion;
//    }
//
//    public Date getCreatedate() {
//        return createdate;
//    }
//
//    public void setCreatedate(Date createdate) {
//        this.createdate = createdate;
//    }
//
//    public Date getLasttime() {
//        return lasttime;
//    }
//
//    public void setLasttime(Date lasttime) {
//        this.lasttime = lasttime;
//    }
//
//    public String getLoginname() {
//        return loginname;
//    }
//
//    public void setLoginname(String loginname) {
//        this.loginname = loginname;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getRealname() {
//        return realname;
//    }
//
//    public void setRealname(String realname) {
//        this.realname = realname;
//    }
//
//    public Integer getSex() {
//        return sex;
//    }
//
//    public void setSex(Integer sex) {
//        this.sex = sex;
//    }
//
//    public Date getBirthday() {
//        return birthday;
//    }
//
//    public void setBirthday(Date birthday) {
//        this.birthday = birthday;
//    }
//
//    public Integer getCategoryparentid() {
//        return categoryparentid;
//    }
//
//    public void setCategoryparentid(Integer categoryparentid) {
//        this.categoryparentid = categoryparentid;
//    }
//
//    public Integer getCategoryid() {
//        return categoryid;
//    }
//
//    public void setCategoryid(Integer categoryid) {
//        this.categoryid = categoryid;
//    }
//
//    public String getHeadpic() {
//        return headpic;
//    }
//
//    public void setHeadpic(String headpic) {
//        this.headpic = headpic;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getTel() {
//        return tel;
//    }
//
//    public void setTel(String tel) {
//        this.tel = tel;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getQq() {
//        return qq;
//    }
//
//    public void setQq(String qq) {
//        this.qq = qq;
//    }
//
//    public String getWeixin() {
//        return weixin;
//    }
//
//    public void setWeixin(String weixin) {
//        this.weixin = weixin;
//    }
//
//    public Integer getProvince() {
//        return province;
//    }
//
//    public void setProvince(Integer province) {
//        this.province = province;
//    }
//
//    public Integer getCity() {
//        return city;
//    }
//
//    public void setCity(Integer city) {
//        this.city = city;
//    }
//
//    public String getPaypwd() {
//        return paypwd;
//    }
//
//    public void setPaypwd(String paypwd) {
//        this.paypwd = paypwd;
//    }
//
//    public Integer getCertifystatus() {
//        return certifystatus;
//    }
//
//    public void setCertifystatus(Integer certifystatus) {
//        this.certifystatus = certifystatus;
//    }
//
//    public Integer getCertifytype() {
//        return certifytype;
//    }
//
//    public void setCertifytype(Integer certifytype) {
//        this.certifytype = certifytype;
//    }
//
//    public Integer getExpertuser() {
//        return expertuser;
//    }
//
//    public void setExpertuser(Integer expertuser) {
//        this.expertuser = expertuser;
//    }
//
//    public BigDecimal getMoney() {
//        return money;
//    }
//
//    public void setMoney(BigDecimal money) {
//        this.money = money;
//    }
//
//    public Integer getUserstatus() {
//        return userstatus;
//    }
//
//    public void setUserstatus(Integer userstatus) {
//        this.userstatus = userstatus;
//    }
//}