package com.zbss.code.generator.plugins;

import com.zbss.code.generator.config.Const;
import com.zbss.code.generator.table.TableColumn;
import com.zbss.code.generator.table.TableInfo;
import com.zbss.code.generator.util.ObjectUtils;
import com.zbss.code.generator.util.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.tree.DefaultDocumentType;

import java.util.List;

/**
 * @author zbss
 * @desc
 * @date 2018/8/16 16:19
 */
public class MybatisXmlGeneratePlugin extends PluginAdapter {

    @Override
    public void pluginXml(List<TableInfo> tableInfoList) {
        for (TableInfo tableInfo : tableInfoList) {
            createXmlDocument(tableInfo);
        }
    }

    private void createXmlDocument(TableInfo tableInfo) {
        DocumentType docType = new DefaultDocumentType();
        docType.setElementName("mapper");
        docType.setPublicID("-//mybatis.org//DTD Mapper 3.0//EN");
        docType.setSystemID(" http://mybatis.org/dtd/mybatis-3-mapper.dtd");
        Document document = tableInfo.getXmlDocument();
        document.setDocType(docType);
        if (ObjectUtils.isEmpty(document)) {
            return;
        }

        String pkg = tableInfo.getModelConfig().getString("targetPackage");

        Element mapperEle = document.addElement("mapper");
        mapperEle.addAttribute("namespace", pkg + "." + tableInfo.getMapperName());
        Element resultMapEle = mapperEle.addElement("resultMap");
        resultMapEle.addAttribute("id", tableInfo.getDomainName() + "Map");
        resultMapEle.addAttribute("type", pkg + "." + tableInfo.getDomainName());
        resultMapEle.addComment(Const.XML_COMMENT);

        String primaryKey = tableInfo.getPrimaryKey();
        String columns = "";
        List<TableColumn> columnList = tableInfo.getActualColumns();
        for (TableColumn tableColumn : columnList) {
            Element ele = null;
            if (primaryKey.equals(tableColumn.getColumnActualName())) {
                ele = resultMapEle.addElement("id");
            } else {
                ele = resultMapEle.addElement("result");
            }
            ele.addAttribute("column", tableColumn.getColumnActualName());
            ele.addAttribute("property", tableColumn.getColumnFieldName());
            ele.addAttribute("jdbcType", tableColumn.getColumnType().split(" ")[0]);
            columns += tableColumn.getColumnActualName() + ",";
        }

        columns = StringUtils.isEmpty(columns) ? "" : columns.substring(0, columns.length() - 1);

        Element sqlEle = mapperEle.addElement("sql");
        sqlEle.addAttribute("id", "column_list");
        sqlEle.addComment(Const.XML_COMMENT);
        sqlEle.addText(System.lineSeparator());
        sqlEle.addText("        " + columns);
    }
}
