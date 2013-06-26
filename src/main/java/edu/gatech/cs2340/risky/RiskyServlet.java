package edu.gatech.cs2340.risky;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.gatech.cs2340.risky.database.ModelDb;
import edu.gatech.cs2340.risky.database.HashMapDbImpl;
import java.io.PrintWriter;
import java.util.Map;
import java.util.HashMap;

public abstract class RiskyServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String operation = (String) request.getParameter("operation");
        if (operation != null && operation.equalsIgnoreCase("POST")) {
            doPost(request, response);
            
        } else if (operation != null && operation.equalsIgnoreCase("PUT")) {
            doPut(request, response);
            
        } else if (operation != null && operation.equalsIgnoreCase("DELETE")) {
            doDelete(request, response);
            
        } else {
            super.service(request, response);
        }
    }
    
    protected Map<String, ModelDb> getDbs(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, ModelDb> dbs = (Map<String, ModelDb>) session.getAttribute("dbs");
        if (dbs == null) {
            dbs = new HashMap<String, ModelDb>();
            session.setAttribute("dbs", dbs);
        }
        return dbs;
    }
    
    protected <T> ModelDb<T> getDb(HttpServletRequest request, Class c) {
        Map<String, ModelDb> dbs = this.getDbs(request);
        ModelDb<T> db = dbs.get(c.getSimpleName());
        if (db == null) {
            db = new HashMapDbImpl<T>();
            dbs.put(c.getSimpleName(), db);
        }
        return db;
    }

    protected int getId(HttpServletRequest request) {
        String uri = request.getPathInfo();
        String idStr = uri.substring(1, uri.length());// Strip off the leading slash, e.g. "/2" becomes "2"
        return Integer.parseInt(idStr);
    }

}
