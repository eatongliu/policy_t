package com.gpdata.wanyou.base.core;

import java.io.Serializable;

/**
 * 
 * @author chengchao
 *
 */
public class TicketInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1174108378227833051L;

    /*
     * 账号：用户ID
     */
    private Long account;
    
    /*
     * 创建时间
     */
    private long thatTimeMillis;
    
    /*
     * 远程主机
     */
    private String remoteHost;
    
    /*
     * 真实IP
     */
    private String xRealIp;


    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public long getThatTimeMillis() {
        return thatTimeMillis;
    }

    public void setThatTimeMillis(long thatTimeMillis) {
        this.thatTimeMillis = thatTimeMillis;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public String getxRealIp() {
        return xRealIp;
    }

    public void setxRealIp(String xRealIp) {
        this.xRealIp = xRealIp;
    }


    @Override
    public String toString() {
        
        StringBuilder buff = new StringBuilder();
        
        buff.append("{");
        buff.append("\"thatTimeMillis\":").append(thatTimeMillis);
        buff.append(",\"xRealIp\":\"").append(xRealIp);
        buff.append("\",\"remoteHost\":\"").append(remoteHost);
        buff.append("\",\"account\":").append(account);
        
        buff.append("}");
        
        return buff.toString();
    }
    
    
    
}
