package com.zbss.code.generator;

import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.generator.Generator;
import com.zbss.code.generator.util.StringUtils;

import java.io.File;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 9:31
 */
public class Application {

    public static void main(String[] args) throws Exception {
        System.out.println("The runtime dir is:" + System.getProperty("user.dir"));
//        String userDir = System.getProperty("user.dir");
//        String configFile = "";
//        if (args != null) {
//            for (String arg : args) {
//                if (arg.contains("--config")) {
//                    configFile = arg.split("=")[1];
//                    break;
//                }
//            }
//        }
//
//        if (StringUtils.isEmpty(configFile)) {
//            throw new RuntimeException("The config file  is missing !");
//        }
//
//        File file = new File(userDir + File.separator + configFile);
//        Config config = Config.getInstance(file);
        Config config = Config.getInstance();
        Generator generator = new Generator(config);
        generator.generate();
        System.out.println("Generate success !");
    }

}
