<%@ page import="edu.gatech.cs2340.risky.Model" %>
<%@ page import="com.google.gson.Gson" %>
<% Object model = (Object) request.getAttribute("model"); %>
<% if (model == null) { %>null<% } else { %>
<% Gson gson = new Gson(); %>
<%=gson.toJson(model) %><% } %>
