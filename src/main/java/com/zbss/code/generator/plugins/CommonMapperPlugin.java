package com.zbss.code.generator.plugins;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.zbss.code.generator.table.TableInfo;

import java.util.List;

/**
 * @author zbss
 * @desc
 * @date 2018/8/16 15:06
 */
public class CommonMapperPlugin extends PluginAdapter {

    @Override
    public void pluginJavaMapper(List<TableInfo> tableInfoList) {
        for (TableInfo tableInfo : tableInfoList) {
            addSuperMapper(tableInfo);
        }
    }

    private void addSuperMapper(TableInfo tableInfo) {
        CompilationUnit cu = tableInfo.getMapperCompilationUnit();
        cu.addImport("tk.mybatis.mapper.common.Mapper");
        ClassOrInterfaceDeclaration type = cu.getInterfaceByName(cu.getType(0).getName().asString()).get();
        ClassOrInterfaceType superType = JavaParser.parseClassOrInterfaceType("Mapper<" + tableInfo.getDomainName() + ">");
        type.addExtendedType(superType);
    }
}
