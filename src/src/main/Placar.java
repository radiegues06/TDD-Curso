package main;

public class Placar {

    ArmazenamentoInterface armazenamento;

    public Placar() {
        this.armazenamento = new Armazenamento();
    }

    public void setArmazenamento(MockArmazenamento mock) {
        this.armazenamento = mock;
    }

    public void registraPontos(String userName, String pointType, String pointValue) {
        armazenamento.setUserPoints(userName, pointType, pointValue);
    }
}
