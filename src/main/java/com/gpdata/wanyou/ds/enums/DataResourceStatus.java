package com.gpdata.wanyou.ds.enums;

/**
 * Created by yaz on 2016/10/31.
 */
public enum DataResourceStatus {
    SUCCESS(0, "正常"), //针对数据源的状态
    ERROR(1, "不可达"),//针对数据源的状态
    NORMAL(2, "正常"), //针对 表、字段第一次新增时的状态
    DELETE(3, "不可达");//针对 表、字段改变的状态

    private int status;
    private String stateInfo;

    DataResourceStatus(int status, String stateInfo) {
        this.stateInfo = stateInfo;
        this.status = status;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
