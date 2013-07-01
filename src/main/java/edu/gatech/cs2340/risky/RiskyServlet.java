package edu.gatech.cs2340.risky;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.gatech.cs2340.risky.database.ArrayListDbImpl;
import edu.gatech.cs2340.risky.database.ModelDb;

public abstract class RiskyServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String operation = (String) request.getParameter("operation");
        System.out.print("got operation: \"");
        System.out.print((operation == null) ? "" : operation);
        System.out.println("\"");
        if (operation != null && operation.equalsIgnoreCase("POST")) {
            doPost(request, response);
            
        } else if (operation != null && operation.equalsIgnoreCase("PUT")) {
            doPut(request, response);
            
        } else if (operation != null && operation.equalsIgnoreCase("DELETE")) {
            doDelete(request, response);
            
        } else {
            System.out.println("doing normal");
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
    
    protected <T extends Model> ModelDb<T> getDb(HttpServletRequest request, Class c) {
        Map<String, ModelDb> dbs = this.getDbs(request);
        ModelDb<T> db = dbs.get(c.getName());
        if (db == null) {
            db = new ArrayListDbImpl<T>();
            dbs.put(c.getName(), db);
        }
        return db;
    }

    protected int getId(HttpServletRequest request) {
        String uri = request.getPathInfo();
        if (uri.equals("")) {
            return -1;
        }
        String idStr = uri.substring(1, uri.length());// Strip off the leading slash, e.g. "/2" becomes "2"
        try {
            return Integer.parseInt(idStr);
        } catch (Exception e) {
            return -1;
        }
    }
    
}
