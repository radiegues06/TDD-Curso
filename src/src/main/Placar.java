package main;

import java.util.HashMap;
import java.util.Map;

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

    public HashMap<String,String> getUserPoints(String userName) {
        return getValidHashMapValues(armazenamento.filterByUser(userName));
    }

    private HashMap<String,String> getValidHashMapValues(HashMap<String,String> inMap) {
        HashMap<String,String> outMap = new HashMap<>();
        for (Map.Entry<String, String> entry : inMap.entrySet()) {

           if (isValidEntry(entry)) {
               outMap.put(entry.getKey(), entry.getValue());
           }
        }

        return outMap;
    }

    private boolean isValidEntry(Map.Entry<String, String> entry) {
        return !entry.getValue().equals("0");
    }

}
