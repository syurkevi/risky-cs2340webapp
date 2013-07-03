package edu.gatech.cs2340.risky;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.gatech.cs2340.risky.api.Error;
import edu.gatech.cs2340.risky.api.LobbyAdapter;
import edu.gatech.cs2340.risky.models.Lobby;

public abstract class ApiServlet extends RiskyServlet {
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            super.service(request, response);
        } catch (Exception e) {
            log("Exception in calling RiskyServlet.service()", e);
        }
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