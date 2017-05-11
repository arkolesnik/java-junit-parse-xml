package com.example;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;

public class XmlParser {

    public static final String RELATIVE_PATH = "structure.xml";
    public static final String XPATH_CLASSES = "//class";
    public static final String XPATH_METHODS = "//class[@name ='%s']/methods/include/@name";
    public static final String ATTRIBUTE = "name";

    public static void main(String arg[]) {
        run();
    }

    public static void run() {
        try {
            Document document = getXmlFile(RELATIVE_PATH);
            NodeList highierNodes = getNodesByXpath(XPATH_CLASSES, document);
            JUnitCore jUnitCore = new JUnitCore();
            for (int i = 0; i < highierNodes.getLength(); i++) {
                Node node = highierNodes.item(i).getAttributes().getNamedItem(ATTRIBUTE);
                String className = node.getNodeValue();
                Class classToRun = Class.forName(className);

                NodeList lowerNodes = getNodesByXpath(String.format(XPATH_METHODS, className), document);
                runSpecifiedTests(lowerNodes, jUnitCore, classToRun);
            }
        } catch (
                Exception e)

        {
            e.printStackTrace();
        }
    }

    private static Document getXmlFile(String path) throws ParserConfigurationException,
            IOException,
            SAXException {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document document = builder.parse(path);
        document.getDocumentElement().normalize();
        return document;
    }

    private static NodeList getNodesByXpath(String xpath, Document document) throws XPathExpressionException {
        XPath xpathObject = XPathFactory.newInstance().newXPath();
        XPathExpression xPathExpression = xpathObject.compile(xpath);
        Object result = xPathExpression.evaluate(document, XPathConstants.NODESET);
        return (NodeList) result;
    }

    private static void runSpecifiedTests(NodeList nodes, JUnitCore jUnitCore, Class clazz) {
        if (nodes.getLength() > 0) {
            for (int j = 0; j < nodes.getLength(); j++) {
                String methodName =nodes.item(j).getNodeValue();
                jUnitCore.run(Request.method(clazz, methodName));
            }
        } else {
            jUnitCore.run(Request.aClass(clazz));
        }
    }
}
