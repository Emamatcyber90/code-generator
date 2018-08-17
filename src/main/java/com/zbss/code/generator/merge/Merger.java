package com.zbss.code.generator.merge;

import com.github.javaparser.ast.CompilationUnit;
import org.dom4j.Document;

public interface Merger {
    String mergeJavaFile(CompilationUnit newCompilationUnit, CompilationUnit oldCompilationUnit);
    String mergeXmlFile(Document newDoc, Document oldDoc);
}
