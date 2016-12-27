package com.gpdata.wanyou.dq.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用来记录哪个数据源创建了验证规则
 *
 * 当创建一条验证规则时, 会检查是否存在一条这样的记录
 * 如果不存在, 说明内部数据源还没有创建, 则隐式创建内部数据源
 * 存在则说明内部数据源已经存在
 * Created by chengchao on 2016/11/17.
 */
@Entity
@Table(name = "validate_data_source")
public class ValidateDataSource {

    /**
     * 主键;
     * 使用外部数据源的 id
     */
    @Id
    private Integer id;

    /**
     * 外部数据源的 id
     */
    private Integer foreignDbsId;

    /**
     * 外部数据源的名称
     */
    private String foreignDbsName;

    /**
     * 创建好的内部数据源的 id
     */
    private Integer innerDbsId;


    /**
     * 创建好的内部数据源的名称(存库名称, 规则是: '外部数据库的名称_外部数据源 id')
     */
    private String innerDbsName;

    /**
     * 创建这条记录的时间
     */
    private Date createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getForeignDbsId() {
        return foreignDbsId;
    }

    public void setForeignDbsId(Integer foreignDbsId) {
        this.foreignDbsId = foreignDbsId;
    }

    public String getForeignDbsName() {
        return foreignDbsName;
    }

    public void setForeignDbsName(String foreignDbsName) {
        this.foreignDbsName = foreignDbsName;
    }

    public Integer getInnerDbsId() {
        return innerDbsId;
    }

    public void setInnerDbsId(Integer innerDbsId) {
        this.innerDbsId = innerDbsId;
    }

    public String getInnerDbsName() {
        return innerDbsName;
    }

    public void setInnerDbsName(String innerDbsName) {
        this.innerDbsName = innerDbsName;
    }
}
