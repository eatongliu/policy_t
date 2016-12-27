package com.gpdata.wanyou.ds.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "datasource_table")
public class DataSourceTable implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer tableId;
    /**
     * 数据资源id
     */
    private Integer resourceId;
    /**
     * 数据表标识
     */
    private String caption;
    /**
     * 数据表名称
     */
    private String tableName;
    /**
     * 字段总数
     */
    private Integer cloumnsCount;
    /**
     * 数据量
     */
    private Long size;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改时间
     */
    private Date reviseDate;
    /**
     * 状态
     */
    private String status;
    /**
     * 备注
     */
    private String remark;

    /**
     * 是否自动更新
     * Y：是，若为Y，则updateSql字段不可为空
     * N：否
     */
    private String isAutoUpdate;

    /**
     * 自动更新sql语句
     */
    private String updateSql;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getCloumnsCount() {
        return cloumnsCount;
    }

    public void setCloumnsCount(Integer cloumnsCount) {
        this.cloumnsCount = cloumnsCount;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getReviseDate() {
        return reviseDate;
    }

    public void setReviseDate(Date reviseDate) {
        this.reviseDate = reviseDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsAutoUpdate() {
        return isAutoUpdate;
    }

    public void setIsAutoUpdate(String isAutoUpdate) {
        this.isAutoUpdate = isAutoUpdate;
    }

    public String getUpdateSql() {
        return updateSql;
    }

    public void setUpdateSql(String updateSql) {
        this.updateSql = updateSql;
    }

    @Override
    public String toString() {
        return "DataSourceTable [tableId=" + tableId + ", resourceId="
                + resourceId + ", caption=" + caption + ", tableName="
                + tableName + ", cloumnsCount=" + cloumnsCount + ", size="
                + size + ", creator=" + creator + ", createDate=" + createDate
                + ", reviseDate=" + reviseDate + ", status=" + status
                + ", remark=" + remark + "]";
    }

    /**
     * 默认标示一致，则认为是同一个表
     */
    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (null == anObject) {
            return false;
        }
        if (this.getClass() == anObject.getClass()) {
            DataSourceTable ano = (DataSourceTable) anObject;
            return tableName.equals(ano.getTableName());
        }
        return false;
    }
}
