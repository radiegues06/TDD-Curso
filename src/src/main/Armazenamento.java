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

    public void load(String filePath) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        document = builder.parse(new File(filePath));

        //Normalize the XML Structure; It's just too important !!
        document.getDocumentElement().normalize();

    }

    public HashMap<String,String> filterByUser(String nomeUsuario) {
        HashMap<String,String> pontosDoUsuario = new HashMap<String,String>();
        try {
            XPathExpression searchXPath = xpath.compile("/Usuarios/Usuario[contains(Nome,'" + nomeUsuario + "')]/Pontos");

            NodeList nodes = (NodeList) searchXPath.evaluate(document, XPathConstants.NODESET);

            setPontosInNode(pontosDoUsuario, nodes);

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return pontosDoUsuario;
    }

    private void setPontosInNode(HashMap<String, String> pontos, NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            pontos.put(getTagContent(element, "Tipo"),
                    getTagContent(element, "Valor"));
        }
    }

    private String getTagContent(Element element, String tagName) {
        return element.getElementsByTagName(tagName).item(0).getTextContent();
    }
}
