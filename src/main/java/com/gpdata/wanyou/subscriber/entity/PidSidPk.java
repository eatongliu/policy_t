package com.gpdata.wanyou.subscriber.entity;

import java.io.Serializable;

/**
 * 联合主键实体
 *
 * @author yaz
 * @create 2016-12-14 11:52
 */

public class PidSidPk implements Serializable {
    private Long pid;
    private Long sid;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj) {
            return false;
        }
        final PidSidPk otherId = (PidSidPk) obj;
        if (this.getPid() == otherId.getPid() &&
                this.getSid() == otherId.getSid()) {
            return true;
        }
        return false;
    }
}
