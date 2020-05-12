package main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Armazenamento {

    private final String CURR_DIR = System.getProperty("user.dir");
    private String filePath = new String();
    private Document document;
    private XPath xpath = XPathFactory.newInstance().newXPath();

    public void loadGameXMLFile(String filePath) throws ParserConfigurationException, IOException, SAXException {
        this.filePath = filePath;
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        document = builder.parse(new File(this.filePath));

        document.getDocumentElement().normalize();
    }

    public HashMap<String,String> filterByUser(String userName) {
        try {
            XPathExpression searchXPath = xpath.compile("/Usuarios/Usuario[contains(Nome,'" + userName + "')]/Pontos");

            NodeList nodeList = (NodeList) searchXPath.evaluate(document, XPathConstants.NODESET);

            return createPointTypesMap(nodeList);

        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private HashMap<String,String> createPointTypesMap(NodeList nodes) {
        HashMap<String,String> pointTypes = new HashMap<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Element pointTypeElement = (Element) nodes.item(i);
            pointTypes.put(getElementTagContent(pointTypeElement, "Tipo"),
                    getElementTagContent(pointTypeElement, "Valor"));
        }
        return pointTypes;
    }

    private String getElementTagContent(Element element, String tagName) {
        return element.getElementsByTagName(tagName).item(0).getTextContent();
    }

    public HashMap<String, String> filterByPointType(String pointType) {
        try {
            XPathExpression searchXPath = xpath.compile("/Usuarios/Usuario/Pontos[contains(Tipo,'" + pointType + "')]");

            NodeList nodes = (NodeList) searchXPath.evaluate(document, XPathConstants.NODESET);

            return createUsuariosPointTypeMap(nodes);

        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private HashMap<String, String> createUsuariosPointTypeMap(NodeList nodes) {
        HashMap<String, String> users = new HashMap<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            Element nameElement = (Element) element.getParentNode();
            users.put(getElementTagContent(nameElement, "Nome"),
                    getElementTagContent(element, "Valor"));
        }
        return users;
    }

    public String filterByUserAndPointType(String userName, String pointType) {
        try {
            XPathExpression searchXPath = xpath.compile("/Usuarios/Usuario[contains(Nome,'" + userName +
                    "')]/Pontos[contains(Tipo,'" + pointType + "')]");

            NodeList nodes = (NodeList) searchXPath.evaluate(document, XPathConstants.NODESET);
            if (nodes.getLength() > 0) {
                Element element = (Element) nodes.item(0);
                return getElementTagContent(element, "Valor");
            } else {
                return "";
            }
        } catch (XPathExpressionException e) {
            return "";
        }
    }

    public void setUserPoints(String userName, String pointType, String pointValue) {
        try {
            Element root = getRootElement();
            Element usuario = getUsuarioElement(userName);

            if (existUsersPointType(userName, pointType)) {
                setPointsNode(usuario, pointType, pointValue);
            } else {
                Element points = createPointsNode(pointType, pointValue);
                usuario.appendChild(points);
                root.appendChild(usuario);
            }
            writeXML(this.document);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean existUsersPointType(String userName, String pointType) {
        return filterByUserAndPointType(userName, pointType).length() > 0;
    }

    private void setPointsNode(Element usuario, String pointType, String pointValue) {
        NodeList nodeList = usuario.getElementsByTagName("Tipo");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element pointNode = (Element) nodeList.item(i);
            if (pointNode.getTextContent().equals(pointType)) {
                usuario.getElementsByTagName("Valor").item(i).setTextContent(pointValue);
            }
        }

                //.item(0).setTextContent(pointValue);
    }

    private Element getUsuarioElement(String userName) {
        try {
            XPathExpression searchXPath = xpath.compile("/Usuarios/Usuario[contains(Nome,'" + userName + "')]");

            NodeList nodes = (NodeList) searchXPath.evaluate(document, XPathConstants.NODESET);
            Element element = (Element) nodes.item(0);
            if (element == null)
                return createUsuarioElement(userName);
            return element;

        } catch (Exception e) {
            return null;
        }
    }

    private Element getRootElement() throws ParserConfigurationException {

        if (isXMLFileInicialized()) {
            return (Element) this.document.getElementsByTagName("Usuarios").item(0);
        } else {
            createNewDocument();
            return createRootElement();
        }
    }

    private void createNewDocument() throws ParserConfigurationException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document newDocument = builder.newDocument();
        this.document = newDocument;
    }

    private Element createRootElement() {
        Element root = document.createElement("Usuarios");
        document.appendChild(root);
        return root;
    }

    private Element createUsuarioElement(String userName) {
        Element usuario = document.createElement("Usuario");
        Element nome = document.createElement("Nome");
        nome.appendChild(document.createTextNode(userName));
        usuario.appendChild(nome);
        return usuario;
    }

    private Element createPointsNode(String pointType, String pointValue) {
        Element tipo = this.document.createElement("Tipo");
        tipo.appendChild(this.document.createTextNode(pointType));
        Element valor = this.document.createElement("Valor");
        valor.appendChild(this.document.createTextNode(pointValue));
//        valor.getChildNodes().item(0).setTextContent("Testando aquiiii");

        Element pontos = this.document.createElement("Pontos");
        pontos.appendChild(tipo);
        pontos.appendChild(valor);
        return pontos;
    }

    private void writeXML(Document document) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);

        if (filePath.isEmpty()) {
            String fileName = java.time.LocalDateTime.now().toString().
                    replace(":","_").replace(".","_");
            this.filePath = CURR_DIR + "\\games\\" + fileName + ".xml";
        }

        StreamResult streamResult = new StreamResult(new File(this.filePath));

        transformer.transform(domSource, streamResult);
    }

    private boolean isXMLFileInicialized() {
        return document != null;
    }

    public String getFilePath() {
        return this.filePath;
    }
}