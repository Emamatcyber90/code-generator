package com.zbss.code.generator.generator;

import com.alibaba.fastjson.JSONObject;
import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.entity.TableInfo;
import com.zbss.code.generator.file.FileTypeEnum;
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
public abstract class Generator {

    Config config;
    JSONObject conf;

    Generator(Config config) {
        this.config = config;
        conf = config.getConfig();
    }

    public abstract void generateFile() throws Exception;

    public abstract void mergeFile() throws Exception;

    public abstract void writeFile() throws Exception;

    public abstract void executePlugin(Plugin plugin) throws Exception;

    public void generate() throws Exception {
        generateFile();
        pluginFile();
        mergeFile();
        writeFile();
    }

    private void pluginFile() throws Exception{
        List<Plugin> pluginList = config.getPluginList();
        if (ObjectUtils.isNotEmpty(pluginList)) {
            for (Plugin plugin : pluginList) {
                executePlugin(plugin);
            }
        }
    }

    void executeWrite(FileTypeEnum type) throws Exception {
        String fileEncoding = ObjectUtils.isEmpty(conf.getString("outputFileEncoding")) ? "utf-8" : conf.getString("outputFileEncoding");
        for (TableInfo tableInfo : config.getTableInfoList()) {
            List<GenerateFile<?>> generateFileList = tableInfo.getGenerateFiles();
            for (GenerateFile<?> generateFile : generateFileList) {
                if (generateFile.getType() == type) {
                    File file = new File(FileUtils.getDirectory(generateFile.getTargetProject(), generateFile.getTargetPackage()), generateFile.getName());
                    FileUtils.writeFile(file, generateFile.getContent(), fileEncoding);
                }
            }
        }
    }

}
