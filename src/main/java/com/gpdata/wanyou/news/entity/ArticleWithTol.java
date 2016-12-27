package com.gpdata.wanyou.news.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "aritcle_with_tol")
public class ArticleWithTol implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long articleId;

    private Long tolId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getTolId() {
        return tolId;
    }

    public void setTolId(Long tolId) {
        this.tolId = tolId;
    }


}
