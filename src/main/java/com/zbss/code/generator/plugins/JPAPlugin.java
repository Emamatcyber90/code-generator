package com.zbss.code.generator.plugins;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
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
public class JPAPlugin extends PluginAdapter {

    @Override
    public void pluginJavaModel(List<TableInfo> tableInfoList) {
        System.out.println("添加JPA注解");
        for (TableInfo tableInfo : tableInfoList) {
            addTableJpaAnnptation(tableInfo);
        }
    }

    private void addTableJpaAnnptation(TableInfo tableInfo) {
        GenerateFile<CompilationUnit> generateFile = getGenerateFileByFileType(tableInfo, FileTypeEnum.MODEL);
        if (generateFile == null || generateFile.getData() == null) {
            return;
        }
        CompilationUnit cu = generateFile.getData();
        cu.addImport("javax.persistence.*");
        ClassOrInterfaceDeclaration clazz = cu.getClassByName(cu.getType(0).getName().asString()).get();
        AnnotationExpr ae = JavaParser.parseAnnotation("@Table(name = \"" + tableInfo.getActualName() + "\")");
        clazz.addAnnotation(ae);

        List<FieldDeclaration> fields = clazz.getFields();
        String primaryKey = tableInfo.getPrimaryKey();
        List<TableColumn> tableColumnList = tableInfo.getActualColumns();
        for (TableColumn tableColumn : tableColumnList) {
            for (FieldDeclaration field : fields) {
                String fieldName = field.getVariable(0).getName().asString();
                if (fieldName.equals(tableColumn.getColumnFieldName())) {
                    if (primaryKey.equals(fieldName)) {
                        field.addAnnotation(JavaParser.parseAnnotation("@Id"));
                        field.addAnnotation(JavaParser.parseAnnotation("@GeneratedValue(strategy = GenerationType.IDENTITY)"));
                    }
                    field.addAnnotation(JavaParser.parseAnnotation("@Column(name = \"" + tableColumn.getColumnActualName() + "\")"));
                }
            }
        }
    }
}
