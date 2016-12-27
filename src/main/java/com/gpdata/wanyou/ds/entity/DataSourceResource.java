package com.gpdata.wanyou.ds.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "datasource_resource")
public class DataSourceResource implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer resourceId;

    /**
     * 数据资源中文名
     */
    private String caption;

    /**
     * 数据源位置
     * 内部数据源：inner
     * 外部数据源：outer
     */
    private String position;

    /**
     * 数据源类型
     * 数据库型:DBMS
     * HDFS型：HDFS
     */
    private String resourceType;

    /**
     * 数据类型
     * 例如：MySQL、Oracle等
     */
    private String dataType;

    /**
     * 数据量
     * 单位：G
     */
    private Long size;

    /**
     * 用户表主键
     * 暂时采用默认user
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 最后一次修改时间
     */
    private Date reviseDate;

    /**
     * 数据源所在主机
     */
    private String host;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String passWord;

    /**
     * 字符集
     * 数据源类型= DBMS 时有效
     */
    private String encode;

    /**
     * 数据库名
     * 数据源类型= DBMS 时有效
     */
    private String dbName;

    /**
     * 表个数
     * 数据源类型= DBMS 时有效
     */
    private Integer tablesCount;

    /**
     * 连接字符串
     * 数据源类型= DBMS 时有效
     */
    private String connString;

    /**
     * HDFS路径
     * 数据源类型= HDFS时有效
     */
    private String hdfsPath;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getConnString() {
        return connString;
    }

    public void setConnString(String connString) {
        this.connString = connString;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public String getHdfsPath() {
        return hdfsPath;
    }

    public void setHdfsPath(String hdfsPath) {
        this.hdfsPath = hdfsPath;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Date getReviseDate() {
        return reviseDate;
    }

    public void setReviseDate(Date reviseDate) {
        this.reviseDate = reviseDate;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTablesCount() {
        return tablesCount;
    }

    public void setTablesCount(Integer tablesCount) {
        this.tablesCount = tablesCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this)
                .toString();
    }
}
