package edu.gatech.cs2340.risky.api;

public class Error {
    
    public String error;
    public Object culprit;
    
    public Error(String error, Object culprit) {
        this.error = error;
        this.culprit = culprit;
    }
}
