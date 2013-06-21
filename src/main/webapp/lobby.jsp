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
    <script type="text/javascript" src="/risky/js/lobby.js"></script>
    <title>Risky Web App</title>
</head>

<body ng-controller="LobbyController">
    <% if (lobby != null) { %>
        <div>Recieved the following data:</div>
        <div>name: <%=lobby.getName() %></div>
        <% TurnManager turnList = new TurnManager(); %>
        <% for (Player p : lobby.players) { %>
            <% turnList.addPlayer(p);%>
        <% } %>
        <% turnList.shuffleOrder(); %>
        
        <div>player count: <%=lobby.players.size() %></div>
        <div>Player turn Order: <%=turnList.PlayerOrder() %></div>
        <% Player p; %>
        <% for (int i=0 ; i < lobby.players.size() ; ++i) { %>
            <% p=turnList.getNextPlayer(); %>
            <div>p: <%=p.name %> (<%=p.armies %> armies)</div>
        <% } %>

    <% } else { %>

        <h1>{{lobby.title || 'Your lobby'}}</h1>

        <div><label for="lobbyTitle">Lobby Name</label><input type="text" ng-model="lobby.title" name="lobbyTitle" /></div>

        <hr></hr>

        <h3>List players</h3>
        <p><div ng-repeat="player in players">{{player}} <span ng-click="removePlayer($index)"><i class="icon-remove"></i></span></div></p>
        <div>
            <div class="input-append">
                <input type="text" ng-model="playerName" name="playerName" />
                <input type="button" class="btn btn-success" ng-click="addPlayer()" value="add player" />
            </div>
        </div>

        <hr></hr>

        <h3>Get ready to rumble!</h3>
        <div ng-show="players.length < 3" class="">Not yet though, <span class="badge badge-important">3</span> player minimum</div>
        <div ng-show="players.length > 6">Woah there, <span class="badge badge-important">6</span> player maximum</div>
        <div ng-show="players.length >= 3 && players.length <= 6">
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
                <div><input type="button" class="btn btn-primary" ng-click="startMatch()" value="Start Match" /> <small ng-show="players.length < 6">Up to <span class="badge badge-info">{{6 - players.length}}</span> more players</small><small ng-show="players.length == 6">No more players</small></div>
            </div>
            <form action="/risky/game/create" method="post" id="submitForm">
                <input type="hidden" value="{{lobby.title}}" name="title" />
                <input type="hidden" ng-repeat="name in players" name="player{{$index}}" value="{{name}}" />
            </form>
        </div>
    <% } %>
</body>
</html>
