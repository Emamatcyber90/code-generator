package com.zbss.code.generator.util;

import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zbss
 * @desc
 * @date 2018/8/16 12:12 
 */
public class JavaparserUtils {

    public static AnnotationExpr gernerateAnnotation(String annotationName, List<Map<String, Object>> keyVal) {
        NormalAnnotationExpr annotationExpr = new NormalAnnotationExpr();
        annotationExpr.setName(annotationName);
        for (Map<String, Object> map : keyVal) {
            Iterator<Map.Entry<String, Object>> iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                MemberValuePair pair = new MemberValuePair();
                pair.setName(entry.getKey());
                pair.setValue(new StringLiteralExpr(String.valueOf(entry.getValue())));
                annotationExpr.getPairs().add(pair);
            }
        }

        return annotationExpr;
    }

}
