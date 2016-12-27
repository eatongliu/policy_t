package com.gpdata.wanyou.ansj.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 自定义词典
 *
 * @author yaz
 * @create 2016-12-12 11:53
 */
@Entity
@Table(name = "define_library")
public class DefineLibrary implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * 内容
     */
    private String word;

    /**
     * 创建时间
     */
    private Date createDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
