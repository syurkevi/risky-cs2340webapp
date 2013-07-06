package edu.gatech.cs2340.risky.controllers.api;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import edu.gatech.cs2340.risky.ApiServlet;
import edu.gatech.cs2340.risky.Database;
import edu.gatech.cs2340.risky.api.annotations.ApiParams;
import edu.gatech.cs2340.risky.models.Lobby;
import edu.gatech.cs2340.risky.models.TurnOrder;

@WebServlet(urlPatterns = {
    "/api/turnOrder"
})
public class TurnOrderController extends ApiServlet {
    
    @Override
    public Object read(HttpServletRequest request) throws Exception {
        Lobby lobby = Lobby.get(request);
        if (lobby == null) {
            throw new Exception("No lobby to use");
        }
        
        return lobby.turnOrder;
    }
    
    @ApiParams({})
    public Object nextAction(HttpServletRequest request) throws Exception {
        TurnOrder order = TurnOrder.get(request);
        order.nextAction();
        return order;
    }
    
    @ApiParams({})
    public Object nextTurn(HttpServletRequest request) throws Exception {
        TurnOrder order = TurnOrder.get(request);
        order.nextTurn();
        return order;
    }
    
}
