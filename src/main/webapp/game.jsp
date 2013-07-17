<html ng-app="risky">
<head>
    <link rel="stylesheet" type="text/css" href="/risky/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/risky/css/style.css">
    <script type="text/javascript" src="/risky/js/angular.min.js"></script>
    <script type="text/javascript" src="/risky/js/angular-resource.min.js"></script>
    <script type="text/javascript" src="/risky/js/risky.js"></script>
    <script type="text/javascript" src="/risky/js/canvas-map.js"></script>
    <script type="text/javascript" src="/risky/js/game.js"></script>
    <script type="text/javascript" src="/risky/js/toast.js"></script>
    <title>Game | Risky</title>
</head>
<body ng-controller="GameController">
    <div>
        <h1 class="pull-left">{{lobby.title}}</h1>
        <a href="/risky/game/quit" class="btn btn-danger btn-mini pull-right">Close game</a>
    </div>
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
            <div class="span4">
                <h4>Select a territory</h4>
                <div>We're picking out the land you want initially</div>
                <div><a class="btn btn-primary btn-mini" ng-click="automateSetup()">Automate</a></div>
            </div>
        </div>
        <div class="span10 row-fluid no-left-gutter action-bar" ng-show="turnOrder.state == 'placearmies'" ng-class="action{{currentAction}}">
            <div class="span4">
                <h4>Place armies</h4>
                <div>{{players[turnOrder.playerIndex].armiesAvailableThisTurn}} armies left</div>
                <div><a class="btn btn-primary btn-mini" ng-click="automatePlacearmies()">Automate</a></div>
            </div>
        </div>
        <div class="span10 row-fluid no-left-gutter action-bar" ng-show="turnOrder.state == 'play'" ng-class="action{{currentAction}}">
            <div class="span4" ng-class="turnOrder.action == 0 | iif : 'highlighted' : ''">
                <h4>Place armies</h4>
                <div>{{players[turnOrder.playerIndex].armiesAvailableThisTurn}} armies to place</div>
                <div>Click a territory to place an army</div>
            </div>
            <div class="span4" ng-class="turnOrder.action == 1 | iif : 'highlighted' : ''">
                <h4>Attack</h4>
                <div>Attack who?</div>
                <div>Where from?</div>
                <div>Repeat as necessary</div>
            </div>
            <div class="span4" ng-class="turnOrder.action == 2 | iif : 'highlighted' : ''">
                <h4>Fortify</h4>
                <div>You have one shot to seize everything you ever wanted. Capture it; don't let it slip!</div>
            </div>
        </div>
        <div class="span10 row-fluid no-left-gutter action-bar" ng-show="turnOrder.state == 'gameover'" ng-class="action{{currentAction}}">
            <div class="span12">
                <h4>A WinRAR is you!</h4>
            </div>
        </div>
    </div>
    <div class="toasts" ng-controller="ToastController">
        <div id="toast{{toast.id}}" class="toast alert alert-block alert-{{toast.type}}" ng-repeat="toast in toasts" >
            <button class="btn btn-mini btn-{{toast.type}} pull-right" ng-show="toast.type != 'success'" ng-click="toastClose(toast.id)"><span class="icon-remove"></span></button>
            <strong>{{toast.message}}</strong>
            <div class="pagination pagination-small" ng-show="toast.type == 'success'">
                <ul>
                    <li ng-class="toast.firstvalue"><a href="#" ng-click="selectValue($index,1)">&laquo;</a></li>
                    <li ng-repeat="value in toast.values" ng-class="value.selected"><a href="#" ng-bind="value" ng-click="selectValue($parent.$index,$index+1)"></a></li>
                    <li ng-class="toast.lastvalue"><a href="#" ng-click="selectValue($index,0)">&raquo;</a></li>
                </ul>
                <button class="btn btn-primary btn-small" ng-click="toastReply(toast.id); toastClose(toast.id);">Execute {{toast.selected}}</button>
            </div>
            <!--div class="btn-group" ng-show="toast.buttons.length > 0">
                <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">Armies<span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li ng-repeat="button in toast.buttons" ng-click="toastReply($event)" id="{{button.value}}">{{button.name}}</li>
                </ul>
            </div-->
        </div>
    </div>
</body>
</html>
