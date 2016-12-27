package com.gpdata.wanyou.news.entity;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "article_tol")
public class ArticleTol implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String tol;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTol() {
        return tol;
    }

    public void setTol(String tol) {
        this.tol = tol;
    }
    
    
}
