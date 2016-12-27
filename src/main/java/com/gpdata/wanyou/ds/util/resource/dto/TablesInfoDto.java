package com.gpdata.wanyou.ds.util.resource.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 表信息dto
 *
 * @author yaz
 * @create 2016-11-08 14:24
 */

public class TablesInfoDto {
    /**
     * 表名
     */
    private String tableName;

    /**
     * 表大小
     */
    private Long tableSize;

    public TablesInfoDto() {
    }

    public TablesInfoDto(String tableName, Long tableSize) {
        this.tableName = tableName;
        this.tableSize = tableSize;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getTableSize() {
        return tableSize;
    }

    public void setTableSize(Long tableSize) {
        this.tableSize = tableSize;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this)
                .toString();
    }
}
