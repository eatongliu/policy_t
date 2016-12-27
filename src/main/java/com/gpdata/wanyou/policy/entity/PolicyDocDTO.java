package com.gpdata.wanyou.policy.entity;

/**
 * Created by ligang on 2016/12/20.
 */
public class PolicyDocDTO {

    /**
     * 文件编号
     */
    private Long pdId;
    /**
     * 文件名称
     */
    private String pdName;
    /**
     * 主题分类
     */
    private String topicClassify;
    /**
     * 颁布机构
     */
    private String pubOrg;
    /**
     * 具体单位
     */
    private String placed;
    /**
     * 成文日期
     */
    private String createDate;
    /**
     * 成文年份
     */
    private String createYear;
    /**
     * 发文字号
     */
    private String issuedNum;
    /**
     * 原文链接
     */
    private String link;
    /**
     * 原文链接地址
     */
    private String linkAddress;
    /**
     * 是否有效 0 否 1 是
     */
    private String isEffect;
    /**
     * 是否试点 0 否 1 是
     */
    private String isPilot;
    /**
     * 是否发布 0 否 1 是
     */
    private String isPub;
    /**
     * 是否隐藏 0 否 1 是
     */
    private String isHide;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 县
     */
    private String county;
    /**
     * 正文
     */
    private String content;

    public Long getPdId() {
        return pdId;
    }

    public void setPdId(Long pdId) {
        this.pdId = pdId;
    }

    public String getPdName() {
        return pdName;
    }

    public void setPdName(String pdName) {
        this.pdName = pdName;
    }

    public String getTopicClassify() {
        return topicClassify;
    }

    public void setTopicClassify(String topicClassify) {
        this.topicClassify = topicClassify;
    }

    public String getPubOrg() {
        return pubOrg;
    }

    public void setPubOrg(String pubOrg) {
        this.pubOrg = pubOrg;
    }

    public String getPlaced() {
        return placed;
    }

    public void setPlaced(String placed) {
        this.placed = placed;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateYear() {
        return createYear;
    }

    public void setCreateYear(String createYear) {
        this.createYear = createYear;
    }

    public String getIssuedNum() {
        return issuedNum;
    }

    public void setIssuedNum(String issuedNum) {
        this.issuedNum = issuedNum;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }

    public String getIsEffect() {
        return isEffect;
    }

    public void setIsEffect(String isEffect) {
        this.isEffect = isEffect;
    }

    public String getIsPilot() {
        return isPilot;
    }

    public void setIsPilot(String isPilot) {
        this.isPilot = isPilot;
    }

    public String getIsPub() {
        return isPub;
    }

    public void setIsPub(String isPub) {
        this.isPub = isPub;
    }

    public String getIsHide() {
        return isHide;
    }

    public void setIsHide(String isHide) {
        this.isHide = isHide;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PolicyDocDTO{" +
                "pdId=" + pdId +
                ", pdName='" + pdName + '\'' +
                ", topicClassify='" + topicClassify + '\'' +
                ", pubOrg='" + pubOrg + '\'' +
                ", placed='" + placed + '\'' +
                ", createDate='" + createDate + '\'' +
                ", createYear='" + createYear + '\'' +
                ", issuedNum='" + issuedNum + '\'' +
                ", link='" + link + '\'' +
                ", linkAddress='" + linkAddress + '\'' +
                ", isEffect='" + isEffect + '\'' +
                ", isPilot='" + isPilot + '\'' +
                ", isPub='" + isPub + '\'' +
                ", isHide='" + isHide + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
