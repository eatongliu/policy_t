package com.gpdata.wanyou.system.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 记录用户操作信息的实体类
 * Created by chengchao on 2016/10/26.
 */
public class OperateTrace implements Serializable {

    /**
     * 时间+登录名 作为rowkey
     */
    private String rowKey;

    /**
     * 创建时间
     */
    private long createTime;

    /**
     * 登录名
     */
    private String n;

    /**
     * 请求者IP地址
     */
    private String ip;

    /**
     * 发出请求时的完整URL
     */

    private String ul;

    /**
     * 请求行中的资源名部分
     */
    private String ui;

    /**
     * 请求行中的参数部分
     */
    private String qs;

    /**
     * 客户机的完整主机名
     */
    private String hn;

    /**
     * 网络端口号
     */
    private Integer pt;

    /**
     * 客户机请求方式
     */
    private String m;

    /**
     * 调用服务路径
     * 完整服务路径
     */
    private String sp;

    /**
     * 服务频道
     */
    private String ch;

    /**
     * 处理请求所用时间
     */
    private long dt;

    /**
     * 请求参数
     * 完整请求参数
     */
    private String pa;

    /**
     * 返回值
     * 完整返回值
     */
    private String re;


    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeVal() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(new Date(this.createTime));
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUl() {
        return ul;
    }

    public void setUl(String ul) {
        this.ul = ul;
    }

    public String getUi() {
        return ui;
    }

    public void setUi(String ui) {
        this.ui = ui;
    }

    public String getQs() {
        return qs;
    }

    public void setQs(String qs) {
        this.qs = qs;
    }

    public String getHn() {
        return hn;
    }

    public void setHn(String hn) {
        this.hn = hn;
    }

    public Integer getPt() {
        return pt;
    }

    public void setPt(Integer pt) {
        this.pt = pt;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getSp() {
        return sp;
    }

    public void setSp(String sp) {
        this.sp = sp;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public String getPa() {
        return pa;
    }

    public void setPa(String pa) {
        this.pa = pa;
    }

    public String getRe() {
        return re;
    }

    public void setRe(String re) {
        this.re = re;
    }

    @Override
    public String toString() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSSSS");
        String result =  "OperateTrace "+
                "\n{ rowKey = '" + rowKey + '\'' +
                "\n, createTime = " + sdf.format(new Date(createTime)) +
                "\n, 登录名 n = '" + n + '\'' +
                "\n, 请求者IP地址 ip = '" + ip + '\'' +
                "\n, URL ul = '" + ul + '\'' +
                "\n, 资源名称 ui = '" + ui + '\'' +
                "\n, 查询参数 qs = '" + qs + '\'' +
                "\n, 客户端主机 hn = '" + hn + '\'' +
                "\n, 客户端口 pt = " + pt +
                "\n, HTTP 方法 m = '" + m + '\'' +
                "\n, 服务路径 sp = '" + sp + '\'' +
                "\n, 服务路径 ch = '" + ch + '\'';

        double d = dt / 1000000000.0;
        if (d > 1) {
            result += "\n, 方法执行时间 dt = " + d + " 秒";
        } else {
            d = dt / 1000000.0;
            if (d > 1) {
                result += "\n, 方法执行时间 dt = " + d + " 毫秒";
            } else {
                result += "\n, 方法执行时间 dt = " + dt + " 纳秒";
            }
        }


        result +=
                "\n, BODY 数据 pa = '" + pa + '\'' +
                "\n, 返回数据 re = '" + re + '\'' +
                '\n' +
                '}';

        return result;

    }
}
