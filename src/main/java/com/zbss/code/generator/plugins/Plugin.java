package com.zbss.code.generator.plugins;

import com.alibaba.fastjson.JSONObject;
import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.entity.TableInfo;
import com.zbss.code.generator.file.FileTypeEnum;
import com.zbss.code.generator.file.GenerateFile;

import java.util.List;
import java.util.Map;

public abstract class Plugin {

    Map<String, Object> properties;
    Config config;
    JSONObject conf;

    // 插件增强
    public abstract void pluginJavaModel(List<TableInfo> tableInfoList);
    public abstract void pluginJavaMapper(List<TableInfo> tableInfoList);
    public abstract void pluginXml(List<TableInfo> tableInfoList);
    public abstract void pluginJavaController(List<TableInfo> tableInfoList);
    public abstract void pluginJavaService(List<TableInfo> tableInfoList);
    public abstract void pluginJavaServiceImpl(List<TableInfo> tableInfoList);

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
    public void setConfig(Config config) {
        this.config = config;
        conf = config.getConfig();
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
