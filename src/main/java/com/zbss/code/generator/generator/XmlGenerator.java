package com.zbss.code.generator.generator;

import com.alibaba.fastjson.JSONObject;
import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.entity.TableInfo;
import com.zbss.code.generator.file.FileTypeEnum;
import com.zbss.code.generator.file.GenerateFile;
import com.zbss.code.generator.file.GenerateXmlFile;
import com.zbss.code.generator.plugins.Plugin;
import com.zbss.code.generator.util.DocumentUtils;
import org.dom4j.Document;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 13:31
 */
public class XmlGenerator extends Generator {

    JSONObject xmlConfig;

    public XmlGenerator(Config config) {
        super(config);
        xmlConfig = config.getConfig().getJSONObject("xml");
    }

    @Override
    public void generateFile() throws Exception {
        String targetPkg = xmlConfig.getString("targetPackage");
        String targetPrj = xmlConfig.getString("targetProject");
        for (TableInfo tableInfo : config.getTableInfoList()) {
            Document document = DocumentUtils.createDocument();
            GenerateFile<Document> generateFile = new GenerateXmlFile<>();
            generateFile.setTargetProject(targetPrj);
            generateFile.setTargetPackage(targetPkg);
            generateFile.setData(document);
            generateFile.setType(FileTypeEnum.XML);
            generateFile.setName(tableInfo.getMapperName() + ".xml");
            tableInfo.getGenerateFiles().add(generateFile);
        }
    }

    @Override
    public void mergeFile() throws Exception {
        if (!conf.getBoolean("isXmlMerge")) {
            return;
        }
    }

    @Override
    public void writeFile() throws Exception {
        executeWrite(FileTypeEnum.XML);
    }

    @Override
    public void executePlugin(Plugin plugin) {
        plugin.pluginXml(config.getTableInfoList());
    }
}
