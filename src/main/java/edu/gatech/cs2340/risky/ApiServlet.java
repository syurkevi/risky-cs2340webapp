package edu.gatech.cs2340.risky;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import edu.gatech.cs2340.risky.api.Error;

public abstract class ApiServlet extends RiskyServlet {
    
    private String payload;
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            super.service(request, response);
        } catch (Exception e) {
            System.out.println(e.toString());
            StackTraceElement[] stackTraceElements = e.getStackTrace();
            for (int i=0 ; i < 100 && i < stackTraceElements.length ; i++) {
                System.out.println("\t" + stackTraceElements[i].toString());
            }
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
            this.payload = "{}";
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    this.payload = "{}";
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
            response.getWriter().write((model != null) ? new Gson().toJson(model) : "{}");
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

}