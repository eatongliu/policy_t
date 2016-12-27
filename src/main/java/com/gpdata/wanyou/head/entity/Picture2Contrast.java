package com.gpdata.wanyou.head.entity;

import javax.persistence.*;

/**
 * 首页 积极消极对照图
 * 假数据表，后期可删
 *
 * @author yaz
 * @create 2016-12-16 10:55
 */
@Entity
@Table(name = "picture2_contrast")
public class Picture2Contrast {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    /**
     * 类型
     */
    private String type;
    /**
     * 正向激励数
     */
    private Integer pcount;
    /**
     * 正向激励名
     */
    private String pname;
    /**
     * 反向激励数
     */
    private Integer ncount;
    /**
     * 反向激励名
     */
    private String nname;
    /**
     * 总数
     * 正向激励数 + 反向激励数
     */
    private Integer total;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNcount() {
        return ncount;
    }

    public void setNcount(Integer ncount) {
        this.ncount = ncount;
    }

    public Integer getPcount() {
        return pcount;
    }

    public void setPcount(Integer pcount) {
        this.pcount = pcount;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNname() {
        return nname;
    }

    public void setNname(String nname) {
        this.nname = nname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
}
