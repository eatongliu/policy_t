package com.gpdata.wanyou.sp.entity;

/**
 * IP策略
 * Created by guoxy on 2016/10/14.
 */
public class IPStrategy {
    /**
     * 代理服务器地址
     */
    private String ipHost;
    /**
     * 代理服务器端口号
     */
    private String ipPort;
    /**
     * 代理用户名
     */
    private String ipUsername;
    /**
     * 代理密码
     */
    private String ipPassword;
    /**
     * 代理服务器地址描述
     */
    private String ipHostDesc;
    /**
     * 代理服务器端口号描述
     */
    private String ipPortDesc;
    /**
     * 代理用户名描述
     */
    private String ipUsernameDesc;
    /**
     * 代理密码描述
     */
    private String ipPasswordDesc;

    public String getIpHost() {
        return ipHost;
    }

    public void setIpHost(String ipHost) {
        this.ipHost = ipHost;
    }

    public String getIpPort() {
        return ipPort;
    }

    public void setIpPort(String ipPort) {
        this.ipPort = ipPort;
    }

    public String getIpUsername() {
        return ipUsername;
    }

    public void setIpUsername(String ipUsername) {
        this.ipUsername = ipUsername;
    }

    public String getIpPassword() {
        return ipPassword;
    }

    public void setIpPassword(String ipPassword) {
        this.ipPassword = ipPassword;
    }

    public String getIpHostDesc() {
        return ipHostDesc;
    }

    public void setIpHostDesc(String ipHostDesc) {
        this.ipHostDesc = ipHostDesc;
    }

    public String getIpPortDesc() {
        return ipPortDesc;
    }

    public void setIpPortDesc(String ipPortDesc) {
        this.ipPortDesc = ipPortDesc;
    }

    public String getIpUsernameDesc() {
        return ipUsernameDesc;
    }

    public void setIpUsernameDesc(String ipUsernameDesc) {
        this.ipUsernameDesc = ipUsernameDesc;
    }

    public String getIpPasswordDesc() {
        return ipPasswordDesc;
    }

    public void setIpPasswordDesc(String ipPasswordDesc) {
        this.ipPasswordDesc = ipPasswordDesc;
    }
}
