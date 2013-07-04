package edu.gatech.cs2340.risky;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.gatech.cs2340.risky.Util.Methods;
import edu.gatech.cs2340.risky.Util.Objects;
import edu.gatech.cs2340.risky.Util.Strings;
import edu.gatech.cs2340.risky.api.Error;
import edu.gatech.cs2340.risky.api.LobbyAdapter;
import edu.gatech.cs2340.risky.api.annotations.ApiParams;
import edu.gatech.cs2340.risky.models.Lobby;

public abstract class ApiServlet extends RiskyServlet {
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            if ("do".equals(getAction(request, 0))) {
                Object model = doAction(request, response);
                dispatch(response, model);
            } else {
                super.service(request, response);
            }
        } catch (Exception e) {
            log("Exception in calling RiskyServlet.service()", e);
        }
    }
    
    protected Object doAction(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> givens = this.getParameterFieldMap(request);
        givens.putAll(this.getPayloadFieldMap(request));
        String method = getDoMethodName(request);
        
        try {
            Method[] options = Objects.getMethodsByName(this.getClass(), method);
            if (options.length == 0) {
                throw new NoSuchMethodException("No methods by name " + method);
            }
            
            int difference = Integer.MAX_VALUE;
            int optionDifference = Integer.MAX_VALUE;
            Method best = null;
            
            for (int i=0 ; i < options.length ; i++) {
                Method option = options[i];
                ApiParams params = option.getAnnotation(ApiParams.class);
                optionDifference = Methods.getParameterDifference(params, givens);
                if (optionDifference < difference) {
                    best = option;
                }
            }
            
            if (best == null) {
                throw new NoSuchMethodException("No method " + method + " with similar enough arguments");
            }
            
            return call(best, givens);
            
        } catch (NoSuchMethodException e) {
            error(response, "404 " + ((e.getMessage() == null || e.getMessage().equals("") ? "Method Not Found" : e.getMessage())));
            return null;
            
        } catch (IllegalAccessException e) {
            error(response, "500 Illegal Access to method " + method + " on " + this.getClass().getSimpleName());
            return null;
            
        } catch (InvocationTargetException e) {
            error(response, "500 " + this.getClass().getSimpleName() + "." + method + " threw "
                    + e.getTargetException().getClass().getName() + " with message " + e.getTargetException().getMessage());
            return null;
            
        } catch (Exception e) {
            error(response, e.getMessage());
            logException(e);
            return null;
        }
    }
    
    protected Object call(Method method, Map<String, Object> params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object[] args = new Object[method.getParameterTypes().length];
        
        Set<Map.Entry<String, Object>> entries = (Set<Map.Entry<String, Object>>) params.entrySet();
            
        System.out.println(entries.size() + " given parameters");
        
        for (Map.Entry<String, Object> entry : entries) {
            System.out.println(entry.getKey());
        }
        
        String[] arguments = method.getAnnotation(ApiParams.class).value();
        for (int i=0 ; i < args.length ; i++) {
            args[i] = (i < arguments.length) ? params.get(arguments[i]) : null;
        }
        
        return method.invoke(this, args);
    }
    
    public String getPayload(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            return "{}";
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    return "{}";
                }
            }
        }
        
        return stringBuilder.toString();
    }
    
    protected Object getPayloadObject(HttpServletRequest request, Class objectClass) {
        return new Gson().fromJson(this.getPayload(request), objectClass);
    }
    
    protected Map<String, Object> getParameterFieldMap(HttpServletRequest request) {
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        Enumeration<String> parameterNames = request.getParameterNames();
        
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            fieldMap.put(parameterName, request.getParameter(parameterName));
        }
        
        return fieldMap;
    }
    
    protected Map<String, Object> getPayloadFieldMap(HttpServletRequest request) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        String payload = getPayload(request);
        
        if (payload == null || "".equals(payload) || "{}".equals(payload)) {
            return fieldMap;
        }
        
        try {
            fieldMap = mapper.readValue(payload, Map.class);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return fieldMap;
    }
    
    protected void warn(HttpServletResponse response, String warning) {
        //this.warnings.add(warning);
    }
    
    protected void error(HttpServletResponse response, String error, Object culprit) {
        dispatch(response, new Error(error, culprit));
    }
    
    protected void error(HttpServletResponse response, String error) {
        dispatch(response, new Error(error, null));
    }
    
    protected void dispatch(HttpServletResponse response, Object model) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            GsonBuilder gson = new GsonBuilder();
            gson.registerTypeAdapter(Lobby.class, new LobbyAdapter());
            
            String responseText = (model != null) ? gson.create().toJson(model) : "{}";
            response.getWriter().write(responseText);
            
        } catch (Exception e) {
            this.logException(e);
            return;
        }
    }
    
    protected String getDoMethodName(HttpServletRequest request) {
        // from /do/stuff-is-cool to stuffIsCool
        String action = getAction(request, 1);
        if (action == null) {
            return null;
        }
        return Strings.dashedToCamel(action);
    }
    
    /*
     * Boilerplate method mapping, so the API is more CRUD-y
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        this.read(request, response);
    }
    
    protected void read(HttpServletRequest request, HttpServletResponse response) {
        
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        this.create(request, response);
    }
    
    protected void create(HttpServletRequest request, HttpServletResponse response) {
        
    }
    
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        this.update(request, response);
    }
    
    protected void update(HttpServletRequest request, HttpServletResponse response) {
        
    }
    
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        this.delete(request, response);
    }
    
    protected void delete(HttpServletRequest request, HttpServletResponse response) {
        
    }
    

}