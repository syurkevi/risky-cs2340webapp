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
import edu.gatech.cs2340.risky.models.Player;

public abstract class RiskyServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!this.preDo(request, response)) {
            return;
        }
        
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
    
    protected boolean preDo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // meant to be overriden in subclasses to save from having to override service() and call super.service()
        return true;
    }
    
    protected <T extends Model> Map<String, T> getModels(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, T> models = (Map<String, T>) session.getAttribute("models");
        if (models == null) {
            models = new HashMap<String, T>();
            session.setAttribute("models", models);
        }
        return models;
    }
    
    protected <T extends Model> T getModel(HttpServletRequest request, Class c) {
        Map<String, Model> models = this.getModels(request);
        T model = (T) models.get(c.getName());
        // leave the option for a null model so the controller can create one as it pleases
        return model;
    }
    
    protected <T extends Model> void setModel(HttpServletRequest request, T model) {
        Map<String, Model> models = this.getModels(request);
        models.put(model.getClass().getName(), model);
    }
    
    protected <T extends Model> void deleteModel(HttpServletRequest request, T model) {
        Map<String, Model> models = this.getModels(request);
        models.remove(model.getClass().getName());
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
    
    protected String getAction(HttpServletRequest request) {
        String uri = request.getServletPath();
        return uri.substring(uri.lastIndexOf('/'), uri.length());
    }
    
}
