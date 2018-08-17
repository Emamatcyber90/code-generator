package com.zbss.code.generator.entity;

import com.alibaba.fastjson.JSON;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 12:09
 */
public class TableColumn {

    private String columnActualName;
    private String columnType;
    private String columnComment;
    private String columnFieldName;

    public String getColumnActualName() {
        return columnActualName;
    }

    public void setColumnActualName(String columnActualName) {
        this.columnActualName = columnActualName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getColumnFieldName() {
        return columnFieldName;
    }

    public void setColumnFieldName(String columnFieldName) {
        this.columnFieldName = columnFieldName;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
