package test;

import main.Armazenamento;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class testArmazenamento {

    String CURR_DIR = System.getProperty("user.dir");
    Armazenamento armazenamento;

    private static void deleteFile(String filePath) {
        File file = new File(filePath);
        file.delete();
    }

    private static int getWordCount(String filePath, String word) {
        File file = new File(filePath);
        int wordCount = 0;
        Pattern p = Pattern.compile(word);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher m = p.matcher(line);
                while (m.find())
                    wordCount++;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordCount;
    }

    @Before
    public void iniciaArmazenamento() {
        this.armazenamento = new Armazenamento();
    }

    @Test(expected = IOException.class)
    public void whenArquivoInexistenteThenIOException() throws IOException, SAXException, ParserConfigurationException {
        armazenamento.loadGameXMLFile("invalidPath");
        fail();
    }

    @Test(expected = SAXException.class)
    public void whenArquivoComFormatoIncorretoThenSAXEception() throws IOException, SAXException, ParserConfigurationException {
        armazenamento.loadGameXMLFile(CURR_DIR + "\\resources\\formatoIncorreto.xml");
    }

    @Test
    public void whenArquivoValidoThenSemErros() {
        try {
            armazenamento.loadGameXMLFile(CURR_DIR + "\\resources\\arquivoComUmUsuario.xml");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            fail();
        }
    }

    @Test
    public void whenFiltraPorNomeDoUsuarioInexistenteThenRetornaVazio() {
        try {
            armazenamento.loadGameXMLFile(CURR_DIR + "\\resources\\arquivoComUmUsuario.xml");
            HashMap<String,String> pontos = armazenamento.filterByUser("invalido");
            assertEquals(null, pontos.get("Moedas"));
            assertEquals(null, pontos.get("Curtidas"));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            fail();
        }
    }

    @Test
    public void whenFiltraPorNomeDoUsuarioThenRetornaPontosDoUsuario() {
        try {
            armazenamento.loadGameXMLFile(CURR_DIR + "\\resources\\arquivoComUmUsuario.xml");
            HashMap<String,String> pontos = armazenamento.filterByUser("Rafael");
            assertEquals("15", pontos.get("Moedas"));
            assertEquals("110", pontos.get("Curtidas"));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            fail();
        }
    }

    @Test
    public void whenFiltraPorNomeDoUsuarioComMaisDeUmUsuarioThenRetornaPontosDoUsuario() {
        try {
            armazenamento.loadGameXMLFile(CURR_DIR + "\\resources\\arquivoComDoisUsuarios.xml");
            HashMap<String,String> pontos1 = armazenamento.filterByUser("Rafael");
            assertEquals("15", pontos1.get("Moedas"));
            assertEquals("110", pontos1.get("Curtidas"));

            HashMap<String,String> pontos2 = armazenamento.filterByUser("Isabela");
            assertEquals("3000", pontos2.get("Fichas"));
            assertEquals("32", pontos2.get("Comentários"));
            assertEquals("101", pontos2.get("Estrelas"));

        } catch (ParserConfigurationException | IOException | SAXException e) {
            fail();
        }
    }

    @Test
    public void whenFiltraPorTipoDePontoThenRerornaUsuarioEPontos() {
        try {
            armazenamento.loadGameXMLFile(CURR_DIR + "\\resources\\arquivoComUmUsuario.xml");
            HashMap<String,String> usuarios = armazenamento.filterByPointType("Moedas");
            assertEquals("15", usuarios.get("Rafael"));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            fail();
        }
    }

    @Test
    public void whenFiltraPorTipoDePontoThenRerornaUsuariosEPontos() {
        try {
            armazenamento.loadGameXMLFile(CURR_DIR + "\\resources\\arquivoComUsuariosEListaDeTipoDePontos.xml");
            HashMap<String,String> usuarios = armazenamento.filterByPointType("Moedas");
            assertEquals("15", usuarios.get("Rafael"));
            assertEquals("32", usuarios.get("Isabela"));
            assertEquals("12", usuarios.get("Júlia"));
            assertEquals(null, usuarios.get("Abner"));
            assertEquals("120", usuarios.get("Raphael"));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            fail();
        }
    }

    @Test
    public void whenFiltraPorUsuarioETipoDePontoThenRetornaPontos() {
        try {
            armazenamento.loadGameXMLFile(CURR_DIR + "\\resources\\arquivoComUsuariosEListaDeTipoDePontos.xml");
            assertEquals("12", armazenamento.filterByUserAndPointType("Júlia", "Estrelas"));
            assertEquals("", armazenamento.filterByUserAndPointType("Isabela", "Curtidas"));
            assertEquals("120", armazenamento.filterByUserAndPointType("Raphael", "Moedas"));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            fail();
        }
    }

    @Test
    public void whenNaoCarregaXMLESetaValorParaUnicoUsuarioThenCriaXMLERetornaValorArmazenado() {
        armazenamento.setUserPoints("Rafael","Estrela","10");
        assertEquals("10", armazenamento.filterByUserAndPointType("Rafael", "Estrela"));
        assertEquals("10", armazenamento.filterByUser("Rafael").get("Estrela"));
        assertEquals("10", armazenamento.filterByPointType("Estrela").get("Rafael"));
        deleteFile(armazenamento.getFilePath());
    }

    @Test
    public void whenNaoCarregaXMLESetaValorParaNovoUsuarioThenEscrevePontosNoXML() {
        armazenamento.setUserPoints("Rafael", "Estrela", "20");
        armazenamento.setUserPoints("Júlia", "Fichas", "5000");
        assertEquals("20", armazenamento.filterByUserAndPointType("Rafael", "Estrela"));
        assertEquals("5000", armazenamento.filterByUserAndPointType("Júlia", "Fichas"));
        deleteFile(armazenamento.getFilePath());
    }

    @Test
    public void whenNaoCarregaXMLESetaValorParaUsuarioJaExistenteThenEscrevePontosNoXMLNaMesmaTagDeXML() {
        armazenamento.setUserPoints("Júlia", "Fichas", "5000");
        armazenamento.setUserPoints("Júlia", "Moedas", "150");
        armazenamento.setUserPoints("Júlia", "Curtidas", "30");
        armazenamento.setUserPoints("Júlia", "Estrelas", "10");

        assertEquals("5000", armazenamento.filterByUser("Júlia").get("Fichas"));
        assertEquals("150", armazenamento.filterByUser("Júlia").get("Moedas"));

        assertEquals(1, getWordCount(armazenamento.getFilePath(), "Júlia"));
//        deleteFile(armazenamento.getFilePath());
    }
}
