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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.gatech.cs2340.risky.Util.Methods;
import edu.gatech.cs2340.risky.Util.Objects;
import edu.gatech.cs2340.risky.Util.Strings;
import edu.gatech.cs2340.risky.api.Error;
import edu.gatech.cs2340.risky.api.annotations.ApiParams;

public abstract class ApiServlet extends RiskyServlet {
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            if ("do".equals(getAction(request, 0))) {
                Object model = doAction(request);
                dispatch(response, model);
            } else {
                super.service(request, response);
            }
        } catch (Exception e) {
            log("Exception in calling RiskyServlet.service()", e);
        }
    }
    
    protected Object doAction(HttpServletRequest request) {
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
            
            return call(request, best, givens);
            
        } catch (NoSuchMethodException e) {
            boolean hasMessage = e.getMessage() == null || e.getMessage().equals("");
            String message = (!hasMessage) ? "Method Not Found" : e.getMessage();
            return new Error("404 " + message, e, HttpServletResponse.SC_NOT_FOUND);
            
        } catch (IllegalAccessException e) {
            return new Error("500 Illegal Access to method " + method + " on " + this.getClass().getSimpleName(), e);
            
        } catch (InvocationTargetException e) {
            Throwable thrown = e.getTargetException();
            return new Error("500 " + this.getClass().getSimpleName() + "." + method + " threw " + thrown.getClass().getName() + ": " + thrown.getMessage(), e);
            
        } catch (Exception e) {
            logException(e);
            return new Error(e.getMessage(), e);
        }
    }
    
    protected Object call(HttpServletRequest request, Method method, Map<String, Object> params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object[] args = new Object[method.getParameterTypes().length];
        args[0] = request;
        
        String[] names = method.getAnnotation(ApiParams.class).value();
        Class[] types = method.getParameterTypes();
        
        for (int i=1 ; i < args.length ; i++) {
            if (i > names.length) {
                args[i] = null;
                continue;
            }
            args[i] = params.get(names[i - 1]);
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
        try {
            return new ObjectMapper().readValue(this.getPayload(request), objectClass);
        } catch (Exception e) {
            return new Object();
        }
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
        error(response, new Error(error, culprit), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    
    protected void error(HttpServletResponse response, String error) {
        error(response, new Error(error, null), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    
    protected void error(HttpServletResponse response, Error error, int code) {
        response.setStatus(code);
        dispatch(response, error);
    }
    
    protected void dispatch(HttpServletResponse response, Object model) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            //gson.registerTypeAdapter(Lobby.class, new LobbyAdapter());
            
            String responseText = (model != null) ? mapper.writeValueAsString(model) : "null";
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
        Object output = null;
        try {
            output = this.getClass().getDeclaredMethod("read", HttpServletRequest.class).invoke(this, request);
            dispatch(response, output);
        } catch (Exception e) {
            error(response, e.getMessage());
            e.printStackTrace();
        }
    }
    
    public Object read(HttpServletRequest request) throws Exception {
        return null;
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Object output = null;
        try {
            output = this.getClass().getDeclaredMethod("create", HttpServletRequest.class).invoke(this, request);
            dispatch(response, output);
        } catch (Exception e) {
            error(response, e.getMessage());
            e.printStackTrace();
        }
    }
    
    public Object create(HttpServletRequest request) throws Exception {
        return null;
    }
    
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        Object output = null;
        try {
            output = this.getClass().getDeclaredMethod("update", HttpServletRequest.class).invoke(this, request);
            dispatch(response, output);
        } catch (Exception e) {
            error(response, e.getMessage());
            e.printStackTrace();
        }
    }
    
    public Object update(HttpServletRequest request) throws Exception {
        return null;
    }
    
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        Object output = null;
        try {
            output = this.getClass().getDeclaredMethod("delete", HttpServletRequest.class).invoke(this, request);
            dispatch(response, output);
        } catch (Exception e) {
            error(response, e.getMessage(), e);
            e.printStackTrace();
        }
    }
    
    public Object delete(HttpServletRequest request) throws Exception {
        return null;
    }
    

}