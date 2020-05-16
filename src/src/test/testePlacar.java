package test;

import main.ArmazenamentoInterface;
import main.MockArmazenamento;
import main.Placar;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class testePlacar {

    MockArmazenamento armazenamento;
    Placar placar;

    @Before
    public void setArmazenamentoMock() {
        placar = new Placar();
        this.armazenamento = new MockArmazenamento();
        placar.setArmazenamento(this.armazenamento);
    }

    @Test
    public void whenRegistraUnicoUsuarioThenPassaInformacaoParaArmazenamento() {
        placar.registraPontos("Rafael", "Estrelas", "100");
        // Mock test
        assertEquals("100", armazenamento.filterByUser("Rafael").get("Estrelas"));
    }

}
