risky.controller("GameController", function ($scope, $q, Toast, Lobby, TurnOrder, Map, Player) {
    
    $scope.lobby = Lobby.get();
    $scope.turnOrder = TurnOrder.get();
    $scope.players = Player.query();
    $scope.map = Map.get();
    
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
                    
                    getCurrentPlayer().$fortify({
                        to: territory.id,
                        armies: 4
                    }, resolve, reject);
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
                "data": {},
                "mapClick": function (resolve, reject, e) {
                    var territory = map.getTerritoryAt(map.toMapPoint([e.pageX, e.pageY]));
                    if (!data.attacking) {
                        data.attacking = territory.id;
                        
                    } else if (!data.defending) {
                        data.defending = territory.id;
                        
                    } else if (!data.attackingDie) {
                        data.attackingDie = 0;// get number from alert?
                        
                    } else if (!data.defendingDie) {
                        data.defendingDie = 0;
                        
                    } else {
                        // send attack
                        getCurrentPlayer().$attack({
                            to: territory.id,
                            armies: 4
                        }, resolve, reject);
                    }
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
    
    $scope.automateSetup = function () {
        handleAction(function () {
            var d = $q.defer();
            $scope.turnOrder.$automateSetup({}, d.resolve, d.reject);
            return d.promise;
        });
    };
    
    $scope.automatePlacearmies = function () {
        handleAction(function () {
            var d = $q.defer();
            $scope.turnOrder.$automatePlacearmies({}, d.resolve, d.reject);
            return d.promise;
        });
    };
    
    $scope.onMapClick = function (e) {
        var d = $q.defer();
        try {
            d.resolve($scope.states[$scope.turnOrder.state][$scope.turnOrder.action].mapClick(e));
        } catch (e) {
            d.reject(e);
        }
        
        var a;
        
        d.promise.then(function () {
            var p = $q.defer();
            a = new Date().getTime();
            $scope.players = Player.query({}, p.resolve, p.reject);
            return p.promise;
            
        }).then(function (data) {
            console.log(new Date().getTime() - a);
            console.log($scope.players[0].territories);
            map.draw($scope.players);
            nextAction();
            
        }, function (error) {
            console.log(3);
            Toast.error(error);
        });
    }
    
    
    var map;
    $scope.map = Map.get({}, function () {
        $scope.players = Player.query({}, function () {
            map = new CanvasMap(document.getElementById("map"), $scope.map, $scope.players, {});
        });
    });
    
    $scope.$watch("players", function () {
        if (map) {
            console.log("redraw");
            map.draw($scope.players);
        }
    }, true);
    
});
