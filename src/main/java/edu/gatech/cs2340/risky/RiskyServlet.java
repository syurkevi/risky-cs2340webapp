package edu.gatech.cs2340.risky;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class RiskyServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!this.preDo(request, response)) {
            return;
        }
        
        if ("do".equals(getAction(request, 0))) {
            
        }
        
        // delegate on operation parameter for plain ol' html forms
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
        // meant to be overridden in subclasses to save from having to override service() and call super.service()
        // good for getting databases and such
        return true;
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
    
    protected String getSessionId(HttpServletRequest request) {
        // seem innocuous when the following line could just be used everywhere this method is used
        // but if/when it comes time to move to many-session-to-one-lobby, migration *should* be easy
        return request.getSession().getId();
    }
    
    protected String getDoMethodName(HttpServletRequest request) {
        // from /do/stuff-is-cool to stuffIsCool
        return "";
    }
    
    protected String getAction(HttpServletRequest request) {
        return getAction(request, 0);
    }
    
    protected String getAction(HttpServletRequest request, int index) {
        String[] actions = getActions(request);
        if (index >= actions.length) {
            return null;
        }
        return actions[index];
    }
    
    protected String[] getActions(HttpServletRequest request) {
        return request.getPathInfo().substring(1).split("/");
    }
    
    protected String getFinalAction(HttpServletRequest request) {
        String uri = request.getServletPath();
        return uri.substring(uri.lastIndexOf("/") + 1, uri.length());
    }
    
    protected void logException(Exception e) {
        System.out.println(e.toString());
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        for (int i=0 ; i < 100 && i < stackTraceElements.length ; i++) {
            System.out.println("\t" + stackTraceElements[i].toString());
        }
    }
    
}
