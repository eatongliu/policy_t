package com.gpdata.wanyou.head.entity;

import javax.persistence.*;

/**
 * 首页 各地区政策数量图
 * 假数据表，后期可删
 *
 * @author yaz
 * @create 2016-12-16 10:54
 */
@Entity
@Table(name = "picture1_map")
public class Picture1Map {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    /**
     * 省名
     */
    private String province;
    /**
     * 数量
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
