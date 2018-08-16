package com.zbss.code.generator.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zbss.code.generator.jdbc.Jdbc;
import com.zbss.code.generator.plugins.Plugin;
import com.zbss.code.generator.table.TableColumn;
import com.zbss.code.generator.table.TableInfo;
import com.zbss.code.generator.util.FileUtils;
import com.zbss.code.generator.util.StringUtils;
import com.zbss.code.generator.util.Utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 12:48
 */
public class Config {

    private static volatile Config instance;
    private String configPath;
    private JSONObject config;
    private List<Plugin> pluginList = new ArrayList<>();
    private List<TableInfo> tableInfoList = new ArrayList<>();

    private Config(String path) {
        this.configPath = path;
        try {
            initConfig();
            initTable();
            initPlugin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initConfig() throws Exception {
        String configStr = null;
        if (configPath == null || "".equals(configPath)) {
            configPath = "config.json";
            configStr = FileUtils.readFileFromClasspath(configPath);
        } else {
            configStr = FileUtils.readFile(configPath);
        }
        config = JSON.parseObject(configStr);
    }

    private void initTable() throws Exception {
        JSONArray tables = config.getJSONArray("tables");
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
            tableInfo.setModelConfig(config.getJSONObject("model"));
            tableInfo.setMapperConfig(config.getJSONObject("mapper"));
            tableInfo.setXmlConfig(config.getJSONObject("xml"));
            tableInfo.setServiceConfig(config.getJSONObject("service"));
            tableInfo.setServiceImplConfig(config.getJSONObject("serviceImpl"));
            tableInfo.setControllerConfig(config.getJSONObject("controller"));
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
        JSONArray pluginArr = config.getJSONArray("plugins");
        if (pluginArr != null && !pluginArr.isEmpty()) {
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

    public JSONObject getConfig() {
         return config;
    }

    public List<Plugin> getPluginList() {
        return pluginList;
    }

    public List<TableInfo> getTableInfoList() {
        return tableInfoList;
    }

    public static Config getInstance(String path) {
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config(path);
                }
            }
        }
        return instance;
    }

    public static Config getInstance() {
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
