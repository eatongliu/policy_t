package com.gpdata.wanyou.ds.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "datasource_field")
public class DataSourceField implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字段id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer fieldId;
    /**
     * 表id
     */
    private Integer tableId;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 标题
     */
    private String caption;
    /**
     * 类型
     */
    private String type;
    /**
     * 长度
     */
    private Long length;
    /**
     * 数据单位
     */
    private String unit;
    /**
     * 是否允许空
     */
    private Integer allowNull;
    /**
     * 数据精度
     */
    private Integer decimalCount;
    /**
     * 状态
     */
    private String status;
    /**
     * 备注
     */
    private String remark;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getAllowNull() {
        return allowNull;
    }

    public void setAllowNull(Integer allowNull) {
        this.allowNull = allowNull;
    }

    public Integer getDecimalCount() {
        return decimalCount;
    }

    public void setDecimalCount(Integer decimalCount) {
        this.decimalCount = decimalCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DataSourceField [fieldId=" + fieldId + ", tableId=" + tableId
                + ", fieldName=" + fieldName + ", caption=" + caption
                + ", type=" + type + ", length=" + length + ", unit=" + unit
                + ", allowNull=" + allowNull + ", decimalCount=" + decimalCount
                + ", remark=" + remark + "]";
    }

    /**
     * 默认字段标志一致，认为是同一个字段
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
            DataSourceField ano = (DataSourceField) anObject;
            return fieldName.equals(ano.getFieldName());
        }
        return false;
    }

}
