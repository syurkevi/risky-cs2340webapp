risky.controller("GameController", function ($scope, $q, Toast, Lobby, TurnOrder, Map, Player) {
    
    $scope.lobby = Lobby.get();
    $scope.turnOrder = TurnOrder.get();
    $scope.players = Player.query();
    
    function getCurrentPlayer() {
        return $scope.players[$scope.turnOrder.playerIndex];
    }
    
    function nextAction() {
        $scope.turnOrder.$nextAction({}, function () {}, Toast.error);
    }
    
    $scope.states = {
        "setup": {
            0: {// seize territory
                "mapClick": function (e) {
                    var territory = map.getTerritoryAt(map.toMapPoint([e.pageX, e.pageY]));
                    
                    var d = $q.defer();
                    getCurrentPlayer().$seize({
                        "territory": territory.id
                    }, d.resolve, d.reject);
                    return d.promise;
                }
            }
        },
        "placearmies": {
            0: {// place army
                "mapClick": function (e) {
                    var territory = map.getTerritoryAt(map.toMapPoint([e.pageX, e.pageY]));
                    
                    var d = $q.defer();
                    getCurrentPlayer().$fortify({
                        to: territory.id,
                        armies: 4
                    }, d.resolve, d.reject);
                    return d.promise;
                }
            }
        },
        "play": {
            0: {// place armies
                "mapClick": function (e) {
                    var territory = map.getTerritoryAt(map.toMapPoint([e.pageX, e.pageY]));
                    
                    var d = $q.defer();
                    getCurrentPlayer().$fortify({
                        to: territory.id,
                        armies: 4
                    }, d.resolve, d.reject);
                    return d.promise;
                }
            }, 
            1: {// attack
                "mapClick": function (e) {
                    
                }
            }, 
            2: {// fortify
                "mapClick": function (e) {
                    
                }
            },
            3: {// end turn
                "mapClick": function (e) {
                    
                }
            }
        }
    }
    
    
    var map;
    $scope.map = Map.get({}, function () {
        $scope.player = Player.query({}, function () {
            map = new CanvasMap(document.getElementById("map"), $scope.map, $scope.players, {});
            map.draw();
        });
    });
    
    $scope.onMapClick = function (e) {
        var d = $q.defer();
        try {
            d.resolve($scope.states[$scope.turnOrder.state][$scope.turnOrder.action].mapClick(e));
        } catch (e) {
            d.reject(e);
        }
        
        d.promise.then(function () {
            Player.query();
            
        }).then(function (data) {
            nextAction();
            
        }, function (error) {
            Toast.error(error);
        });
    };
    
    $scope.$watch("players", function () {
        if (map) map.draw();
    }, true);
    
    
    
    
    
    
    $scope.automateTerritorySelection = function () {
        Lobby.$automateTerritorySelection({}, function () {
            Player.query();
        });
    };
    
    $scope.automateArmySelection = function (playerIndex) {
        var player = $scope.players[playerIndex || $scope.turnOwner];
        
        var territories = player.getTerritories();
        var armies = player.armies.availableThisTurn;
        var each = Math.floor(armies / territories.length);
        
        if (!territories[0].armies) territories[0].armies = 0;
        territories[0].armies += armies - (territories.length-1)*each;
        
        for (var i=1 ; i < territories.length ; i++) {
            if (!territories[i].armies) territories[i].armies = 0;
            territories[i].armies += each;
        }
        map.draw();
        $scope.nextTurn();
    };
    
});
