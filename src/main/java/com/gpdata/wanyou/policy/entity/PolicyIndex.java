package com.gpdata.wanyou.policy.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by ligang on 2016/12/16.
 */
@Entity
@Table(name = "policy_index")
public class PolicyIndex {
    @Id
    /**
     * 文件编号
     */
    private Long pdId;
    /**
     * 文件索引 / 文件类型
     * 1 zcfg 1 政策法规
     * 2 zcwj 2 政策文件
     * 3 zcjd 3 政策解读
     * 4 jyta 4 建议提案
     */
    private String index_;
    /**
     * 成文日期
     */
    private String createDate;
    /**
     * 文件名称
     */
    private String pdName;
    /**
     * 主题分类
     */
    private String topicClassify;

    public Long getPdId() {
        return pdId;
    }

    public void setPdId(Long pdId) {
        this.pdId = pdId;
    }

    public String getIndex_() {
        return index_;
    }

    public void setIndex_(String index_) {
        this.index_ = index_;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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
        return "PolicyIndex{" +
                "pdId=" + pdId +
                ", index='" + index_ + '\'' +
                ", createDate='" + createDate + '\'' +
                ", pdName='" + pdName + '\'' +
                ", topicClassify='" + topicClassify + '\'' +
                '}';
    }
}
