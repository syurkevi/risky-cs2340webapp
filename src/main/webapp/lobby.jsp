<%@ page import="edu.gatech.cs2340.risky.model.Player" %>
<%@ page import="edu.gatech.cs2340.risky.model.TurnManager" %>
<%@ page import="edu.gatech.cs2340.risky.model.Lobby" %>
<%@ page import="java.util.*" %>

<% Lobby lobby = (Lobby) request.getAttribute("lobby"); %>

<html ng-app="risky">
<head>
    <link rel="stylesheet" type="text/css" href="/risky/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/risky/css/style.css">
    <script type="text/javascript" src="/risky/js/angular.min.js"></script>
    <script type="text/javascript" src="/risky/js/risky.js"></script>
    <title>Soldiers and Arms!</title>
</head>

<body ng-controller="LobbyController" background="Generals.jpg")>
<style>
body
{
background-repeat:no-repeat;
background-position: top center;
margin-top: 25;
margin-left: 50;
margin-right: 20;
text-align:left;
}
</style>    
<img src="gameMap.jpg" width="500" height="500" align="right" opacity=".4">
    <% if (lobby != null) { %>
        <div>Received the following data:</div>
        <div>name: <%=lobby.getName() %></div>
        <% TurnManager turnList = new TurnManager(); %>
        <% for (Player p : lobby.players) { %>
            <% turnList.addPlayer(p);%>
        <% } %>
        <% turnList.shuffleOrder(); %>
        
        <div>player count: <%=lobby.players.size() %></div>
        <div>Player turn Order: <%=turnList.PlayerOrder() %></div>
        <%Player p;%>
        <% for (int i=0 ; i < lobby.players.size() ; ++i) { %>
            <% p=turnList.getNextPlayer(); %>
            <div>p: <%=p.name %> (<%=p.armies %> armies)</div>
        <% } %>

    <% } else { %>


        <hr></hr>

        <h3><strong>Compiling data... </strong></h3>
        <p><div ng-repeat="player in players">{{player}} <span ng-click="removePlayer($index)"><i class="icon-remove"></i></span></div></p>
        <div>
            <div class="input-append">
                <input type="text" ng-model="playerName" name="playerName" />
                <input type="button" class="btn btn-success" ng-click="addPlayer()" value="add player" />
            </div>
        </div>

        <hr></hr>

        <h3>Commencing global war...</h3>
        <div ng-show="players.length < 3" class=""><strong>Error: Need </strong><span class="badge badge-important">3</span>
            <strong> player minimum...</strong>
            </div>
        <div ng-show="players.length > 6"><strong>Error: Only </strong><span class="badge badge-important">6</span>
            <strong> player maximum...</strong>
            </div>
        <div ng-show="players.length >= 3 && players.length <= 6">
                <h4>Initializing coordinates and data:</h4>
                <div>
                    <div>players: </div>
                    <ul>
                        <li ng-repeat="(id, player) in players">{{player}}</li>
                    </ul>
                </div>
                <div><input type="button" class="btn btn-primary" ng-click="startMatch()" value="START!" /> <small ng-show="players.length < 6">Up to <span class="badge badge-info">{{6 - players.length}}</span> more players</small><small ng-show="players.length == 6"><strong>No more players</strong></small></div>
            </div>
            <form action="/risky/lobby/create" method="post" id="submitForm">
                <input type="hidden" value="{{lobby.title}}" name="title" />
                <input type="hidden" ng-repeat="name in players" name="player{{$index}}" value="{{name}}" />
            </form>
        </div>
         <hr></hr>
    <% } %>
</body>
</html>