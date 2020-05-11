package main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Armazenamento {

    Document document;

    XPath xpath = XPathFactory.newInstance().newXPath();

    public void loadGameXMLFile(String filePath) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        document = builder.parse(new File(filePath));

        //Normalize the XML Structure; It's just too important !!
        document.getDocumentElement().normalize();

    }

    public HashMap<String,String> filterByUser(String userName) {
        HashMap<String,String> userPoints = new HashMap<>();
        try {
            XPathExpression searchXPath = xpath.compile("/Usuarios/Usuario[contains(Nome,'" + userName + "')]/Pontos");

            NodeList nodes = (NodeList) searchXPath.evaluate(document, XPathConstants.NODESET);

            parsePointTypesForUser(userPoints, nodes);

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return userPoints;
    }

    private void parsePointTypesForUser(HashMap<String, String> pointTypes, NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); i++) {
            Element pointTypeElement = (Element) nodes.item(i);
            pointTypes.put(getElementTagContent(pointTypeElement, "Tipo"),
                    getElementTagContent(pointTypeElement, "Valor"));
        }
    }

    private String getElementTagContent(Element element, String tagName) {
        return element.getElementsByTagName(tagName).item(0).getTextContent();
    }

    public HashMap<String, String> filterByPointType(String pointType) {
        HashMap<String,String> users = new HashMap<>();
        try {
            XPathExpression searchXPath = xpath.compile("/Usuarios/Usuario/Pontos[contains(Tipo,'" + pointType + "')]");

            NodeList nodes = (NodeList) searchXPath.evaluate(document, XPathConstants.NODESET);

            setUsuariosForPointType(users, nodes);

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return users;
    }

    private void setUsuariosForPointType(HashMap<String, String> users, NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            Element nameElement = (Element) element.getParentNode();
            users.put(getElementTagContent(nameElement, "Nome"),
                    getElementTagContent(element, "Valor"));
        }
    }

    public String filterByUserAndPointType(String userName, String pointType) {
        String value = new String();
        try {
            XPathExpression searchXPath = xpath.compile("/Usuarios/Usuario[contains(Nome,'" + userName +
                    "')]/Pontos[contains(Tipo,'" + pointType + "')]");

            NodeList nodes = (NodeList) searchXPath.evaluate(document, XPathConstants.NODESET);
            if (nodes.getLength() > 0) {
                Element element = (Element) nodes.item(0);
                value = getElementTagContent(element, "Valor");
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return value;
    }
}