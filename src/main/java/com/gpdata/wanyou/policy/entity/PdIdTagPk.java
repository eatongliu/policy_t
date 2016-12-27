package com.gpdata.wanyou.policy.entity;

import java.io.Serializable;

/**
 * Created by ligang on 2016/12/15.
 */
public class PdIdTagPk implements Serializable {

    /**
     * 文件编号
     */
    private Long pdId;
    /**
     * 文件标签
     */
    private String tag;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj) {
            return false;
        }

        PdIdTagPk pdIdTagPk = (PdIdTagPk) obj;
        if (this.getPdId() == pdIdTagPk.getPdId() &&
                this.getTag() == pdIdTagPk.getTag()) {
            return true;
        }
        return false;

    }
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
