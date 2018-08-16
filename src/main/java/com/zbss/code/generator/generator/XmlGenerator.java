package com.zbss.code.generator.generator;

import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.plugins.Plugin;
import com.zbss.code.generator.table.TableInfo;
import com.zbss.code.generator.util.DocumentUtils;
import com.zbss.code.generator.util.FileUtils;
import com.zbss.code.generator.util.ObjectUtils;
import com.zbss.code.generator.util.PathUtils;
import org.dom4j.Document;

import java.io.IOException;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 13:31
 */
public class XmlGenerator extends Generator {

    public XmlGenerator(Config config) {
        super(config);
    }

    @Override
    public void generateFile() {
        for (TableInfo tableInfo : config.getTableInfoList()) {
            Document document = DocumentUtils.createDocument();
            tableInfo.setXmlDocument(document);
        }
    }

    @Override
    public void mergeFile() {
        if (!conf.getBoolean("isXmlMerge")) {
            return;
        }
    }

    @Override
    public void writeFile() {
        for (TableInfo tableInfo : config.getTableInfoList()) {
            Document doc = tableInfo.getXmlDocument();
            if (ObjectUtils.isEmpty(doc)) {
                continue;
            }

            String xmlPath = tableInfo.getXmlConfig().getString("targetPackage");
            String filePath = PathUtils.getClassPath() + xmlPath.replaceAll("\\.", "\\/") + "/" + tableInfo.getMapperName() + ".xml";
            String fileContent = null;
            try {
                fileContent = DocumentUtils.convertDocumentToStringWithFormat(doc);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        plugin.pluginXml(config.getTableInfoList());
    }
}
