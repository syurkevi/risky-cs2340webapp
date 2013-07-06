package edu.gatech.cs2340.risky.Util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Objects {
    
    public static Method[] getMethodsByName(Class c, String name) {
        Method[] candidates = c.getMethods();
        ArrayList<Method> methods = new ArrayList<Method>();
        for (Method candidate : candidates) {
            if (candidate.getName().equals(name)) {
                methods.add(candidate);
            }
        }
        
        candidates = new Method[methods.size()];
        for (int i=0 ; i < candidates.length ; i++) {
            candidates[i] = methods.get(i);
        }
        
        return candidates;
    }
    
}
