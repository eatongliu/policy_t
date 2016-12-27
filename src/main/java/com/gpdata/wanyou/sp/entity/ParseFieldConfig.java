package com.gpdata.wanyou.sp.entity;

import java.io.Serializable;

/**
 * Created by chengchao on 2016/10/18.
 */
public class ParseFieldConfig implements Serializable {

    private static final String EMPTY_STRING = "";
    /**
     * 爬虫id（必填）
     */
    private String spiderid = EMPTY_STRING;

    /**
     * id 页面解析配置id（必填）
     */
    private String id = EMPTY_STRING;

    private String caption = EMPTY_STRING;
    private String name = EMPTY_STRING;
    private String xpath = EMPTY_STRING;
    private String type = EMPTY_STRING;
    private String remark = EMPTY_STRING;


    public String getSpiderid() {
        return spiderid;
    }

    public void setSpiderid(String spiderid) {
        this.spiderid = spiderid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
