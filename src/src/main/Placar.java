package main;

import java.util.*;

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
        HashMap<String,String> validMap = new HashMap<>();
        for (Map.Entry<String, String> entry : inMap.entrySet()) {
           if (isValidEntry(entry)) {
               validMap.put(entry.getKey(), entry.getValue());
           }
        }

        return validMap;
    }

    private boolean isValidEntry(Map.Entry<String, String> entry) {
        return !entry.getValue().equals("0");
    }

    public LinkedList<String> getPointRanking(String pointType) {
        LinkedList<String> users = new LinkedList<>();
        List<Integer> values = new ArrayList<>();

        for (Map.Entry<String, String> entry :
                armazenamento.filterByPointType(pointType).entrySet()) {

            values.add(Integer.parseInt(entry.getValue()));
        }

        Collections.sort(values, Collections.reverseOrder());

        for (Integer value : values) {
            for (Map.Entry<String, String> entry : armazenamento.filterByPointType(pointType).entrySet()) {
                if (value.toString().equals(entry.getValue())) {
                    users.add(entry.getKey());
                }
            }
        }
        return users;
    }
}