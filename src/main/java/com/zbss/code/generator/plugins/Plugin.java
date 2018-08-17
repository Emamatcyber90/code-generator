package com.zbss.code.generator.plugins;

import com.alibaba.fastjson.JSONObject;
import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.entity.TableInfo;
import com.zbss.code.generator.file.FileTypeEnum;
import com.zbss.code.generator.file.GenerateFile;

import java.util.List;

public abstract class Plugin {

    JSONObject properties;
    Config config;
    JSONObject conf;
    public void plugin() throws Exception {
        pluginJavaModel(config.getTableInfoList());
        pluginJavaMapper(config.getTableInfoList());
        pluginXml(config.getTableInfoList());
        pluginJavaService(config.getTableInfoList());
        pluginJavaServiceImpl(config.getTableInfoList());
        pluginJavaController(config.getTableInfoList());
    }

    // 插件增强
    public abstract void pluginJavaModel(List<TableInfo> tableInfoList);
    public abstract void pluginJavaMapper(List<TableInfo> tableInfoList);
    public abstract void pluginXml(List<TableInfo> tableInfoList);
    public abstract void pluginJavaController(List<TableInfo> tableInfoList);
    public abstract void pluginJavaService(List<TableInfo> tableInfoList);
    public abstract void pluginJavaServiceImpl(List<TableInfo> tableInfoList);

    public void setProperties(JSONObject properties) {
        this.properties = properties;
    }
    public void setConfig(Config config) {
        this.config = config;
        conf = config.getJsonConfig();
    }
    public GenerateFile getGenerateFileByFileType(TableInfo tableInfo, FileTypeEnum type) {
        List<GenerateFile<?>> generateFileList = tableInfo.getGenerateFiles();
        for (GenerateFile<?> generateFile : generateFileList) {
            if (generateFile.getType() == type) {
                return generateFile;
            }
        }
        return null;
    }

}
