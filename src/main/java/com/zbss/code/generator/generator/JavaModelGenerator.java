package com.zbss.code.generator.generator;

import com.alibaba.fastjson.JSONObject;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.config.Const;
import com.zbss.code.generator.file.FileTypeEnum;
import com.zbss.code.generator.file.GenerateFile;
import com.zbss.code.generator.file.GenerateModelFile;
import com.zbss.code.generator.plugins.Plugin;
import com.zbss.code.generator.entity.TableColumn;
import com.zbss.code.generator.entity.TableInfo;
import com.zbss.code.generator.util.FileUtils;
import com.zbss.code.generator.util.ObjectUtils;

import java.io.File;
import java.util.List;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 13:30
 */
public class JavaModelGenerator extends Generator {

    private JSONObject modelConfig;

    public JavaModelGenerator(Config config) {
        super(config);
        modelConfig = config.getConfig().getJSONObject("model");
    }

    @Override
    public void generateFile() throws Exception {
        String targetPkg = modelConfig.getString("targetPackage");
        String targetPrj = modelConfig.getString("targetProject");
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

            GenerateFile<CompilationUnit> generateFile = new GenerateModelFile<>();
            generateFile.setData(cu);
            generateFile.setTargetProject(targetPrj);
            generateFile.setTargetPackage(targetPkg);
            generateFile.setName(tableInfo.getDomainName() + ".java");
            generateFile.setType(FileTypeEnum.MODEL);
            tableInfo.getGenerateFiles().add(generateFile);
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
        executeWrite(FileTypeEnum.MODEL);
    }

    @Override
    public void executePlugin(Plugin plugin) {
        plugin.pluginJavaModel(config.getTableInfoList());
    }
}
