package com.zbss.code.generator.generator;

import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.plugins.Plugin;
import com.zbss.code.generator.table.TableInfo;
import com.zbss.code.generator.util.ObjectUtils;

import java.util.List;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 13:33
 */
public abstract class Generator {

    Config config;

    Generator(Config config) {
        this.config = config;
    }

    public abstract void generate();
    public abstract void executePlugin(Plugin plugin);
    public abstract void executeWriteFile(TableInfo tableInfo);

    void plugin() {
        List<Plugin> pluginList = config.getPluginList();
        if (ObjectUtils.isNotEmpty(pluginList)) {
            for (Plugin plugin : pluginList) {
                executePlugin(plugin);
            }
        }
    }

    void writeFile() {
        List<TableInfo> tableInfoList = config.getTableInfoList();
        if (ObjectUtils.isNotEmpty(tableInfoList)) {
            for (TableInfo tableInfo : tableInfoList) {
                executeWriteFile(tableInfo);
            }
        }
    }

}
