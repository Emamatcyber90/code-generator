package com.zbss.code.generator.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zbss.code.generator.entity.TableColumn;
import com.zbss.code.generator.entity.TableInfo;
import com.zbss.code.generator.generator.FileGenerator;
import com.zbss.code.generator.jdbc.Jdbc;
import com.zbss.code.generator.merge.FileMerger;
import com.zbss.code.generator.merge.Merger;
import com.zbss.code.generator.plugins.Plugin;
import com.zbss.code.generator.util.FileUtils;
import com.zbss.code.generator.util.ObjectUtils;
import com.zbss.code.generator.util.StringUtils;
import com.zbss.code.generator.util.Utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 12:48
 */
public class Config {

    private static volatile Config instance;
    private JSONObject jsonConfig;                                  // 配置文件的json格式
    private List<Plugin> pluginList = new ArrayList<>();            // 插件
    private List<FileGenerator> generatorList = new ArrayList<>();  // 文件生成器
    private List<TableInfo> tableInfoList = new ArrayList<>();      // 单表信息，包含需要生成的各种信息
    private File configFile;                                        // 配置文件
    private Merger merger;                                          // 合并器，合并文件用到

    private Config(File file) throws Exception {
        this.configFile = file;
        initConfig();
        initTable();
        initGenerator();
        initPlugin();
        initMerger();
    }

    private void initConfig() throws Exception {
        String configStr = null;
        if (configFile == null) {
            configStr = FileUtils.readFileFromClasspath("config.json");
        } else {
            configStr = FileUtils.readFile(configFile);
        }

        jsonConfig = JSON.parseObject(configStr);
    }

    private void initTable() throws Exception {
        JSONArray tables = jsonConfig.getJSONArray("tables");
        Jdbc jdbc = Jdbc.getInstance(this);
        Connection conn = jdbc.getConnection();
        DatabaseMetaData databaseMetaData = conn.getMetaData();
        tableInfoList = new ArrayList<>();
        for (int i = 0; i < tables.size(); i++) {
            JSONObject table = tables.getJSONObject(i);
            String tableName = table.getString("tableName");
            String domainName = table.getString("domainName");
            if (StringUtils.isEmpty(tableName)) {
                continue;
            }
            if (StringUtils.isEmpty(domainName)) {
                domainName = Utils.firstUpperCase(Utils.convertToCamelCase(tableName));
            }

            TableInfo tableInfo = new TableInfo();
            tableInfo.setActualName(tableName);
            tableInfo.setDomainName(domainName);
            tableInfo.setConfig(this);
            List<TableColumn> columnList = new ArrayList<>();
            tableInfo.setActualColumns(columnList);
            ResultSet rs = databaseMetaData.getColumns(null, "%", tableName, "%");
            while (rs.next()) {
                TableColumn column = new TableColumn();
                column.setColumnActualName(rs.getString("COLUMN_NAME"));
                column.setColumnType(rs.getString("TYPE_NAME"));
                column.setColumnComment(rs.getString("REMARKS"));
                column.setColumnFieldName(Utils.convertToCamelCase(column.getColumnActualName()));
                columnList.add(column);
            }

            rs = databaseMetaData.getPrimaryKeys(null, null, tableName);
            while (rs.next()) {
                tableInfo.setPrimaryKey(rs.getString("COLUMN_NAME"));
                break;
            }

            tableInfoList.add(tableInfo);
        }

        jdbc.returnConnection(conn);
    }

    private void initPlugin() throws Exception {
        JSONArray pluginArr = jsonConfig.getJSONArray("plugins");
        if (pluginArr != null && !pluginArr.isEmpty()) {
            Collections.sort(pluginArr, new Comparator<Object>() {
                @Override
                public int compare(Object o1, Object o2) {
                    JSONObject j1 = JSON.parseObject(JSON.toJSONString(o1));
                    JSONObject j2 = JSON.parseObject(JSON.toJSONString(o2));
                    return j1.getInteger("order") - j2.getInteger("order");
                }
            });
            for (int i = 0; i < pluginArr.size(); i++) {
                JSONObject pluginObj = pluginArr.getJSONObject(i);
                String className = pluginObj.getString("className");
                if (StringUtils.isNotEmpty(className)) {
                    JSONObject pros = pluginObj.getJSONObject("properties");
                    Plugin plugin = (Plugin) Class.forName(className).newInstance();
                    if (pros != null) {
                        plugin.setProperties(pros);
                    }
                    plugin.setConfig(this);
                    pluginList.add(plugin);
                }
            }
        }
    }

    private void initGenerator() throws Exception {
        JSONArray generators = jsonConfig.getJSONArray("generators");
        if (generators != null && !generators.isEmpty()) {
            Collections.sort(generators, new Comparator<Object>() {
                @Override
                public int compare(Object o1, Object o2) {
                    JSONObject j1 = JSON.parseObject(JSON.toJSONString(o1));
                    JSONObject j2 = JSON.parseObject(JSON.toJSONString(o2));
                    return j1.getInteger("order") - j2.getInteger("order");
                }
            });
            for (int i = 0; i < generators.size(); i++) {
                JSONObject generatorObj = generators.getJSONObject(i);
                String className = generatorObj.getString("className");
                if (StringUtils.isNotEmpty(className)) {
                    JSONObject pros = generatorObj.getJSONObject("properties");
                    FileGenerator fileGenerator = (FileGenerator) Class.forName(className).newInstance();
                    if (pros != null) {
                        fileGenerator.setProperties(pros);
                    }
                    fileGenerator.setConfig(this);
                    generatorList.add(fileGenerator);
                }
            }
        }
    }

    private void initMerger() throws Exception {
        String mergerClassName = jsonConfig.getString("merger");
        if (ObjectUtils.isNotEmpty(mergerClassName)) {
            merger = (Merger) Class.forName(mergerClassName).newInstance();
        } else {
            merger = new FileMerger();
        }
    }

    public JSONObject getJsonConfig() {
         return jsonConfig;
    }

    public List<Plugin> getPluginList() {
        return pluginList;
    }

    public List<TableInfo> getTableInfoList() {
        return tableInfoList;
    }

    public List<FileGenerator> getGeneratorList() {
        return generatorList;
    }

    public Merger getMerger() {
        return merger;
    }

    public static Config getInstance(File file) throws Exception {
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config(file);
                }
            }
        }
        return instance;
    }

    public static Config getInstance() throws Exception {
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config(null);
                }
            }
        }
        return instance;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
