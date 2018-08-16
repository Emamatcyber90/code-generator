package com.zbss.code.generator.plugins;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.zbss.code.generator.config.Const;
import com.zbss.code.generator.table.TableColumn;
import com.zbss.code.generator.table.TableInfo;

import java.util.List;

/**
 * @author zbss
 * @desc 给实体类添加JPA注解
 * @date 2018/8/16 11:31
 */
public class CommentPlugin extends PluginAdapter {

    @Override
    public void pluginJavaModel(List<TableInfo> tableInfoList) {
        System.out.println("添加注释");
        for (TableInfo tableInfo : tableInfoList) {
            addComment(tableInfo);
        }
    }

    private void addComment(TableInfo tableInfo) {
        CompilationUnit cu = tableInfo.getModelCompilationUnit();
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
