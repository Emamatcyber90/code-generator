package com.zbss.code.generator.generator;

import com.alibaba.fastjson.JSONObject;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.entity.TableInfo;
import com.zbss.code.generator.file.FileTypeEnum;
import com.zbss.code.generator.file.GenerateFile;
import com.zbss.code.generator.plugins.Plugin;
import com.zbss.code.generator.util.FileUtils;
import com.zbss.code.generator.util.ObjectUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 13:33
 */
public class Generator {

    Config config;
    JSONObject jsonConfig;

    public Generator(Config config) {
        this.config = config;
        jsonConfig = config.getJsonConfig();
    }

    public void generate() throws Exception {
        generateFile();
        pluginFile();
        mergeFile();
        writeFile();
    }

    private void generateFile() throws Exception {
        List<FileGenerator> generatorList = config.getGeneratorList();
        if (ObjectUtils.isNotEmpty(generatorList)) {
            System.out.println("===== execute generator start =====");
            for (FileGenerator fileGenerator : generatorList) {
                System.out.println("\t" + fileGenerator.getClass().getSimpleName());
                fileGenerator.generateFile();
            }
            System.out.println("===== execute generator end =====");
        }
    }

    private void pluginFile() throws Exception {
        List<Plugin> pluginList = config.getPluginList();
        if (ObjectUtils.isNotEmpty(pluginList)) {
            System.out.println("===== execute plugin start =====");
            for (Plugin plugin : pluginList) {
                System.out.println("\t" + plugin.getClass().getSimpleName());
                plugin.plugin();
            }
            System.out.println("===== execute plugin end =====");
        }
    }

    private void mergeFile() throws Exception {
        List<TableInfo> tableInfoList = config.getTableInfoList();
        Boolean isJavaMerge = jsonConfig.getBoolean("isJavaMerge");
        Boolean isXmlMerge = jsonConfig.getBoolean("isXmlMerge");
        if (ObjectUtils.isNotEmpty(tableInfoList)) {
            for (TableInfo tableInfo : config.getTableInfoList()) {
                List<GenerateFile<?>> generateFileList = tableInfo.getGenerateFiles();
                for (GenerateFile<?> generateFile : generateFileList) {
                    File oldFile = new File(FileUtils.getDirectory(generateFile.getTargetProject(), generateFile.getTargetPackage()), generateFile.getName());
                    if (oldFile.exists()) {
                        if (FileTypeEnum.FLAG_JAVA.equals(generateFile.getFlag()) && isJavaMerge) {
                            mergeJavaFile(generateFile, oldFile);
                        }
                        if (FileTypeEnum.FLAG_XML.equals(generateFile.getFlag()) && isXmlMerge) {
                            mergeXmlFile(generateFile, oldFile);
                        }
                    }
                }
            }
        }
    }

    private void mergeJavaFile(GenerateFile<?> generateFile, File oldFile) throws FileNotFoundException {
        CompilationUnit newCu = (CompilationUnit) generateFile.getData();
        CompilationUnit oldCu = JavaParser.parse(oldFile);
        String content = config.getMerger().mergeJavaFile(newCu, oldCu);
        if (ObjectUtils.isNotEmpty(content)) {
            generateFile.setContent(content);
        }
    }

    private void mergeXmlFile(GenerateFile<?> generateFile, File oldFile) throws Exception {
        Document newDoc = (Document) generateFile.getData();
        Document oldDoc = DocumentHelper.parseText(FileUtils.readFile(oldFile));
        String content = config.getMerger().mergeXmlFile(newDoc, oldDoc);
        if (ObjectUtils.isNotEmpty(content)) {
            generateFile.setContent(content);
        }
    }

    private void writeFile() throws Exception {
        String fileEncoding = ObjectUtils.isEmpty(jsonConfig.getString("outputFileEncoding")) ? "utf-8" : jsonConfig.getString("outputFileEncoding");
        List<TableInfo> tableInfoList = config.getTableInfoList();
        if (ObjectUtils.isNotEmpty(tableInfoList)) {
            for (TableInfo tableInfo : config.getTableInfoList()) {
                List<GenerateFile<?>> generateFileList = tableInfo.getGenerateFiles();
                for (GenerateFile<?> generateFile : generateFileList) {
                    File file = new File(FileUtils.getDirectory(generateFile.getTargetProject(), generateFile.getTargetPackage()), generateFile.getName());
                    FileUtils.writeFile(file, generateFile.getContent(), fileEncoding);
                }
            }
        }
    }

}
