<%@ page import="edu.gatech.cs2340.risky.model.Game" %>
<%@ page import="edu.gatech.cs2340.risky.model.Lobby" %>
<%@ page import="edu.gatech.cs2340.risky.model.Player" %>
<%@ page import="java.util.*" %>

<% Lobby lobby = (Lobby) request.getAttribute("lobby"); %>
<% Game game = (Game) request.getAttribute("game"); %>

<html ng-app="risky">
<head>
    <link rel="stylesheet" type="text/css" href="/risky/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/risky/css/style.css">
    <script type="text/javascript" src="/risky/js/angular.min.js"></script>
    <script type="text/javascript" src="/risky/js/risky.js"></script>
    <script type="text/javascript" src="/risky/js/game.js"></script>
    <script type="text/model-data" for="players">[
        <% for (int i=0 ; i < game.lobby.players.size() ; i++) { %>
            <% Player player = game.lobby.players.get(i); %>
            <% if (i > 0) { %>
            ,
            <% } %>
            {"name": "<%=player.name %>"}
        <% } %>
    ]</script>
    <title>Risky Web App</title>
</head>
<body ng-controller="GameController">
    <% if (game == null) { %>
        <a href="/risky/lobby">Create a lobby first</a>
    <% } else { %>
        <h1>Your game</h1>
        <%=game.lobby.name %>
        <canvas id="map" width="800" height="500"></canvas>
        <div class="row-fluid">
            <div class="span2">
                <div ng-repeat="player in players" ng-class="$index == turnOwner | iif : 'hasTurn' : """>
                   <span ng-class="$index == turnOwner | iif : 'label' : ''">{{player.name}}</span>
                </div>
            </div>
            <div class="span10 row-fluid no-left-gutter">
                <div class="span3">
                    <h4>Place armies</h4>
                    <small>Click one of your territories to place an army</small>
                    <small>Hit 'u' to undo an placement</small>
                </div>
                <div class="span3">
                    <h4>Attack</h4>
                    <small>Choose a territory to attack</small>
                    <small>Choose a territory to attack from</small>
                    <small>Repeat as necessary</small>
                </div>
                <div class="span3">
                    <h4>Fortify</h4>
                    <small>You have one shot to seize everything you ever wanted. Capture it; don't let it slip!</small>
                </div>
                <div class="span3">
                    <h4>End turn</h4>
                    <small>Call it quits already</small>
                    <a class="btn btn-primary btn-mini" ng-click="nextTurn()">End</a>
                </div>
            </div>
        </div>
    <% } %>
</body>
</html>
