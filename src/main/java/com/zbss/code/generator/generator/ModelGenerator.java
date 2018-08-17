package com.zbss.code.generator.generator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.zbss.code.generator.config.Const;
import com.zbss.code.generator.entity.TableColumn;
import com.zbss.code.generator.entity.TableInfo;
import com.zbss.code.generator.file.FileTypeEnum;
import com.zbss.code.generator.file.GenerateFile;
import com.zbss.code.generator.file.GenerateModelFile;

import java.util.List;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 13:30
 */
public class ModelGenerator extends FileGenerator {

    @Override
    public void generateFile() throws Exception {
        String targetPkg = config.getJsonConfig().getJSONObject("model").getString("targetPackage");
        String targetPrj = config.getJsonConfig().getJSONObject("model").getString("targetProject");
        for (TableInfo tableInfo : config.getTableInfoList()) {
            CompilationUnit cu = new CompilationUnit();
            String pkg = jsonConfig.getJSONObject("model").getString("targetPackage");
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
            generateFile.setFlag(FileTypeEnum.FLAG_JAVA);
            tableInfo.getGenerateFiles().add(generateFile);
        }
    }
}
