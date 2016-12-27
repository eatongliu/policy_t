package com.gpdata.wanyou.ds.util.resource.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 字段信息dto
 *
 * @author yaz
 * @create 2016-11-08 14:24
 */

public class FieldInfoDto {
    /**
     * 字段名
     * 即：英文标识
     */
    private String name;

    /**
     * 字段备注
     * 即：中文名
     */
    private String comment;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 字符串数据长度
     * 如果为数字或时间日期等，则此项为空
     */
    private Long length;

    /**
     * 0：不允许为空；1：允许为空
     */
    private Integer isNull;

    /**
     * 数字精度
     * 如果为varchar等，则此项为空
     */
    private Integer precision;

    public FieldInfoDto() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getIsNull() {
        return isNull;
    }

    public void setIsNull(Integer isNull) {
        this.isNull = isNull;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this)
                .toString();
    }
}
