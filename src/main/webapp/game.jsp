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
    <script type="text/javascript" src="/risky/js/angular-resource.min.js"></script>
    <script type="text/javascript" src="/risky/js/risky.js"></script>
    <script type="text/javascript" src="/risky/js/game.js"></script>
    <title>Game | Risky</title>
</head>
<body ng-controller="GameController">
    <h1>{{lobby.title}}</h1>
    <div class="toasts" style="position: fixed; left: 32px; top: 32px; width: 196px; height: 256px; background-color: #FFF0EE; border-radius: 4px; border: 3px solid #CCC0BB; overflow: scroll;"><h2 style="text-align: center; text-decoration: underline; font-style: italics;">Toast!!!</h2><div id={{toast.id}} ng-repeat="toast in toasts" class="toast toast-{{toast.type}}">{{toast.message}}</div></div>
    <canvas id="map" width="800" height="500" ng-click="onMapClick($event)"></canvas>
    <div class="row-fluid">
        <div class="span2">
            <ul class="nav nav-pills nav-stacked">
                <li ng-repeat="player in players">
                    <swatch color="{{player.color}}"></swatch>
                    <span ng-class="$index == turnOrder.playerIndex | iif : 'label' : ''">{{player.name}}</span>
                    <small>{{player.armiesAvailableThisTurn}}</small>
                </li>
            </ul>
        </div>
        <div class="span10 row-fluid no-left-gutter action-bar" ng-show="turnOrder.state == 'setup'" ng-class="action{{currentAction}}">
            <div class="span3">
                <h4>Select a territory</h4>
                <div>We're picking out the land you want initially</div>
                <div><a class="btn btn-primary btn-mini" ng-click="automateTerritorySelection()">Automate</a></div>
            </div>
        </div>
        <div class="span10 row-fluid no-left-gutter action-bar" ng-show="turnOrder.state == 'play'" ng-class="action{{currentAction}}">
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
                <a class="btn btn-primary btn-mini" ng-click="player[turnOrder.playerIndex].$quit()">Quit Game</a>
            </div>
        </div>
        <div class="span10 row-fluid no-left-gutter action-bar" ng-show="turnOrder.state == 'gameover'" ng-class="action{{currentAction}}">
            <div class="span12">
                <h4>A WinRAR is you!</h4>
            </div>
        </div>
    </div>
</body>
</html>
