package com.zbss.code.generator.file;

import com.github.javaparser.ast.CompilationUnit;

public class GenerateMapperFile<T> extends GenerateFile {
    @Override
    String convertDataToContent(Object data) {
        CompilationUnit cu = (CompilationUnit)data;
        return cu.toString();
    }
}
