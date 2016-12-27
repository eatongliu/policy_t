package com.gpdata.wanyou.sp.entity;


import com.gpdata.wanyou.utils.PinyinUtil;

public class Field {
    public String caption;
    public String name;
    public String xpath;

    public Field(String caption, String name, String xpath) {
        this.caption = caption;
        this.name = PinyinUtil.getPin(caption);
        this.xpath = xpath;
    }

    public Field() {
    }

    public String getcaption() {
        return caption;
    }

    public void setcaption(String caption) {
        this.caption = caption;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getxpath() {
        return xpath;
    }

    public void setxpath(String xpath) {
        this.xpath = xpath;
    }

    @Override
    public String toString() {
        return "Field{" +
                "caption='" + caption + '\'' +
                ", name='" + name + '\'' +
                ", xpath='" + xpath + '\'' +
                '}';
    }
}