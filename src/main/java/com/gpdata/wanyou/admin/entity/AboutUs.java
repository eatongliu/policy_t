package com.gpdata.wanyou.admin.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ligang on 2016/12/12.
 */

@Entity
@Table(name = "about_us")
public class AboutUs {

    /**
     * 排序
     */
    @Id
/*    @GeneratedValue(strategy = GenerationType.AUTO)*/
    private Integer sortId;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    @Column(columnDefinition = "text")
    private String content;
    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 创建人Id
     */
    private String creatorId;
    /**
     * 修改日期
     */
    private Date reviseDate;

    public Integer getSortId() {
        return sortId;
    }

    public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Date getReviseDate() {
        return reviseDate;
    }

    public void setReviseDate(Date reviseDate) {
        this.reviseDate = reviseDate;
    }

    @Override
    public String toString() {
        return "AboutUs{" +
                "sortId=" + sortId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createDate=" + createDate +
                ", creatorId='" + creatorId + '\'' +
                ", reviseDate=" + reviseDate +
                '}';
    }
}
