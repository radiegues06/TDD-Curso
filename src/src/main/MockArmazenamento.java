package main;

import java.util.HashMap;
import java.util.Map;

public class MockArmazenamento implements ArmazenamentoInterface {

    HashMap<String, HashMap<String,String>> users = new HashMap<>();

    @Override
    public HashMap<String, String> filterByUser(String userName) {
        return users.get(userName);
    }

    @Override
    public HashMap<String, String> filterByPointType(String pointType) {
        HashMap<String,String> pointTypeMap = new HashMap<>();
        for (Map.Entry<String, HashMap<String,String>> entry : users.entrySet()) {
            String userName = entry.getKey();
            HashMap<String,String> userPoints = entry.getValue();

            if (userPoints.containsKey(pointType));
                pointTypeMap.put(userName, userPoints.get(pointType));
        }
        return pointTypeMap;
    }

    @Override
    public String filterByUserAndPointType(String userName, String pointType) {
        return users.get(userName).get(pointType);
    }

    @Override
    public void setUserPoints(String userName, String pointType, String pointValue) {
        HashMap<String,String> points = users.containsKey(userName)? users.get(userName) : new HashMap<>();
        points.put(pointType, pointValue);
        users.put(userName, points);
    }

}
