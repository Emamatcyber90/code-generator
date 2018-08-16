package com.zbss.code.generator.util;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.DocumentType;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @Desc
 * @Auther zbss
 * @Date 2017-09-21 12:37
 */
public class DocumentUtils {

    public static Document createDocument(DocumentType documentType) {
        Document document = DocumentHelper.createDocument();
        if (documentType != null) {
            document.setDocType(documentType);
        }
        return document;
    }

    public static Document createDocument() {
        return createDocument(null);
    }

    public static String convertDocumentToString(Document document) {
        return document.asXML();
    }

    public static String convertDocumentToStringWithFormat(Document document) {
        OutputFormat format = new OutputFormat();
        format.setEncoding("UTF-8");
        format.setIndentSize(4);
        format.setNewlines(true);
        format.setNewLineAfterDeclaration(true);
        format.setPadText(true);
        try {
            StringWriter out = new StringWriter();
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(document);
            writer.flush();
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException("IOException while generating textual representation: " + e.getMessage());
        }
    }

}
