package com.gpdata.wanyou.head.entity;

import javax.persistence.*;

/**
 * 首页 五边形政策分类数量图
 * 假数据表，后期可删
 *
 * @author yaz
 * @create 2016-12-16 10:56
 */
@Entity
@Table(name = "picture3_pentagon")
public class Picture3Pentagon {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    /**
     * 类型（5项）
     */
    private String type;
    /**
     * 政策总数
     */
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
