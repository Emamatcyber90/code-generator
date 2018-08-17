package com.zbss.code.generator.generator;

import com.github.javaparser.ast.CompilationUnit;
import com.zbss.code.generator.config.Const;
import com.zbss.code.generator.entity.TableInfo;
import com.zbss.code.generator.file.FileTypeEnum;
import com.zbss.code.generator.file.GenerateFile;
import com.zbss.code.generator.file.GenerateMapperFile;
import com.zbss.code.generator.util.StringUtils;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 13:30 
 */
public class MapperGenerator extends FileGenerator {

    @Override
    public void generateFile() throws Exception {
        String targetPkg = config.getJsonConfig().getJSONObject("mapper").getString("targetPackage");
        String targetPrj = config.getJsonConfig().getJSONObject("mapper").getString("targetProject");
        for (TableInfo tableInfo : config.getTableInfoList()) {
            CompilationUnit cu = new CompilationUnit();

            String pkg = jsonConfig.getJSONObject("model").getString("targetPackage");
            cu.setPackageDeclaration(pkg);

            String sf = config.getJsonConfig().getJSONObject("mapper").getJSONObject("properties").getString("suffix");
            String suffix = StringUtils.isEmpty(sf) ? Const.MAPPER : sf;
            String typeName = tableInfo.getDomainName() + suffix;
            tableInfo.setMapperName(typeName);
            cu.addInterface(typeName);

            GenerateFile<CompilationUnit> generateFile = new GenerateMapperFile<>();
            generateFile.setData(cu);
            generateFile.setName(tableInfo.getMapperName() + ".java");
            generateFile.setType(FileTypeEnum.MAPPER);
            generateFile.setFlag(FileTypeEnum.FLAG_JAVA);
            generateFile.setTargetProject(targetPrj);
            generateFile.setTargetPackage(targetPkg);
            tableInfo.getGenerateFiles().add(generateFile);
        }
    }
}
