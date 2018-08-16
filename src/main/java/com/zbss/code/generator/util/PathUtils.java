package com.zbss.code.generator.util;

public class PathUtils {

    public static String getClassPath() {
        return Thread.currentThread().getContextClassLoader ().getResource("").getPath();
    }

}
