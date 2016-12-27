package com.gpdata.wanyou.news.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 新闻
 * 
 * @author chengchao
 *
 */
@Entity
@Table(name = "article")
public class Article implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /*
     * occurrence time
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date ot;
    
    /*
     * 发布时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date pt;

    /*
     * 分类
     */
    private Long categoryId;
    
    /*
     * 点击量
     */
    private Integer clickCount = Integer.valueOf(1);
    
    /*
     * 显示状态: {1: 显示, 0: 不显示}
     */
    private Integer showStatus = Integer.valueOf(1);
    
    /*
     * 标题
     */
    private String title;
    
    /*
     * 摘要
     */
    private String summary;
    
    /**
     * <p>tag or label</p>
     * 标签(逗号分隔的词组)
     * 同时还有多对多关联表
     */
    private String tol;
    
    /*
     * 内容
     */
    private String content;
    
    /*
     * 文章分类
     */
    private String categoryName;
   
    /*
     * 作者
     */
    
    /*
     * 封面图片
     */
    private String coverImage;
    
    /*
     * 作者
     */
    private String author;
    
    /*
     * 来源
     */
    private String upstream;
    
    public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getOt() {
        return ot;
    }

    public void setOt(Date ot) {
        this.ot = ot;
    }

    public Date getPt() {
        return pt;
    }

    public void setPt(Date pt) {
        this.pt = pt;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    public Integer getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(Integer showStatus) {
        this.showStatus = showStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTol() {
        return tol;
    }

    public void setTol(String tol) {
        this.tol = tol;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUpstream() {
        return upstream;
    }

    public void setUpstream(String upstream) {
        this.upstream = upstream;
    }
    
    
    
    

}
