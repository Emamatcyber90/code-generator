package com.zbss.code.generator.file;

import com.zbss.code.generator.util.DocumentUtils;
import org.dom4j.Document;

public class GenerateXmlFile<T> extends GenerateFile {
    @Override
    String convertDataToContent(Object data) {
        Document doc = (Document) data;
        return DocumentUtils.convertDocumentToStringWithFormat(doc);
    }
}
