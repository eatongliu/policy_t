package com.gpdata.wanyou.user.entity;

import javax.persistence.*;

/**
 * Created by acer_liuyutong on 2016/12/14.
 */
@Entity
@Table(name = "user_keyword")
public class UserKeyword {
    /**
     * 主键Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer wordId;
    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 关键词
     */
    private String keyWord;
    /**
     * 访问热度
     */
    private int heat = 1;

    public UserKeyword() {}

    public UserKeyword(Long userId, String keyWord) {
        this.userId = userId;
        this.keyWord = keyWord;
    }

    public Integer getWordId() {
        return wordId;
    }

    public void setWordId(Integer wordId) {
        this.wordId = wordId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    @Override
    public String toString() {
        return "UserKeyword{" +
                "wordId=" + wordId +
                ", userId=" + userId +
                ", keyWord='" + keyWord + '\'' +
                ", heat=" + heat +
                '}';
    }
}
