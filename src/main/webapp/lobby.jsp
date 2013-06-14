<%@ page import="edu.gatech.cs2340.risky.model.Player" %>
<%@ page import="edu.gatech.cs2340.risky.model.Lobby" %>
<%@ page import="java.util.*" %>

<% Lobby lobby = (Lobby) request.getAttribute("lobby"); %>

<html ng-app="risky">
<head>
    <link rel="stylesheet" type="text/css" href="/risky/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/risky/css/style.css">
    <script type="text/javascript" src="/risky/js/angular.min.js"></script>
    <script type="text/javascript" src="/risky/js/risky.js"></script>
    <title>Risky Web App</title>
</head>
<body ng-controller="LobbyController">

<% if (lobby != null) { %>
    <div>Recieved the following data:</div>
    <div>name: <%=lobby.name %></div>
    <div>player count: <%=lobby.players.size() %></div>
    <% for (Player p : lobby.players) { %>
        <div>p: <%=p.name %> (<%=192/lobby.players.size() %> armies)</div>
    <% } %>
<% } else { %>

<h1>{{lobby.title || 'Your lobby'}}</h1>

<div><label for="lobbyTitle">Lobby Name</label><input type="text" ng-model="lobby.title" name="lobbyTitle" /></div>

<hr></hr>

<h3>List players</h3>
<p><div ng-repeat="(id, name) in players">{{name}} <span ng-click="removePlayer(id)"><i class="icon-remove"></i></span></div></p>
<div>
    <div class="input-append">
        <input type="text" ng-model="playerName" name="playerName" />
        <input type="button" class="btn btn-success" ng-click="addPlayer()" value="add player" />
    </div>
</div>

<hr></hr>

<h3>Get ready to rumble!</h3>
<div ng-show="playerCount < 3" class="">Not yet though, <span class="badge badge-important">3</span> player minimum</div>
<div ng-show="playerCount > 6">Woah there, <span class="badge badge-important">6</span> player maximum</div>
<div ng-show="playerCount >= 3 && playerCount <= 6">
    <div ng-show="!lobby.title" class="badge badge-warning">Mind naming your lobby for me?</div>
    <div ng-show="lobby.title">
        <h4>So here's what I've got:</h4>
        <div>Lobby name: {{lobby.title}}</div>
        <div>
            <div>Players: </div>
            <ul>
                <li ng-repeat="(id, player) in players">{{player}}</li>
            </ul>
        </div>
        <div><input type="button" class="btn btn-primary" ng-click="startMatch()" value="Start Match" /> <small ng-show="playerCount < 6">Up to <span class="badge badge-info">{{6 - playerCount}}</span> more players</small><small ng-show="playerCount == 6">No more players</small></div>
    </div>
    <form action="/risky/lobby/create" method="post" id="submitForm">
        <input type="hidden" value="{{lobby.title}}" name="title" />
        <input type="hidden" ng-repeat="(id, name) in players" name="player{{id}}" value="{{name}}" />
    </form>
</div>
<% } %>
</body>
</html>