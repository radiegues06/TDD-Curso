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

    public void setUserPoints(String userName, String pointType, String pointValue) {
        try {

            Element root = getRootElement();
            Element usuario = getUsuarioElement(userName);

            if (filterByUserAndPointType(userName, pointType).length() > 0) {
                setPointsNode(usuario, pointValue);
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

    private void setPointsNode(Element usuario, String pointValue) {
        usuario.getElementsByTagName("Valor").item(0).setTextContent(pointValue);
    }

    private Element getUsuarioElement(String userName) {
        try {
            XPathExpression searchXPath = xpath.compile("/Usuarios/Usuario[contains(Nome,'" + userName + "')]");

            NodeList nodes = (NodeList) searchXPath.evaluate(document, XPathConstants.NODESET);
            Element element = (Element) nodes.item(0);
            if (element == null)
                throw new Exception();
            return element;

        } catch (Exception e) {
            return createUsuarioElement(userName);
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