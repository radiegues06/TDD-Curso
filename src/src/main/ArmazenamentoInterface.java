package main;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;

public interface ArmazenamentoInterface {
    void loadGameXMLFile(String filePath) throws ParserConfigurationException, IOException, SAXException;

    HashMap<String,String> filterByUser(String userName);

    HashMap<String, String> filterByPointType(String pointType);

    String filterByUserAndPointType(String userName, String pointType);

    void setUserPoints(String userName, String pointType, String pointValue);

    String getFilePath();
}
