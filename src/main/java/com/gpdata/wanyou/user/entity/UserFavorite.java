package com.gpdata.wanyou.user.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户收藏信息
 *
 * @author guoxiaoyang
 * @date 2016年5月3日 下午3:15:45
 */
@Entity
@Table(name = "user_favorite")
public class UserFavorite {
    /**
     * favorId
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer favorId;

    /**
     * 收藏时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createDate;

    /**
     * 文章id
     */
    private Long pdId;

    /**
     * 关联用户id
     */
    private Long userId;

    public Integer getFavorId() {
        return favorId;
    }

    public void setFavorId(Integer favorId) {
        this.favorId = favorId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getPdId() {
        return pdId;
    }

    public void setPdId(Long pdId) {
        this.pdId = pdId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}