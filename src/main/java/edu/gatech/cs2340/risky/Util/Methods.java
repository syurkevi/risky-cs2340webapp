package edu.gatech.cs2340.risky.Util;

import java.util.Map;

import edu.gatech.cs2340.risky.api.annotations.ApiParams;

public class Methods {
    
    public static int getParameterDifference(ApiParams params, Map<String, Object> givens) {
        int difference = 0;
        
        if (params == null) {
            return Integer.MAX_VALUE;
        }
        
        for (String param : params.value()) {
            if (!givens.containsKey(param)) {
                difference++;
            }
        }
        return difference;
    }
    
}
