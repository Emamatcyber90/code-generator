package com.zbss.code.generator.table;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.javaparser.ast.CompilationUnit;
import com.zbss.code.generator.config.Config;
import org.dom4j.Document;

import java.util.List;

public class TableInfo {

    private String primaryKey;
    private Config config;
    private String actualName;
    private String domainName;
    private String mapperName;
    private JSONObject modelConfig;
    private JSONObject mapperConfig;
    private JSONObject xmlConfig;
    private JSONObject serviceConfig;
    private JSONObject serviceImplConfig;
    private JSONObject controllerConfig;
    private List<TableColumn> actualColumns;
    private CompilationUnit modelCompilationUnit;
    private CompilationUnit mapperCompilationUnit;
    private CompilationUnit serviceCompilationUnit;
    private CompilationUnit serviceImplCompilationUnit;
    private CompilationUnit controllerCompilationUnit;
    private Document xmlDocument;

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

    public CompilationUnit getModelCompilationUnit() {
        return modelCompilationUnit;
    }

    public void setModelCompilationUnit(CompilationUnit modelCompilationUnit) {
        this.modelCompilationUnit = modelCompilationUnit;
    }

    public CompilationUnit getMapperCompilationUnit() {
        return mapperCompilationUnit;
    }

    public void setMapperCompilationUnit(CompilationUnit mapperCompilationUnit) {
        this.mapperCompilationUnit = mapperCompilationUnit;
    }

    public CompilationUnit getServiceCompilationUnit() {
        return serviceCompilationUnit;
    }

    public void setServiceCompilationUnit(CompilationUnit serviceCompilationUnit) {
        this.serviceCompilationUnit = serviceCompilationUnit;
    }

    public CompilationUnit getServiceImplCompilationUnit() {
        return serviceImplCompilationUnit;
    }

    public void setServiceImplCompilationUnit(CompilationUnit serviceImplCompilationUnit) {
        this.serviceImplCompilationUnit = serviceImplCompilationUnit;
    }

    public CompilationUnit getControllerCompilationUnit() {
        return controllerCompilationUnit;
    }

    public void setControllerCompilationUnit(CompilationUnit controllerCompilationUnit) {
        this.controllerCompilationUnit = controllerCompilationUnit;
    }

    public JSONObject getModelConfig() {
        return modelConfig;
    }

    public void setModelConfig(JSONObject modelConfig) {
        this.modelConfig = modelConfig;
    }

    public JSONObject getMapperConfig() {
        return mapperConfig;
    }

    public void setMapperConfig(JSONObject mapperConfig) {
        this.mapperConfig = mapperConfig;
    }

    public JSONObject getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(JSONObject serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    public JSONObject getServiceImplConfig() {
        return serviceImplConfig;
    }

    public void setServiceImplConfig(JSONObject serviceImplConfig) {
        this.serviceImplConfig = serviceImplConfig;
    }

    public JSONObject getControllerConfig() {
        return controllerConfig;
    }

    public void setControllerConfig(JSONObject controllerConfig) {
        this.controllerConfig = controllerConfig;
    }

    public Document getXmlDocument() {
        return xmlDocument;
    }

    public void setXmlDocument(Document xmlDocument) {
        this.xmlDocument = xmlDocument;
    }

    public String getMapperName() {
        return mapperName;
    }

    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }

    public JSONObject getXmlConfig() {
        return xmlConfig;
    }

    public void setXmlConfig(JSONObject xmlConfig) {
        this.xmlConfig = xmlConfig;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
