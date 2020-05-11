package test;

import main.Usuario;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class testUsuario {

    Usuario usuario;

    @Before
    public void inicializa() {
        this.usuario = new Usuario("Rafael");
    }
    @Test
    public void whenSetarNomeUsuarioThenRetornaNome() {
        assertEquals("Rafael", usuario.getNome());
    }

    @Test
    public void whenTentaSetarNomeInvalidoThenNaoMudaNome() {
        usuario.changeNome(null);
        assertEquals("Rafael", usuario.getNome());

    }

    @Test
    public void whenSetaNomeDuasVezesThenRetornaUltimoNome() {
        usuario.changeNome("Júlia");
        assertEquals("Júlia", usuario.getNome());
    }

    @Test
    public void whenPointTypeNullThenNullPoints() {
        assertEquals(null, usuario.getPoint("Estrela"));
    }

    @Test
    public void whenSetSinglePointTypeThenReturnPointType() {
        usuario.setPoint("Estrela", "10");
        assertEquals("10", usuario.getPoint("Estrela"));
    }

    @Test
    public void whenSetMuliplePointTypeThenReturnPointType() {
        usuario.setPoint("Estrela", "10");
        usuario.setPoint("Moedas", "305");
        usuario.setPoint("Curtidas", "1001");
        assertEquals("10", usuario.getPoint("Estrela"));
        assertEquals("305", usuario.getPoint("Moedas"));
        assertEquals("1001", usuario.getPoint("Curtidas"));
    }

    @Test
    public void whenAtualizaPointTypeThenRetornaUltimoValor() {
        usuario.setPoint("Estrela", "100");
        usuario.setPoint("Estrela", "200");
        assertEquals("200", usuario.getPoint("Estrela"));
    }
}