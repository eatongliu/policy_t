package com.gpdata.wanyou.base.flexible;

public enum FlexibleFileType {

    /**
     * 公共文件, 可以直接访问的
     */
    PUB("PUB"),

    /**
     * 私有文件, 不可直接访问的
     */
    RES("RES");

    private String type;

    private FlexibleFileType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }

}
