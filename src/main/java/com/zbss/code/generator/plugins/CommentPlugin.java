package com.zbss.code.generator.plugins;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.zbss.code.generator.config.Const;
import com.zbss.code.generator.entity.TableColumn;
import com.zbss.code.generator.entity.TableInfo;
import com.zbss.code.generator.file.FileTypeEnum;
import com.zbss.code.generator.file.GenerateFile;
import com.zbss.code.generator.util.ObjectUtils;

import java.util.List;

/**
 * @author zbss
 * @desc 给实体类添加注释
 * @date 2018/8/16 11:31
 */
public class CommentPlugin extends PluginAdapter {

    @Override
    public void pluginJavaMapper(List<TableInfo> tableInfoList) {
        for (TableInfo tableInfo : tableInfoList) {
            addComment(tableInfo, FileTypeEnum.MAPPER, TYPE_INTERFACE);
        }
    }

    @Override
    public void pluginJavaModel(List<TableInfo> tableInfoList) {
        for (TableInfo tableInfo : tableInfoList) {
            addComment(tableInfo, FileTypeEnum.MODEL, TYPE_CLASS);
        }
    }

    private void addComment(TableInfo tableInfo, FileTypeEnum type, String classType) {
        GenerateFile<CompilationUnit> generateFile = getGenerateFileByFileType(tableInfo, type);
        if (generateFile == null || generateFile.getData() == null) {
            return;
        }
        CompilationUnit cu = generateFile.getData();
        ClassOrInterfaceDeclaration clazz = null;
        if (TYPE_CLASS.equals(classType)) {
            clazz = cu.getClassByName(cu.getType(0).getName().asString()).get();
        } else {
            clazz = cu.getInterfaceByName(cu.getType(0).getName().asString()).get();
        }
        List<FieldDeclaration> fields = clazz.getFields();
        if (ObjectUtils.isNotEmpty(fields)) {
            List<TableColumn> tableColumnList = tableInfo.getActualColumns();
            for (TableColumn tableColumn : tableColumnList) {
                for (FieldDeclaration field : fields) {
                    String fieldName = field.getVariable(0).getName().asString();
                    if (fieldName.equals(tableColumn.getColumnFieldName())) {
                        field.setComment(new BlockComment(Const.JAVA_COMMENT_PREFIX + Const.BLANK + tableColumn.getColumnComment()));
                    }
                }
            }
        }

        List<MethodDeclaration> methods = clazz.getMethods();
        if (ObjectUtils.isNotEmpty(methods)) {
            for (MethodDeclaration method : methods) {
                method.setComment(new BlockComment(Const.JAVA_COMMENT_PREFIX));
            }
        }
    }
}
