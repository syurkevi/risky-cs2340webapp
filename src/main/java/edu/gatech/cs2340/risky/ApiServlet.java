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
import com.google.gson.Gson;

public abstract class ApiServlet extends RiskyServlet {
    
    // store them, so we don't have to ask when dispatch()ing
    HttpServletRequest request;
    HttpServletResponse response;
    private String payload;
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        this.request = request;
        this.response = response;
        super.service(request, response);
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
        return this.payload;
    }
    
    protected Object getPayloadObject(Class objectClass) {
        return new Gson().fromJson(this.getPayload(), objectClass);
    }
    
    protected void dispatch(Object model) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (false) {
            response.getWriter().write(model.getClass().getName());
        } else {
            response.getWriter().write(new Gson().toJson(model));
        }
    }

}
