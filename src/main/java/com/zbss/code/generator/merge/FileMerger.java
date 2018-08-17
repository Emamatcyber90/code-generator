package com.zbss.code.generator.merge;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.zbss.code.generator.config.Const;
import com.zbss.code.generator.util.DocumentUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author zbss
 * @desc
 * @date 2018/8/17 15:26 
 */
public class FileMerger implements Merger {

    @Override
    public String mergeJavaFile(CompilationUnit newCompilationUnit, CompilationUnit oldCompilationUnit) {
        String lineSeparator = System.getProperty("line.separator");

        //截取Class
        TypeDeclaration<?> newType = newCompilationUnit.getTypes().get(0);
        TypeDeclaration<?> oldType = oldCompilationUnit.getTypes().get(0);

        if (newType.getName().toString().contains("Example")){
            return newCompilationUnit.toString();
        }

        // 设置包名为新的包名
        StringBuilder sb = new StringBuilder(newCompilationUnit.getPackageDeclaration().get().toString());

        //合并imports
        NodeList<ImportDeclaration> newImports = newCompilationUnit.getImports();
        NodeList<ImportDeclaration> oldImports = oldCompilationUnit.getImports();
        NodeList<ImportDeclaration> cmpImports = new NodeList<ImportDeclaration>(newImports);
        if (oldImports.size() > 0){
            for (ImportDeclaration oldImp : oldImports){
                boolean isRepeat = false;
                if (newImports.size() > 0){
                    for (ImportDeclaration newImp : newImports){
                        if (oldImp.toString().equals(newImp.toString())){
                            isRepeat = true;
                            break;
                        }
                    }
                }
                if (!isRepeat){
                    int idx = oldImports.indexOf(oldImp);
                    cmpImports.add(idx, oldImp);
                }
            }
        }
        if (cmpImports.size() > 0){
            for (ImportDeclaration imports : cmpImports) {
                sb.append(imports.toString());
            }
        }

        // 合并类注解
        NodeList<AnnotationExpr> oldTypeList = oldType.getAnnotations();
        NodeList<AnnotationExpr> newTypeList = newType.getAnnotations();
        if (oldTypeList != null && oldTypeList.isNonEmpty()){
            for (AnnotationExpr oldAep : oldTypeList){
                if (null != newTypeList && newTypeList.isNonEmpty()) {
                    boolean isRepeat = false;
                    for (AnnotationExpr newAep : newTypeList) {
                        if (newAep.toString().equals(oldAep.toString())) {
                            isRepeat = true;
                            break;
                        }
                    }
                    if (!isRepeat) {
                        newType.addAnnotation(oldAep);
                    }
                }
            }
        }

        sb.append(lineSeparator);

        // 设置类名
        String className = newType.toString().substring(0, newType.toString().indexOf("{")+1);
        sb.append(className);
        sb.append(lineSeparator);

        //合并fields（根据注解）
        List<FieldDeclaration> newFields = newType.getFields();
        List<FieldDeclaration> oldFields = oldType.getFields();
        List<FieldDeclaration> cmpFields = new ArrayList<FieldDeclaration>(newFields);
        if (oldFields.size() > 0){
            for (FieldDeclaration oldField : oldFields){
                if (!oldField.toString().contains(Const.JAVA_COMMENT_PREFIX)){
                    int index = oldFields.indexOf(oldField);
                    cmpFields.add(index, oldField);
                }
                for (FieldDeclaration newField : newFields){
                    if (newField.getVariables().get(0).getName().equals(oldField.getVariables().get(0).getName())){
                        NodeList<AnnotationExpr> oldAnnotaions = oldField.getAnnotations();
                        NodeList<AnnotationExpr> newAnnotaions = newField.getAnnotations();
                        if (oldAnnotaions != null && oldAnnotaions.isNonEmpty()){
                            for (AnnotationExpr oldAep : oldAnnotaions){
                                if (newAnnotaions != null && newAnnotaions.isNonEmpty()) {
                                    boolean isRepeat = false;
                                    for (AnnotationExpr newAep : newAnnotaions) {
                                        if (newAep.toString().equals(oldAep.toString())) {
                                            isRepeat = true;
                                            break;
                                        }
                                    }
                                    if (!isRepeat) {
                                        newField.addAnnotation(oldAep);
                                    }
                                } else {
                                    newField.addAnnotation(oldAep);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (cmpFields.size() > 0){
            for (FieldDeclaration field : cmpFields){
                String [] strs = field.toString().split(lineSeparator);
                for (String str : strs){
                    sb.append("\t"+str+lineSeparator);
                }
                sb.append(lineSeparator);
            }
        }
        sb.append(lineSeparator);

        //合并methods
        List<MethodDeclaration> newMethods = newType.getMethods();
        List<MethodDeclaration> oldMethods = oldType.getMethods();
        List<MethodDeclaration> cmpMethods = new ArrayList<MethodDeclaration>(newMethods);
        if (oldMethods.size() > 0){
            for (MethodDeclaration oldMethod : oldMethods){
                if (!oldMethod.toString().contains(Const.JAVA_COMMENT_PREFIX)){
                    int index = oldMethods.indexOf(oldMethod);
                    cmpMethods.add(index, oldMethod);
                }
            }
        }
        if (cmpMethods.size() > 0){
            for (MethodDeclaration method : cmpMethods){
                String [] strs = method.toString().split(lineSeparator);
                for (String str : strs){
                    sb.append("\t"+str+lineSeparator);
                }
                sb.append(lineSeparator);
            }
        }

        return sb.append("}").toString();

    }

    @Override
    public String mergeXmlFile(Document newDoc, Document oldDoc) {
        Element oldRootEle = oldDoc.getRootElement();
        Iterator<Element> elementIterator = oldRootEle.elementIterator();
        while (elementIterator.hasNext()) {
            Element element = elementIterator.next();
            Iterator<Node> nodeIterator = element.nodeIterator();
            boolean hasComment = false;
            while (nodeIterator.hasNext()) {
                Node node = nodeIterator.next();
                if (node.getNodeType() == Node.COMMENT_NODE) {
                    if (node.asXML().contains(Const.XML_COMMENT)) {
                        hasComment = true;
                        break;
                    }
                }
            }

            if (!hasComment) {
                element.setParent(null);
                newDoc.getRootElement().add(element);
            }
        }

        return DocumentUtils.convertDocumentToStringWithFormat(newDoc);
    }

}
