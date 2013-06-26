<%@ page import="edu.gatech.cs2340.risky.models.*" %>
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
            {"name": "<%=player.name %>", "armies": "<%=player.armies %>"}
        <% } %>
    ]</script>
    <script type="text/model-data" for="territories">[
        {"id": 0, "vertexes": [[4, 4], [16, 4], [12, 20], [6, 18]]},
        {"id": 1, "vertexes": [[16, 4], [14, 12], [20, 18], [24, 10]]},
        {"id": 2, "vertexes": [[20, 7], [28, 6], [26, 18], [20, 18], [24, 10]]},
        {"id": 3, "vertexes": [[12, 20], [14, 12], [20, 18], [26, 18], [24, 24], [16, 22]]},
        {"id": 4, "vertexes": [[30, 24], [34, 20], [38, 22], [36, 24], [32, 26]]},
        {"id": 5, "vertexes": [[34, 8], [42, 4], [50, 6], [58, 4], [50, 12]]},
        {"id": 6, "vertexes": [[58, 4], [68, 6], [66, 14], [56, 6]]},
        {"id": 7, "vertexes": [[56, 6], [66, 14], [62, 16], [54, 8]]},
        {"id": 8, "vertexes": [[58, 12], [58, 21], [65, 22], [68, 18], [70, 12], [67, 10], [66, 14], [62, 16]]},
        {"id": 9, "vertexes": [[42, 10], [50, 12], [54, 8], [58, 12], [58, 18], [52, 18]]},
        {"id": 10, "vertexes": [[46, 20], [47, 14], [52, 18], [58, 18], [58, 21], [55, 23]]},
        {"id": 11, "vertexes": [[68, 18], [65, 22], [62, 40], [68, 36], [71, 24]]},
        {"id": 12, "vertexes": [[55, 23], [58, 21], [65, 22], [64, 28], [58, 30]]},
        {"id": 13, "vertexes": [[52, 22], [55, 23], [58, 30], [56, 34], [54, 32]]},
        {"id": 14, "vertexes": [[57, 32], [58, 30], [64, 28], [63, 34], [61, 32]]},
        {"id": 15, "vertexes": [[55, 36], [57, 32], [61, 32], [63, 34], [62, 40]]},
        {"id": 16, "vertexes": [[46, 34], [50, 38], [48, 42], [42, 38]]},
        {"id": 17, "vertexes": [[46, 34], [44, 36], [42, 38], [45, 40], [38, 42], [35, 41], [32, 38], [36, 36]]},
        {"id": 18, "vertexes": [[35, 41], [30, 44], [26, 40], [28, 38], [32, 38]]},
        {"id": 19, "vertexes": [[34, 37], [30, 34], [27, 35], [28, 38], [32, 38]]},
        {"id": 20, "vertexes": [[27, 35], [28, 38], [26, 40], [27, 41], [22, 42], [20, 34]]},
        {"id": 21, "vertexes": [[28, 42], [18, 46], [12, 34], [20, 34], [22, 42], [27, 41]]},
        {"id": 22, "vertexes": [[16, 42], [4, 40], [2, 30], [11, 32]]},
        {"id": 23, "vertexes": [[32, 26], [36, 24], [38, 22], [40, 24], [37, 27], [33, 27]]}
    ]</script>
    <title>Risky Web App</title>
</head>
<body ng-controller="GameController">
    <% if (game == null) { %>
        <a href="/risky/lobby">Create a lobby first</a>
    <% } else { %>
        <h1><%=game.lobby.name %></h1>
        <canvas id="map" width="800" height="500" ng-click="onMapClick($event)"></canvas>
        <div class="row-fluid">
            <div class="span2">
                <div ng-repeat="player in players">
                    <span class="color-swatch" style="background-color: {{player.color}}"></span>
                    <span ng-class="$index == turnOwner | iif : 'label' : ''">{{player.name}}</span>
                </div>
            </div>
            <div class="span10 row-fluid no-left-gutter action-bar" ng-show="state == 'setup'" ng-class="action{{currentAction}}">
                <div class="span3">
                    <h4>Select a territory</h4>
                    <div>We're picking out the land you want initially</div>
                    <div><a class="btn btn-primary btn-mini" ng-click="automateTerritorySelection()">Automate</a></div>
                </div>
            </div>
            <div class="span10 row-fluid no-left-gutter action-bar" ng-show="state == 'placearmies'" ng-class="action{{currentAction}}">
                <div class="span3">
                    <h4>Place armies</h4>
                    <div>{{players[turnOwner].armies.availableThisTurn}} armies left</div>
                    <div><a class="btn btn-primary btn-mini" ng-click="automateArmySelection()">Automate</a></div>
                </div>
            </div>
            <div class="span10 row-fluid no-left-gutter action-bar" ng-show="state == 'play'" ng-class="action{{currentAction}}">
                <div class="span3">
                    <h4>Place armies</h4>
                    <div>Click territories to place an army</div>
                    <div>Hit 'u' to undo an placement</div>
                </div>
                <div class="span3">
                    <h4>Attack</h4>
                    <div>Attack who?</div>
                    <div>Where from?</div>
                    <div>Repeat as necessary</div>
                </div>
                <div class="span3">
                    <h4>Fortify</h4>
                    <div>You have one shot to seize everything you ever wanted. Capture it; don't let it slip!</div>
                </div>
                <div class="span3">
                    <h4>End turn</h4>
                    <div>Call it quits already</div>
                    <a class="btn btn-primary btn-mini" ng-click="nextTurn()">End</a>
                </div>
            </div>
        </div>
    <% } %>
</body>
</html>
