package com.gpdata.wanyou.tk.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 任务模板 task_template
 * Created by guoxy on 2016/10/25.
 */
@Entity
@Table(name = "task_template")
public class TaskTemplate {
    /**
     * 模板id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer templateId;
    /**
     * 标题
     */
    private String caption;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createDatetime;
    /**
     * 修改时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date reviseDatetime;
    /**
     * 模板类型
     */
    private Integer tempType;
    /***
     * 上传类型模板zip路径
     */
    private String tempPath;
    /**
     * 说明
     */
    private String remark;

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getReviseDatetime() {
        return reviseDatetime;
    }

    public void setReviseDatetime(Date reviseDatetime) {
        this.reviseDatetime = reviseDatetime;
    }

    public Integer getTempType() {
        return tempType;
    }

    public void setTempType(Integer tempType) {
        this.tempType = tempType;
    }

    public String getTempPath() {
        return tempPath;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
