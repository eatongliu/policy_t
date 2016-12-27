package com.gpdata.wanyou.ds.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class NewsDto implements Serializable {


    private String tableid;
    private List<Map<String, Object>> row;

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    public List<Map<String, Object>> getRow() {
        return row;
    }

    public void setRow(List<Map<String, Object>> row) {
        this.row = row;
    }


}
