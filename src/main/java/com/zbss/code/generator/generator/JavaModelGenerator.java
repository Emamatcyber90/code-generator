package com.zbss.code.generator.generator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.config.Const;
import com.zbss.code.generator.plugins.Plugin;
import com.zbss.code.generator.table.TableColumn;
import com.zbss.code.generator.table.TableInfo;
import com.zbss.code.generator.util.ObjectUtils;

import java.io.File;
import java.util.List;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 13:30
 */
public class JavaModelGenerator extends Generator {

    public JavaModelGenerator(Config config) {
        super(config);
    }

    @Override
    public void generateFile() throws Exception {
        for (TableInfo tableInfo : config.getTableInfoList()) {
            CompilationUnit cu = new CompilationUnit();
            String pkg = conf.getJSONObject("model").getString("targetPackage");
            cu.setPackageDeclaration(pkg);
            ClassOrInterfaceDeclaration type = cu.addClass(tableInfo.getDomainName());
            List<TableColumn> columnList = tableInfo.getActualColumns();
            for (TableColumn actualColumn : columnList) {
                String fieldName = actualColumn.getColumnFieldName();
                String fieldTypeStr = Const.DATA_TYPE.get(actualColumn.getColumnType().split(" ")[0]);
                type.addField(fieldTypeStr, fieldName, Modifier.PRIVATE);
            }
            tableInfo.setModelCompilationUnit(cu);
        }
    }

    @Override
    public void mergeFile() throws Exception {
        if (!conf.getBoolean("isJavaMerge")) {
            return;
        }
    }

    @Override
    public void writeFile() throws Exception {
        String targetPkg = conf.getJSONObject("model").getString("targetPackage");
        String targetPrj = conf.getJSONObject("model").getString("targetProject");
        File dir = getDirectory(targetPrj, targetPkg);
        for (TableInfo tableInfo : config.getTableInfoList()) {
            CompilationUnit cu = tableInfo.getModelCompilationUnit();
            if (ObjectUtils.isEmpty(cu)) {
                continue;
            }

            File targetFile = new File(dir, tableInfo.getDomainName() + ".java");
            String fileEncoding = ObjectUtils.isEmpty(conf.getString("outputFileEncoding")) ? "utf-8" : conf.getString("outputFileEncoding");
            String fileContent = cu.toString();
            writeFile(targetFile, fileContent, fileEncoding);
        }
    }

    @Override
    public void executePlugin(Plugin plugin) {
        plugin.pluginJavaModel(config.getTableInfoList());
    }
}
