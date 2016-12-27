package com.gpdata.wanyou.news.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 新闻分类
 * 
 * @author chengchao
 *
 */
@XmlRootElement
@Entity
@Table(name = "article_category")
public class ArticleCategory implements Serializable {
    
    /**
     * 文章分类之新闻
     */
    public static final String ARTICLE_CATEGORY_TYPE_NEWS = "NEWS";
    
    /**
     * 文章分类之帮助
     */
    public static final String ARTICLE_CATEGORY_TYPE_HELPER = "HELPER";
    
    /**
     * 
     * 文章分类之数据专家
     */
    public static final String ARTICLE_CATEGORY_TYPE_EXPERT = "EXPERT";

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Long parentId = Long.valueOf(0L);
    
    private String categoryType;
    
    private String categoryName;
    
    private Integer articleCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
    }

}
