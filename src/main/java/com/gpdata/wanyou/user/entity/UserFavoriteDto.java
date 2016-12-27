package com.gpdata.wanyou.user.entity;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by acer_liuyutong on 2016/12/17.
 */
public class UserFavoriteDto {
//    a.favorId,a.createDate,b.index,b.pdName,b.topicClassify
    /**
     * 收藏Id
     */
    private Integer favorId;
    /**
     * 文章Id
     */
    private  BigInteger pdId;
    /**
     * 收藏时间
     */
    private Date createDate;
    /**
     * 文章类型/ES索引
     */
    private String index_;
    /**
     * 文章标题
     */
    private String pdName;
    /**
     * 主题
     */
    private String topicClassify;

    public Integer getFavorId() {
        return favorId;
    }

    public void setFavorId(Integer favorId) {
        this.favorId = favorId;
    }

    public BigInteger getPdId() {
        return pdId;
    }

    public void setPdId(BigInteger pdId) {
        this.pdId = pdId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getIndex_() {
        return index_;
    }

    public void setIndex_(String index_) {
        this.index_ = index_;
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

    @Override
    public String toString() {
        return "UserFavoriteDto{" +
                "favorId=" + favorId +
                ", pdId=" + pdId +
                ", createDate=" + createDate +
                ", index_='" + index_ + '\'' +
                ", pdName='" + pdName + '\'' +
                ", topicClassify='" + topicClassify + '\'' +
                '}';
    }
}
