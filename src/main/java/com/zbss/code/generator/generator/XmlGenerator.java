package com.zbss.code.generator.generator;

import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.plugins.Plugin;
import com.zbss.code.generator.table.TableInfo;
import com.zbss.code.generator.util.DocumentUtils;
import com.zbss.code.generator.util.ObjectUtils;
import org.dom4j.Document;

import java.io.File;

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
    public void generateFile() throws Exception {
        for (TableInfo tableInfo : config.getTableInfoList()) {
            Document document = DocumentUtils.createDocument();
            tableInfo.setXmlDocument(document);
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
        String targetPkg = conf.getJSONObject("xml").getString("targetPackage");
        String targetPrj = conf.getJSONObject("xml").getString("targetProject");
        File dir = getDirectory(targetPrj, targetPkg);

        for (TableInfo tableInfo : config.getTableInfoList()) {
            Document doc = tableInfo.getXmlDocument();
            if (ObjectUtils.isEmpty(doc)) {
                continue;
            }

            File targetFile = new File(dir, tableInfo.getMapperName() + ".xml");
            String fileEncoding = ObjectUtils.isEmpty(conf.getString("outputFileEncoding")) ? "utf-8" : conf.getString("outputFileEncoding");
            String fileContent = DocumentUtils.convertDocumentToStringWithFormat(doc);
            writeFile(targetFile, fileContent, fileEncoding);
        }
    }

    @Override
    public void executePlugin(Plugin plugin) {
        plugin.pluginXml(config.getTableInfoList());
    }
}
