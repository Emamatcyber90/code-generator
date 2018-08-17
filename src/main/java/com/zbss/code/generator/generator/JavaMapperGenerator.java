package com.zbss.code.generator.generator;

import com.alibaba.fastjson.JSONObject;
import com.github.javaparser.ast.CompilationUnit;
import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.config.Const;
import com.zbss.code.generator.entity.TableInfo;
import com.zbss.code.generator.file.FileTypeEnum;
import com.zbss.code.generator.file.GenerateFile;
import com.zbss.code.generator.file.GenerateMapperFile;
import com.zbss.code.generator.plugins.Plugin;
import com.zbss.code.generator.util.FileUtils;
import com.zbss.code.generator.util.StringUtils;

import java.io.File;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 13:30 
 */
public class JavaMapperGenerator extends Generator {

    JSONObject mapperConfig;

    public JavaMapperGenerator(Config config) {
        super(config);
        mapperConfig = config.getConfig().getJSONObject("mapper");
    }

    @Override
    public void generateFile() throws Exception {
        String targetPkg = mapperConfig.getString("targetPackage");
        String targetPrj = mapperConfig.getString("targetProject");
        for (TableInfo tableInfo : config.getTableInfoList()) {
            CompilationUnit cu = new CompilationUnit();

            String pkg = conf.getJSONObject("model").getString("targetPackage");
            cu.setPackageDeclaration(pkg);

            String suffix = Const.MAPPER;
            String sf = mapperConfig.getJSONObject("properties").getString("suffix");
            suffix = StringUtils.isEmpty(sf) ? suffix : sf;
            String typeName = tableInfo.getDomainName() + suffix;
            tableInfo.setMapperName(typeName);
            cu.addInterface(typeName);

            GenerateFile<CompilationUnit> generateFile = new GenerateMapperFile<>();
            generateFile.setData(cu);
            generateFile.setName(tableInfo.getDomainName() + ".java");
            generateFile.setType(FileTypeEnum.MAPPER);
            generateFile.setTargetProject(targetPrj);
            generateFile.setTargetPackage(targetPkg);
            tableInfo.getGenerateFiles().add(generateFile);
        }
    }

    @Override
    public void mergeFile() {
        if (!conf.getBoolean("isJavaMerge")) {
            return;
        }
    }

    @Override
    public void writeFile() throws Exception {
        executeWrite(FileTypeEnum.MAPPER);
    }

    @Override
    public void executePlugin(Plugin plugin) {
        plugin.pluginJavaMapper(config.getTableInfoList());
    }
}
