package com.gpdata.wanyou.policy.entity;

/**
 * Created by guoxy on 2016/12/16.
 * 政策通爬虫文件存储格式：
 * ES：
 * ES：
 * {
 * "pdId":"",          //文件编号
 * "pdName":"",       //文件名称   "模糊"
 * "pdType":"",       //文件类型 1政策法规 zcfg 2 政策文件zcwj  3政策解读zcjd  4 建议提案jyta
 * "topicClassify":"",  //主题分类   "模糊"
 * "pubOrg":"",      //颁布机构   "模糊"
 * "placed":"",       //具体单位   "模糊"
 * "createDate":"",    //成文日期   区间查询  startTime   endTime
 * "issuedNum":"",    //发文字号   "模糊"
 * "link":"",          //原文链接
 * "linkAddress":"",   //原文链接地址
 * "isEffect":"",      //是否有效 0 否 1 是    "精确"
 * "isPilot":"",      //是否试点 0 否 1 是     "精确"
 * "isPub":"",        //是否发布  0 否 1 是   默认 0    "精确"
 * "isHide":"",        //是否隐藏  0 否 1 是   默认 1    "精确"
 * "content":""     //文件内容   "模糊"
 * "province":""     //省      "模糊"
 * "city":""     //市            "模糊"
 * "county":""     //县       "模糊"
 * "Order":""     //排序     "n"  按时间 createDate    "z" 默认
 * "keyWords":""     //查询关键字    "模糊"
 * "startTime":"" //查询起始年份
 * "endTime":"" //查询结束年份
 * }
 * 以下是搜索条件
 */
public class PolicyDocument {
    /**
     * 文件编号
     */
    private Long pdId;
    /**
     * 文件名称
     */
    private String pdName;
    /**
     * 文件类型索引
     * 1 zcfg 1 政策法规
     * 2 zcwj 2 政策文件
     * 3 zcjd 3政策解读
     * 4 jyta 4 建议提案
     */
    private String index;
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
     * 排序
     * N  时间 desc
     * Z  综合
     */
    private String order;
    /**
     * 关键词组,空格分隔字符串，订阅用词
     * 空格分隔字符串，订阅用词  关键词组 keywords
     * 把id和词组 丢给ES  ES返回文章全部或者订阅id 政策id、政策标题、政策类型
     */
    private String keyWords;
    /**
     * 查询起始年份
     */
    private String startTime;
    /**
     * 查询结束年份
     */
    private String endTime;


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

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getCreateYear() {
        return createYear;
    }

    public void setCreateYear(String createYear) {
        this.createYear = createYear;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "PolicyDocument{" +
                "pdId=" + pdId +
                ", pdName='" + pdName + '\'' +
                ", index='" + index + '\'' +
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
                ", order='" + order + '\'' +
                ", keyWords='" + keyWords + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
