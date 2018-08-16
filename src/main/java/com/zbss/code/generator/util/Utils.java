package com.zbss.code.generator.util;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 18:07
 */
public class Utils {

    public static String convertToCamelCase(String name) {
        if (StringUtils.isEmpty(name)) {
            return name;
        }

        String fieldName = "";
        String[] names = name.split("_");
        for (int i = 0; i < names.length; i++) {
            if (i == 0) {
                fieldName += names[i].toLowerCase();
            } else {
                fieldName += names[i].substring(0, 1).toUpperCase() + names[i].substring(1);
            }
        }

        return fieldName;
    }

    public static String firstUpperCase(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }

        if (Character.isUpperCase(str.charAt(0))) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String firstLowerCase(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }

        if (Character.isLowerCase(str.charAt(0))) {
            return str;
        }

        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

}
