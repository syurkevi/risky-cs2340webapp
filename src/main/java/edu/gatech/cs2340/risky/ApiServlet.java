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
            if (request.getParameter("action") != null) {
                doAction(request, response);
            } else {
                super.service(request, response);
            }
        } catch (Exception e) {
            handleException(response, e);
            log("Exception in calling RiskyServlet.service()", e);
        }
    }
    
    protected void doAction(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> givens = this.getParameterFieldMap(request);
        String method = givens.remove("action").toString();
        //givens.remove("action");
        //givens.putAll(this.getPayloadFieldMap(request));
        
        //String method = getDoMethodName(request);
        
        Object model;
        
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
            
            model = call(request, best, givens);
            
        } catch (NoSuchMethodException e) {
            boolean hasMessage = e.getMessage() == null || e.getMessage().equals("");
            String message = (!hasMessage) ? "Method Not Found" : e.getMessage();
            model = new Error("404 " + message, e, HttpServletResponse.SC_NOT_FOUND);
            
        } catch (IllegalAccessException e) {
            model = new Error("500 Illegal Access to method " + method + " on " + this.getClass().getSimpleName(), e);
            
        } catch (InvocationTargetException e) {
            Throwable thrown = e.getTargetException();
            //model = new Error("500 " +
            //this.getClass().getSimpleName() +
            //"." + method + " threw " +
            //thrown.getClass().getName() + 
            //": ",// + thrown.getMessage(),
            //e);
            model = e;
        } catch (Exception e) {
            logException(e);
            model = new Error(e.getMessage(), e);
        }
        
        dispatch(response, model);
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
    
    protected void dispatch(HttpServletResponse response, Object model) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        if (model == null) {
            // ensure model is not null in else ifs below
        } else if (model.getClass().equals(Error.class)) {
            response.setStatus(((Error) model).code);
        } else if (model.getClass().getName().contains("Exception")) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        
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
        String action = getAction(request, 1);
        if (action == null) {
            return null;
        }// from /do/stuff-is-cool to stuffIsCool
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
            handleException(response, e);
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
            handleException(response, e);
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
            handleException(response, e);
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
            handleException(response, e);
        }
    }
    
    public Object delete(HttpServletRequest request) throws Exception {
        return null;
    }
    
    protected void handleException(HttpServletResponse response, Throwable e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        dispatch(response, e);
        e.printStackTrace();
    }
    

}