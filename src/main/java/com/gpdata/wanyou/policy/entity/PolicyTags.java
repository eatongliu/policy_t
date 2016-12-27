package com.gpdata.wanyou.policy.entity;

import javax.persistence.*;

/**
 * Created by ligang on 2016/12/15.
 */
@Entity
@Table(name = "policy_tags")
@IdClass(value = PdIdTagPk.class)
public class PolicyTags {

    /**
     * 文件编号
     */
    @Id
    @Column(name = "pdId", nullable = false)
    private Long pdId;

    /**
     * 文件标签
     */
    @Id
    @Column(name = "tag", nullable = false)
    private String tag;
    /**
     * 标签权重
     */
    private Integer weight;

    public Long getPdId() {
        return pdId;
    }

    public void setPdId(Long pdId) {
        this.pdId = pdId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

}
