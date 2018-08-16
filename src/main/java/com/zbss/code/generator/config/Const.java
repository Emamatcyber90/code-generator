package com.zbss.code.generator.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 18:29
 */
public class Const {

    public static final String DEFAULT_CONFIG = "generate-config.json";
    public static final Map<String, String> DATA_TYPE = new HashMap<>();
    public static final String JAVA_COMMENT_PREFIX = "@zbss.generated";
    public static final String XML_COMMENT = "@zbss.generated";
    public static final String BLANK = " ";
    public static final String MAPPER = "Mapper";

    static {
        DATA_TYPE.put("TINYINT", "Integer");
        DATA_TYPE.put("SMALLINT", "Integer");
        DATA_TYPE.put("MEDIUMINT", "Integer");
        DATA_TYPE.put("INT", "Integer");
        DATA_TYPE.put("BIT", "Integer");
        DATA_TYPE.put("INTEGER", "Integer");
        DATA_TYPE.put("BIGINT", "Integer");
        DATA_TYPE.put("FLOAT", "Double");
        DATA_TYPE.put("DOUBLE", "Double");
        DATA_TYPE.put("DECIMAL", "BigDecimal");
        DATA_TYPE.put("CHAR", "String");
        DATA_TYPE.put("VARCHAR", "String");
        DATA_TYPE.put("TINYBLOB", "byte[]");
        DATA_TYPE.put("TINYTEXT", "String");
        DATA_TYPE.put("BLOB", "byte[]");
        DATA_TYPE.put("TEXT", "String");
        DATA_TYPE.put("MEDIUMBLOB", "byte[]");
        DATA_TYPE.put("MEDIUMTEXT", "String");
        DATA_TYPE.put("LOGNGBLOB", "byte[]");
        DATA_TYPE.put("LONGTEXT", "String");
        DATA_TYPE.put("DATE", "Date");
        DATA_TYPE.put("TIME", "Date");
        DATA_TYPE.put("YEAR", "Date");
        DATA_TYPE.put("DATETIME", "Date");
        DATA_TYPE.put("TIMESTAMP", "Date");
    }

}
