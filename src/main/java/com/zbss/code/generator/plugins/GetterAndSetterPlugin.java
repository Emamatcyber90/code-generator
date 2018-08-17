package com.zbss.code.generator.plugins;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.zbss.code.generator.config.Const;
import com.zbss.code.generator.entity.TableColumn;
import com.zbss.code.generator.entity.TableInfo;
import com.zbss.code.generator.file.FileTypeEnum;
import com.zbss.code.generator.file.GenerateFile;
import com.zbss.code.generator.util.Utils;

import java.util.List;

/**
 * @author zbss
 * @desc 给实体类添加get和set方法
 * @date 2018/8/16 11:31
 */
public class GetterAndSetterPlugin extends PluginAdapter {

    @Override
    public void pluginJavaModel(List<TableInfo> tableInfoList) {
        for (TableInfo tableInfo : tableInfoList) {
            addGetterAndSetter(tableInfo);
        }
    }

    private void addGetterAndSetter(TableInfo tableInfo) {
        GenerateFile<CompilationUnit> generateFile = getGenerateFileByFileType(tableInfo, FileTypeEnum.MODEL);
        if (generateFile == null || generateFile.getData() == null) {
            return;
        }
        CompilationUnit cu = generateFile.getData();
        ClassOrInterfaceDeclaration clazz = cu.getClassByName(cu.getType(0).getName().asString()).get();
        List<TableColumn> columnList = tableInfo.getActualColumns();
        for (TableColumn tableColumn : columnList) {
            String methodName = Utils.firstUpperCase(tableColumn.getColumnFieldName());
            MethodDeclaration getMethod = clazz.addMethod("get" + methodName, Modifier.PUBLIC);
            getMethod.setType(Const.DATA_TYPE.get(tableColumn.getColumnType().split(" ")[0]));
            BlockStmt getBlock = new BlockStmt();
            getMethod.setBody(getBlock);
            getBlock.addStatement("return " + tableColumn.getColumnFieldName() + ";");

            MethodDeclaration setMethod = clazz.addMethod("set" + methodName, Modifier.PUBLIC);
            setMethod.addParameter(Const.DATA_TYPE.get(tableColumn.getColumnType().split(" ")[0]), tableColumn.getColumnFieldName());
            BlockStmt setblock = new BlockStmt();
            setMethod.setBody(setblock);
            setblock.addStatement("this." + tableColumn.getColumnFieldName() + "=" + tableColumn.getColumnFieldName() + ";");
        }
    }
}
