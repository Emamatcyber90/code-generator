package com.zbss.code.generator;

import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.generator.Generator;
import com.zbss.code.generator.generator.JavaMapperGenerator;
import com.zbss.code.generator.generator.JavaModelGenerator;
import com.zbss.code.generator.generator.XmlGenerator;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 9:31 
 */
public class Application {

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        Config config = Config.getInstance();
        Generator modelGenerator = new JavaModelGenerator(config);
        Generator mapperGenerator = new JavaMapperGenerator(config);
        Generator xmlGenerator = new XmlGenerator(config);
        modelGenerator.generate();
        mapperGenerator.generate();
        xmlGenerator.generate();
        System.out.println("Generate success !");
    }

}
