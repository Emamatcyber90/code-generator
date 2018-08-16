package com.zbss.code.generator.generator;

import com.alibaba.fastjson.JSONObject;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.config.Const;
import com.zbss.code.generator.plugins.Plugin;
import com.zbss.code.generator.table.TableColumn;
import com.zbss.code.generator.table.TableInfo;
import com.zbss.code.generator.util.FileUtils;
import com.zbss.code.generator.util.ObjectUtils;
import com.zbss.code.generator.util.PathUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 13:30
 */
public class JavaModelGenerator extends Generator {

    private JSONObject conf = null;

    public JavaModelGenerator(Config config) {
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
            tableInfo.setModelCompilationUnit(cu);
            map.put(tableInfo.getDomainName(), cu);
        }

        plugin();
        writeFile();
    }

    @Override
    public void executePlugin(Plugin plugin) {
        plugin.pluginJavaModel(config.getTableInfoList());
    }

    @Override
    public void executeWriteFile(TableInfo tableInfo) {
        CompilationUnit cu = tableInfo.getModelCompilationUnit();
        if (ObjectUtils.isEmpty(cu)) {
            return;
        }
        String modelPath = tableInfo.getModelConfig().getString("targetPackage");
        String filePath = null;
        try {
            filePath = PathUtils.getClassPath() + modelPath.replaceAll("\\.", "\\/") + "/" + tableInfo.getDomainName() + ".java";
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(filePath);
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

        // package
        String pkg = conf.getJSONObject("model").getString("targetPackage");
        cu.setPackageDeclaration(pkg);

        // class name
        ClassOrInterfaceDeclaration type = cu.addClass(tableInfo.getDomainName());

        List<TableColumn> columnList = tableInfo.getActualColumns();
        if (ObjectUtils.isEmpty(columnList)) {
            throw new RuntimeException("table " + tableInfo.getActualName() + " column is empty !");
        }

        // fields
        for (TableColumn actualColumn : columnList) {
            String fieldName = actualColumn.getColumnFieldName();
            String fieldTypeStr = Const.DATA_TYPE.get(actualColumn.getColumnType().split(" ")[0]);
            type.addField(fieldTypeStr, fieldName, Modifier.PRIVATE);
        }

        return cu;
    }
}
