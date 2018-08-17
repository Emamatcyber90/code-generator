package com.zbss.code.generator.generator;

import com.alibaba.fastjson.JSONObject;
import com.zbss.code.generator.config.Config;

/**
 * @author zbss
 * @desc
 * @date 2018/8/17 13:19 
 */
public abstract class FileGenerator {

    Config config;
    JSONObject properties;
    JSONObject jsonConfig;

    public abstract void generateFile() throws Exception;

    public void setProperties(JSONObject properties) {
        this.properties = properties;
    }

    public void setConfig(Config config) {
        this.config = config;
        this.jsonConfig = config.getJsonConfig();
    }
}
