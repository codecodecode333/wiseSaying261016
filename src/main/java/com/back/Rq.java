package com.back;

import java.util.HashMap;
import java.util.Map;

public class Rq {
    private String actionName;
    private Map<String, String> params = new HashMap<>();

    public Rq(String command){
        String[] parts = command.split("\\?", 2);
        actionName = parts[0];

        if (parts.length == 2) {
            String[] tokens = parts[1].split("&");

            for (String token : tokens) {
                String[] param = token.split("=", 2);

                if (param.length == 2) {
                    params.put(param[0], param[1]);
                }
            }
        }
    }

    public String getActionName(){
        return actionName;
    }
    public int getParamAsInt(String key, int defaultValue){
        try {
            return Integer.parseInt(params.get(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    public String getParam(String key, String defaultValue){
        return params.getOrDefault(key, defaultValue);
    }

}
