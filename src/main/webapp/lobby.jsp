<html ng-app="risky">
<head>
    <link rel="stylesheet" type="text/css" href="/risky/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/risky/css/style.css">
    <script type="text/javascript" src="/risky/js/angular.min.js"></script>
    <script type="text/javascript" src="/risky/js/angular-resource.min.js"></script>
    <script type="text/javascript" src="/risky/js/risky.js"></script>
    <script type="text/javascript" src="/risky/js/lobby.js"></script>
    <title>Lobby | Risky</title>
</head>

<body ng-controller="LobbyController">
    <h1>{{lobby.title}}</h1>
    
    <hr></hr>

    <h3>Players</h3>
    <p>
        <div ng-repeat="player in players">
            <span>#<span>{{$index}}</span></span>: <input type="text" ng-model="player.name" /><!-- JSPs can't have hashtag followed by an opening curly brace -->
            <a class="btn btn-danger" ng-click="removePlayer($index)"><i class="icon-remove"></i></a>
        </div>
        <div class="input-append">
            <input type="text" ng-model="playerName" />
            <a class="btn btn-success" ng-click="addPlayer()">add player</a>
        </div>
    </p>

    <hr></hr>

    <h3>Get ready to rumble!</h3>
    <p>
        <a class="btn" ng-click="loadDefaults()">Load Default Lobby <small>(for testing purposes)</small></a>
    </p>
    <p>
        <div ng-show="players.length < 3">Not yet though, <span class="badge badge-important">3</span> player minimum</div>
        <div ng-show="players.length >= 3 && players.length <= 6">
            <a class="btn btn-primary" href="/risky/game/start">Start Match</a> Up to <span class="badge badge-info">{{6 - players.length}}</span> more players</small>
        </div>
    </p>
</body>
</html>
