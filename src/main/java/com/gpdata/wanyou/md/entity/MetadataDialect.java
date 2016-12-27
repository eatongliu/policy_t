package com.gpdata.wanyou.md.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "metadata_dialect")
public class MetadataDialect implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer dialectid;

    private String caption;
    private Date createdate;
    private String creator;
    private Date revisedate;
    private String remark;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Integer getDialectid() {
        return dialectid;
    }

    public void setDialectid(Integer dialectid) {
        this.dialectid = dialectid;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getRevisedate() {
        return revisedate;
    }

    public void setRevisedate(Date revisedate) {
        this.revisedate = revisedate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "MetadataDialect [dialectid=" + dialectid + ", caption="
                + caption + ", createdate=" + createdate + ", creator="
                + creator + ", revisedate=" + revisedate + ", remark=" + remark
                + "]";
    }

}