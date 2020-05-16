package test;

import main.MockArmazenamento;
import main.Placar;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

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
        assertEquals("100", armazenamento.filterByUserAndPointType("Rafael", "Estrelas"));
    }

    @Test
    public void whenRegistraVariosUsuariosThenPassaInformacaoParaArmazenamento() {
        placar.registraPontos("Rafael", "Fichas", "3000");
        placar.registraPontos("Isabela", "Estrelas", "150");
        placar.registraPontos("Júlia", "Curtidas", "18");

        assertEquals("3000", armazenamento.filterByUserAndPointType("Rafael", "Fichas"));
        assertEquals("150", armazenamento.filterByUserAndPointType("Isabela", "Estrelas"));
        assertEquals("18", armazenamento.filterByUserAndPointType("Júlia", "Curtidas"));
    }

    @Test
    public void whenRegistraValorEAtualizaThenPassaInformacaoParaArmazenamento() {
        placar.registraPontos("Rafael", "Fichas", "3000");
        placar.registraPontos("Rafael", "Estrelas", "15");
        placar.registraPontos("Rafael", "Fichas", "1000");

        assertEquals("1000", armazenamento.filterByUserAndPointType("Rafael", "Fichas"));
    }

    @Test
    public void whenGetUserPointsThenRetornaTodosOsPontosValidosDoUsuario() {
        placar.registraPontos("Rafael", "Estrelas", "15");
        placar.registraPontos("Rafael", "Curtidas", "0");
        placar.registraPontos("Rafael", "Vidas", "7");
        placar.registraPontos("Rafael", "Fichas", "1500");
        placar.registraPontos("Júlia", "Comentários", "30");
        placar.registraPontos("Júlia", "Curtidas", "13");

        assertEquals("15", placar.getUserPoints("Rafael").get("Estrelas"));
        assertEquals(null, placar.getUserPoints("Rafael").get("Curtidas"));
    }

    @Test
    public void whenGetRankingThenRetornaListaOrdenada() {
        placar.registraPontos("Rafael", "Estrelas", "17");
        placar.registraPontos("Júlia", "Estrelas", "55");
        placar.registraPontos("Isabela", "Estrelas", "25");
        placar.registraPontos("Abner", "Estrelas", "19");
        placar.registraPontos("Raphael", "Estrelas", "32");

        LinkedList<String> usersList =  placar.getPointsRanking("Estrelas");

        assertEquals("Júlia", usersList.get(0));
        assertEquals("Raphael", usersList.get(1));
        assertEquals("Isabela", usersList.get(2));
    }

}
