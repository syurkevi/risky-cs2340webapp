package edu.gatech.cs2340.risky;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.gatech.cs2340.risky.RiskyServlet;
import com.google.gson.Gson;

public abstract class ApiServlet extends RiskyServlet {
    
    HttpServletRequest request;
    HttpServletResponse response;
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        this.request = request;
        this.response = response;
        super.service(request, response);
    }
    
    protected void dispatch(Object model) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(model));
    }

}
