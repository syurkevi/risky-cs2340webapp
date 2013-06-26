package edu.gatech.cs2340.risky;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.lang.reflect.Field;

public class Model {
    
    public Model() {
        
    }
    
    public final String toApi() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
