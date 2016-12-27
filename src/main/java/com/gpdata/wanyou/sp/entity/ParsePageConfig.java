package com.gpdata.wanyou.sp.entity;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chengchao on 16-10-14.
 */
public class ParsePageConfig implements Serializable {

    private String id = "";
    private String spiderid = "";
    private String caption = "";
    private String designurl = "";
    private String parseurl = "";
    private String tableid = "";


    public List<String> keyList() {

        return Arrays.asList("id", "caption", "designurl", "parseurl", "tableid");

    }

    public String getText(String key) {

        String result;
        switch (key) {
            case "id":
                result = getId();
                break;
            case "caption":
                result = getCaption();
                break;
            case "designurl":
                result = getDesignurl();
                break;
            case "parseurl":
                result = getParseurl();
                break;
            case "tableid":
                result = getTableid();
                break;
            default:
                result = "";
        }
        return result;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpiderid() {
        return spiderid;
    }

    public void setSpiderid(String spiderid) {
        this.spiderid = spiderid;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDesignurl() {
        return designurl;
    }

    public void setDesignurl(String designurl) {
        this.designurl = designurl;
    }

    public String getParseurl() {
        return parseurl;
    }

    public void setParseurl(String parseurl) {
        this.parseurl = parseurl;
    }

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
