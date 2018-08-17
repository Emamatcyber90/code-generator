package com.zbss.code.generator.generator;

import com.zbss.code.generator.entity.TableInfo;
import com.zbss.code.generator.file.FileTypeEnum;
import com.zbss.code.generator.file.GenerateFile;
import com.zbss.code.generator.file.GenerateXmlFile;
import com.zbss.code.generator.util.DocumentUtils;
import org.dom4j.Document;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 13:31
 */
public class XmlGenerator extends FileGenerator {

    @Override
    public void generateFile() throws Exception {
        String targetPkg = config.getJsonConfig().getJSONObject("xml").getString("targetPackage");
        String targetPrj = config.getJsonConfig().getJSONObject("xml").getString("targetProject");
        for (TableInfo tableInfo : config.getTableInfoList()) {
            Document document = DocumentUtils.createDocument();
            GenerateFile<Document> generateFile = new GenerateXmlFile<>();
            generateFile.setTargetProject(targetPrj);
            generateFile.setTargetPackage(targetPkg);
            generateFile.setData(document);
            generateFile.setType(FileTypeEnum.XML);
            generateFile.setFlag(FileTypeEnum.FLAG_XML);
            generateFile.setName(tableInfo.getMapperName() + ".xml");
            tableInfo.getGenerateFiles().add(generateFile);
        }
    }
}
