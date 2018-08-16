package com.zbss.code.generator.generator;

import com.alibaba.fastjson.JSONObject;
import com.github.javaparser.ast.CompilationUnit;
import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.config.Const;
import com.zbss.code.generator.plugins.Plugin;
import com.zbss.code.generator.table.TableInfo;
import com.zbss.code.generator.util.FileUtils;
import com.zbss.code.generator.util.ObjectUtils;
import com.zbss.code.generator.util.PathUtils;
import com.zbss.code.generator.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 13:30 
 */
public class JavaMapperGenerator extends Generator {

    private JSONObject conf = null;

    public JavaMapperGenerator(Config config) {
        super(config);
        conf = config.getConfig();
    }

    @Override
    public void generate() {
        if (config == null) {
            throw new RuntimeException("配置信息不能为空！");
        }

        List<TableInfo> tableInfoList = config.getTableInfoList();
        if (tableInfoList == null || tableInfoList.isEmpty()) {
            throw new RuntimeException("表格配置不能为空！");
        }

        Map<String, CompilationUnit> map = new HashMap<>();
        for (TableInfo tableInfo : tableInfoList) {
            CompilationUnit cu = generateCompilationUnit(tableInfo);
            tableInfo.setMapperCompilationUnit(cu);
            map.put(tableInfo.getDomainName(), cu);
        }

        plugin();
        writeFile();
    }

    @Override
    public void executePlugin(Plugin plugin) {
        plugin.pluginJavaMapper(config.getTableInfoList());
    }

    @Override
    public void executeWriteFile(TableInfo tableInfo) {
        CompilationUnit cu = tableInfo.getMapperCompilationUnit();
        if (ObjectUtils.isEmpty(cu)) {
            return;
        }
        String mapperPath = tableInfo.getMapperConfig().getString("targetPackage");
        String filePath = PathUtils.getClassPath() + mapperPath.replaceAll("\\.", "\\/") + "/" + tableInfo.getMapperName() + ".java";
        String fileContent = cu.toString();
        String fileEncoding = ObjectUtils.isEmpty(conf.getString("outputFileEncoding")) ? "utf-8" : conf.getString("outputFileEncoding");
        try {
            FileUtils.writeFile(filePath, fileContent, fileEncoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CompilationUnit generateCompilationUnit(TableInfo tableInfo) {
        CompilationUnit cu = new CompilationUnit();

        String pkg = conf.getJSONObject("model").getString("targetPackage");
        cu.setPackageDeclaration(pkg);

        String suffix = Const.MAPPER;
        String sf = conf.getJSONObject("mapper").getJSONObject("properties").getString("suffix");
        suffix = StringUtils.isEmpty(sf) ? suffix : sf;
        String typeName = tableInfo.getDomainName() + suffix;
        tableInfo.setMapperName(typeName);
        cu.addInterface(typeName);
        return cu;

    }
}
