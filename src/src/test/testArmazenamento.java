package test;

import main.Armazenamento;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import static org.junit.Assert.fail;

public class testArmazenamento {

    String CURR_DIR = System.getProperty("user.dir");
    Armazenamento armazenamento;

    @Before
    public void iniciaArmazenamento() {
        this.armazenamento = new Armazenamento();
    }

    @Test(expected = IOException.class)
    public void whenArquivoInexistenteThenIOException() throws IOException, SAXException, ParserConfigurationException {
        armazenamento.load("invalidPath");
        fail();
    }

    @Test(expected = SAXException.class)
    public void whenArquivoComFormatoIncorretoThenSAXEception() throws IOException, SAXException, ParserConfigurationException {
        armazenamento.load(CURR_DIR + "\\resources\\formatoIncorreto.xml");
    }

    @Test
    public void whenArquivoValidoThenSemErros() {
        try {
            armazenamento.load(CURR_DIR + "\\resources\\arquivoComUmUsuario.xml");
        } catch (Exception e) {
            fail();
        }
    }
}
