package main;

import java.util.HashMap;
import java.util.List;

public class MockArmazenamento implements ArmazenamentoInterface {

    HashMap<String, HashMap<String,String>> users = new HashMap<>();

    @Override
    public HashMap<String, String> filterByUser(String userName) {
        return users.get(userName);
    }

    @Override
    public HashMap<String, String> filterByPointType(String pointType) {
        return null;
    }

    @Override
    public String filterByUserAndPointType(String userName, String pointType) {
        return null;
    }

    @Override
    public void setUserPoints(String userName, String pointType, String pointValue) {
        HashMap<String,String> points = users.containsKey(userName)? users.get(userName) : new HashMap<>();
        points.put(pointType, pointValue);
        users.put(userName, points);
    }

}
