package com.gpdata.wanyou.tk.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by guoxy on2016/11/10.
 */
@Entity
@Table(name = "task_kettle")
public class TaskKettle {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer kettleId;
    /**
     * kettle文件名
     */
    private String kettleName;
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 用户
     */
    private String creator;

    /**
     * 创建时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createDate;
    /**
     * 修改时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date reviseDate;
    /**
     * 类型
     */
    private int ktlType;

    /**
     * @return
     */
    private String remark;

    public Integer getKettleId() {
        return kettleId;
    }

    public void setKettleId(Integer kettleId) {
        this.kettleId = kettleId;
    }

    public String getKettleName() {
        return kettleName;
    }

    public void setKettleName(String kettleName) {
        this.kettleName = kettleName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getReviseDate() {
        return reviseDate;
    }

    public void setReviseDate(Date reviseDate) {
        this.reviseDate = reviseDate;
    }

    public int getKtlType() {
        return ktlType;
    }

    public void setKtlType(int ktlType) {
        this.ktlType = ktlType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "TaskKettle{" +
                "kettleId=" + kettleId +
                ", kettleName='" + kettleName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", creator='" + creator + '\'' +
                ", createDate=" + createDate +
                ", reviseDate=" + reviseDate +
                ", ktlType=" + ktlType +
                ", remark='" + remark + '\'' +
                '}';
    }
}
