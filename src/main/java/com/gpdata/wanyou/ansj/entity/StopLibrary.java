package com.gpdata.wanyou.ansj.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 停用词词典
 *
 * @author yaz
 * @create 2016-12-12 12:00
 */
@Entity
@Table(name = "stop_library")
public class StopLibrary {

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
