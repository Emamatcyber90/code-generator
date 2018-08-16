package com.zbss.code.generator.generator;

import com.alibaba.fastjson.JSONObject;
import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.plugins.Plugin;
import com.zbss.code.generator.util.ObjectUtils;

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

    public abstract void generateFile();
    public abstract void mergeFile();
    public abstract void writeFile();
    public abstract void executePlugin(Plugin plugin);

    public void generate() {
        generateFile();
        pluginFile();
        mergeFile();
        writeFile();
    }

    private void pluginFile() {
        List<Plugin> pluginList = config.getPluginList();
        if (ObjectUtils.isNotEmpty(pluginList)) {
            for (Plugin plugin : pluginList) {
                executePlugin(plugin);
            }
        }
    }

}
