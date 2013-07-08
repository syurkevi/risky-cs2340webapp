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
                "data": {},
                "mapClick": function (e) {
                    var territory = map.getTerritoryAt(map.toMapPoint([e.pageX, e.pageU]));
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
                        var d = $q.defer();
                        getCurrentPlayer().$attack({
                            to: territory.id,
                            armies: 4
                        }, d.resolve, d.reject);
                        return d.promise;
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
    
    
    var map;
    $scope.map = Map.get({}, function () {
        $scope.players = Player.query({}, function () {
            map = new CanvasMap(document.getElementById("map"), $scope.map, $scope.players, {});
            map.draw();
        });
    });
    
    $scope.onMapClick = function (e) {
        handleAction($scope.states[$scope.turnOrder.state][$scope.turnOrder.action].mapClick, [e]);
    };
    
    function handleAction(func, params) {
        var d = $q.defer();
        try {
            d.resolve(func.apply(null, params));
        } catch (e) {
            d.reject(e);
        }
        
        d.promise.then(function () {
            $scope.players = Player.query();
            
        }).then(function (data) {
            map.draw();console.log("hey");// TODO update map after automation actions
            nextAction();console.log("hey2");
            
        }, function (error) {
            Toast.error(error);
        });
    }
    
    $scope.$watch("players", function () {
        if (map) map.draw();
    }, true);
    
});
