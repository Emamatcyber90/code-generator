package com.zbss.code.generator.plugins;

import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.table.TableInfo;

import java.util.List;
import java.util.Map;

public abstract class Plugin {

    Map<String, Object> properties;
    Config config;

    public abstract void pluginJavaModel(List<TableInfo> tableInfoList);
    public abstract void pluginJavaMapper(List<TableInfo> tableInfoList);
    public abstract void pluginJavaController(List<TableInfo> tableInfoList);
    public abstract void pluginJavaService(List<TableInfo> tableInfoList);
    public abstract void pluginJavaServiceImpl(List<TableInfo> tableInfoList);
    public abstract void pluginXml(List<TableInfo> tableInfoList);

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
    public void setConfig(Config config) {
        this.config = config;
    }
}
