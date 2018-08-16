package com.zbss.code.generator.generator;

import com.github.javaparser.ast.CompilationUnit;
import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.config.Const;
import com.zbss.code.generator.plugins.Plugin;
import com.zbss.code.generator.table.TableInfo;
import com.zbss.code.generator.util.FileUtils;
import com.zbss.code.generator.util.ObjectUtils;
import com.zbss.code.generator.util.PathUtils;
import com.zbss.code.generator.util.StringUtils;

import java.io.IOException;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 13:30 
 */
public class JavaMapperGenerator extends Generator {

    public JavaMapperGenerator(Config config) {
        super(config);
    }

    @Override
    public void generateFile() {
        for (TableInfo tableInfo : config.getTableInfoList()) {
            CompilationUnit cu = new CompilationUnit();

            String pkg = conf.getJSONObject("model").getString("targetPackage");
            cu.setPackageDeclaration(pkg);

            String suffix = Const.MAPPER;
            String sf = tableInfo.getMapperConfig().getJSONObject("properties").getString("suffix");
            suffix = StringUtils.isEmpty(sf) ? suffix : sf;
            String typeName = tableInfo.getDomainName() + suffix;
            tableInfo.setMapperName(typeName);
            cu.addInterface(typeName);
            tableInfo.setMapperCompilationUnit(cu);
        }
    }

    @Override
    public void mergeFile() {
        if (!conf.getBoolean("isJavaMerge")) {
            return;
        }
    }

    @Override
    public void writeFile() {
        for (TableInfo tableInfo : config.getTableInfoList()) {
            CompilationUnit cu = tableInfo.getMapperCompilationUnit();
            if (ObjectUtils.isEmpty(cu)) {
                continue;
            }
            String mapperPath = tableInfo.getMapperConfig().getString("targetPackage");
            String filePath = PathUtils.getClassPath() + mapperPath.replaceAll("\\.", "\\/") + "/" + tableInfo.getMapperName() + ".java";
            String fileContent = cu.toString();
            String fileEncoding = ObjectUtils.isEmpty(conf.getString("outputFileEncoding")) ? "utf-8" : conf.getString("outputFileEncoding");
            try {
                FileUtils.writeFile(filePath, fileContent, fileEncoding);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void executePlugin(Plugin plugin) {
        plugin.pluginJavaMapper(config.getTableInfoList());
    }
}
