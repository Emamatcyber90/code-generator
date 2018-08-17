package com.zbss.code.generator.generator;

import com.alibaba.fastjson.JSONObject;
import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.entity.TableInfo;
import com.zbss.code.generator.file.GenerateFile;
import com.zbss.code.generator.plugins.Plugin;
import com.zbss.code.generator.util.FileUtils;
import com.zbss.code.generator.util.ObjectUtils;

import java.io.File;
import java.util.List;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 13:33
 */
public class Generator {

    Config config;
    JSONObject conf;

    public Generator(Config config) {
        this.config = config;
        conf = config.getJsonConfig();
    }

    public void generate() throws Exception {
        generateFile();
        pluginFile();
        writeFile();
    }

    private void generateFile() throws Exception {
        List<FileGenerator> generatorList = config.getGeneratorList();
        if (ObjectUtils.isNotEmpty(generatorList)) {
            for (FileGenerator fileGenerator : generatorList) {
                fileGenerator.generateFile();
            }
        }
    }

    private void pluginFile() throws Exception{
        List<Plugin> pluginList = config.getPluginList();
        if (ObjectUtils.isNotEmpty(pluginList)) {
            for (Plugin plugin : pluginList) {
                plugin.plugin();
            }
        }
    }

    private void writeFile() throws Exception {
        String fileEncoding = ObjectUtils.isEmpty(conf.getString("outputFileEncoding")) ? "utf-8" : conf.getString("outputFileEncoding");
        List<TableInfo> tableInfoList = config.getTableInfoList();
        if (ObjectUtils.isNotEmpty(tableInfoList)) {
            for (TableInfo tableInfo : config.getTableInfoList()) {
                List<GenerateFile<?>> generateFileList = tableInfo.getGenerateFiles();
                for (GenerateFile<?> generateFile : generateFileList) {
                    File file = new File(FileUtils.getDirectory(generateFile.getTargetProject(), generateFile.getTargetPackage()), generateFile.getName());
                    FileUtils.writeFile(file, generateFile.getContent(), fileEncoding);
                }
            }
        }
    }

}
