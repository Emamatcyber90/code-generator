package com.zbss.code.generator.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.javaparser.ast.CompilationUnit;
import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.file.GenerateFile;
import org.dom4j.Document;

import java.util.ArrayList;
import java.util.List;

public class TableInfo {

    private String primaryKey;
    private Config config;
    private String actualName;
    private String domainName;
    private String mapperName;
    private List<TableColumn> actualColumns;
    private List<GenerateFile<?>> generateFiles;

    public String getActualName() {
        return actualName;
    }

    public void setActualName(String actualName) {
        this.actualName = actualName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getMapperName() {
        return mapperName;
    }

    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }

    public List<TableColumn> getActualColumns() {
        return actualColumns;
    }

    public void setActualColumns(List<TableColumn> actualColumns) {
        this.actualColumns = actualColumns;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<GenerateFile<?>> getGenerateFiles() {
        generateFiles = generateFiles == null ? new ArrayList<GenerateFile<?>>() : generateFiles;
        return generateFiles;
    }

    public void setGenerateFiles(List<GenerateFile<?>> generateFiles) {
        this.generateFiles = generateFiles;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
