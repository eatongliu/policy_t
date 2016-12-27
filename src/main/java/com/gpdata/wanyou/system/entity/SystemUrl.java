package com.gpdata.wanyou.system.entity;

import javax.persistence.*;

/**
 * 系统中的 url
 * Created by chengchao on 2016/10/26.
 */
@Entity
@Table(name = "sys_url")
public class SystemUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String methodName;

    private String urlName;

    private String urlValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public String getUrlValue() {
        return urlValue;
    }

    public void setUrlValue(String urlValue) {
        this.urlValue = urlValue;
    }
}
