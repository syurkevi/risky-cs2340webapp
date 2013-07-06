risky.controller("GameController", function ($scope, $q, Toast, Lobby, TurnOrder, Map, Player) {
    
    $scope.lobby = Lobby.get();
    $scope.turnOrder = TurnOrder.get();
    $scope.players = Player.query();
    function getCurrentPlayer() {
        return $scope.players[$scope.turnOrder.playerIndex];
    }
    
    function nextAction() {
        $scope.turnOrder.$nextAction();
    }
    
    $scope.states = {
        "setup": {
            0: {// seize territory
                "mapClick": function (e) {
                    var deferred = $q.defer();
                    var territory = map.getTerritoryAt(map.toMapPoint([e.pageX, e.pageY]));
                    if (!territory) deferred.reject("Invalid territory"); // TODO alert user of invalid territory
                    
                    getCurrentPlayer().$seize({
                        "territory": territory.id
                    }, function (data) {
                        deferred.resolve(data);
                    }, function (error) {
                        deferred.reject(error);
                    });
                    return deferred.promise;
                }
            }
        },
        "placearmies": {
            0: {// place army
                "mapClick": function (e) {
                    var territory = map.getTerritoryAt(map.toMapPoint([e.pageX, e.pageY]));
                    if (!territory) return; // alert user of invalid territory
                    
                    getCurrentPlayer().$fortify({
                        to: territory.id,
                        armies: 5
                    });
                }
            }
        },
        "play": {
            0: {// place armies
                "mapClick": function (e) {
                    var territory = map.getTerritoryAt(map.toMapPoint([e.pageX, e.pageY]));
                    if (!territory) return;
                    
                    getCurrentPlayer().$fortify({
                        to: territory.id,
                        armies: 5
                    });
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
    
    $scope.automateTerritorySelection = function () {
        for (var i=0 ; i < map.polygons.length ; i++) {
            map.polygons[i].owner = $scope.players[i % $scope.players.length];
        }
        map.draw();
        $scope.states.setup.deinit();
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
    
    $scope.onMapClick = function (e) {
        var clickCall = $scope.states[$scope.turnOrder.state][$scope.turnOrder.action].mapClick(e);
        var q = clickCall;
        if (!q || !q["then"]) {
            var deferred = $q.defer();
            Player.query({}, function (success) {
                deferred.resolve(success);
            }, function (error) {
                deferred.reject(error);
            });
            q = deferred.promise;
        }
        
        q.then(function (data) {
            nextAction();
            map.draw();
            
        }, function (error) {
            Toast.error(error);
        });
    };
    
    $scope.$watch("$scope.map", function () {
        if (map) map.draw();
    });
    
    $scope.$watch("$scope.lobby.players", function () {
        if (map) map.draw();
    }, true);
    
});
