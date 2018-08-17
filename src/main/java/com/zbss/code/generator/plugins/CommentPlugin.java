package com.zbss.code.generator.plugins;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.zbss.code.generator.config.Const;
import com.zbss.code.generator.entity.TableColumn;
import com.zbss.code.generator.entity.TableInfo;
import com.zbss.code.generator.file.FileTypeEnum;
import com.zbss.code.generator.file.GenerateFile;

import java.util.List;

/**
 * @author zbss
 * @desc 给实体类添加JPA注解
 * @date 2018/8/16 11:31
 */
public class CommentPlugin extends PluginAdapter {

    @Override
    public void pluginJavaModel(List<TableInfo> tableInfoList) {
        for (TableInfo tableInfo : tableInfoList) {
            addComment(tableInfo, FileTypeEnum.MODEL);
        }
    }

    private void addComment(TableInfo tableInfo, FileTypeEnum type) {
        GenerateFile<CompilationUnit> generateFile = getGenerateFileByFileType(tableInfo, type);
        if (generateFile == null || generateFile.getData() == null) {
            return;
        }
        CompilationUnit cu = generateFile.getData();
        ClassOrInterfaceDeclaration clazz = cu.getClassByName(cu.getType(0).getName().asString()).get();
        List<FieldDeclaration> fields = clazz.getFields();
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
}
