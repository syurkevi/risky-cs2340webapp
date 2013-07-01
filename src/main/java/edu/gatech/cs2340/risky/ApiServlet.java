package edu.gatech.cs2340.risky;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.gatech.cs2340.risky.RiskyServlet;
import edu.gatech.cs2340.risky.api.Error;
import com.google.gson.Gson;
import java.io.PrintWriter;

public abstract class ApiServlet extends RiskyServlet {
    
    // store them, so we don't have to ask when dispatch()ing
    HttpServletRequest request;
    HttpServletResponse response;
    private String payload;
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        this.request = request;
        this.response = response;
        
        try {
            super.service(request, response);
        } catch (Exception e) {
            System.out.println(e.toString());
            StackTraceElement[] stackTraceElements = e.getStackTrace();
            for (int i=0 ; i < 100 ; i++) {
                System.out.println("\t" + stackTraceElements[i].toString());
            }
        }
    }
    
    public String getPayload() {
        if (this.payload == null) {
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
            
            this.payload = stringBuilder.toString();
        }
        System.out.println(this.payload);
        return this.payload;
    }
    
    protected Object getPayloadObject(Class objectClass) {
        return new Gson().fromJson(this.getPayload(), objectClass);
    }
    
    protected void warn(String warning) {
        //this.warnings.add(warning);
    }
    
    protected void error(String error, Object culprit) {
        dispatch(new Error(error, culprit));
    }
    
    protected void error(String error) {
        dispatch(new Error(error, null));
    }
    
    protected void dispatch(Object model) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            response.getWriter().write((model != null) ? new Gson().toJson(model) : "{}");
        } catch (IOException e) {
            System.out.println("IOException");
        }
        
        this.request = null;
        this.response = null;
        this.payload = null;
    }

}
