package edu.gatech.cs2340.risky.controllers.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.gatech.cs2340.risky.ApiServlet;
import edu.gatech.cs2340.risky.models.Map;
import edu.gatech.cs2340.risky.models.factories.MapFactory;

@WebServlet(urlPatterns = {
    "/api/map"
})
public class MapServlet extends ApiServlet {
    
    Map map;
    
    @Override
    protected boolean preDo(HttpServletRequest request, HttpServletResponse response) {
        map = this.<Map>getModel(request, Map.class);
        if (map == null) {
            map = MapFactory.get(0);
            this.setModel(request, this.map);
        }
        return true;
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        dispatch(response, map);
    }
    
}
