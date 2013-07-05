package edu.gatech.cs2340.risky.api;

import javax.servlet.http.HttpServletResponse;

public class Error {
    
    public String error;
    public Object culprit;
    public int code;
    
    public Error(String error, Object culprit) {
        this(error, culprit, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    
    public Error(String error, Object culprit, int code) {
        this.error = error;
        this.culprit = culprit;
        this.code = code;
    }
}
