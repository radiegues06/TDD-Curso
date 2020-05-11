package main;

import java.util.HashMap;

public class Usuario {

    String nome;
    HashMap<String,String> points = new HashMap<>();

    public Usuario(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void changeNome(String nome) {
        if (isValidNome(nome))
            this.nome = nome;
    }

    private boolean isValidNome(String nome) {
        return !(nome == null || nome.isEmpty());
    }

    public String getPoint(String pointType) {
        return points.get(pointType);
    }

    public void setPoint(String pointType, String pointValue) {
        points.put(pointType, pointValue);
    }
}
