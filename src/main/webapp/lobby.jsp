<html ng-app="risky">
<head>
    <link rel="stylesheet" type="text/css" href="/risky/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/risky/css/style.css">
    <script type="text/javascript" src="/risky/js/angular.min.js"></script>
    <script type="text/javascript" src="/risky/js/angular-resource.min.js"></script>
    <script type="text/javascript" src="/risky/js/risky.js"></script>
    <script type="text/javascript" src="/risky/js/lobby.js"></script>
    <title>Risky Web App</title>
</head>

<body ng-controller="LobbyController">
    <h1>{{lobby.title}}</h1>

    <hr></hr>

    <h3>Players</h3>
    <p>
        <div ng-repeat="player in players">
            <span>#<span>{{player.id}}</span></span>: <input type="text" ng-model="player.name" /><!-- JSPs can't have hashtag immediately followed by an opening curly brace -->
            <a class="btn" ng-click="player.$update()"><i class="icon-edit"></i></a>
            <a class="btn btn-danger" ng-click="removePlayer({{player.id}})"><i class="icon-remove"></i></a>
        </div>
        <div class="input-append">
            <input type="text" ng-model="playerName" />
            <a class="btn btn-success" ng-click="addPlayer()">add player</a>
        </div>
    </p>
    
    <div><a class="btn" ng-click="refreshPlayers()">Update players</a></div>

    <hr></hr>

    <h3>Get ready to rumble!</h3>
    <p>
        <div ng-show="players.length < 3">Not yet though, <span class="badge badge-important">3</span> player minimum</div>
        <div ng-show="players.length >= 3 && players.length <= 6">
            <a class="btn btn-primary" href="/risky/lobby/start-match">Start Match</a> Up to <span class="badge badge-info">{{6 - players.length}}</span> more players</small>
        </div>
    </p>
</body>
</html>
