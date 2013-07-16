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
                    var data = $scope.states.play[1].data;
                    if (!data.attacking) {// get the territory to attack from
                        var territory = map.getTerritoryAt(map.toMapPoint([e.pageX, e.pageY]));
                        if (map.getOwnerOfTerritory($scope.players, territory.id).name != getCurrentPlayer().name) throw new Exception("You do not own this territory");
                        data['attacking'] = territory;
                        
                        console.log("set attacking to " + territory.id);
                        
                    } else if (!data.defending) {// get the territory to defend from
                        var territory = map.getTerritoryAt(map.toMapPoint([e.pageX, e.pageY]));
                        if (map.getOwnerOfTerritory($scope.players, territory.id).name == getCurrentPlayer().name) throw new Exception("You own this territory");
                        data['defending'] = territory;
                        
                        console.log(data);
                        console.log(data.attacking);
                        console.log(data.attacking.id);
                        console.log("set defending to " + territory.id);
                        console.log(map.getOwnerOfTerritory($scope.players, data.attacking.id));
                        
                        // get attacking number of die
                        var die = prompt(map.getOwnerOfTerritory($scope.players, data.attacking).name + ", attack with how many die?")*1;// @SY make this a Toast.prompt, or however you've implemented
                        if (isNaN(die)) throw new Exception("Not a number");
                        var armies = map.getDeedForTerritory(territory);
                        if (die < 1 || die >= armies) throw new Exception("Cannot use that many die. Must be between 1 exclusive and " + (armies-1) + " inclusive");
                        data.attackingDie = die;
                        
                        console.log("set attacking die to " + die);
                        
                        // get number of defending die
                        var die = prompt(map.getOwnerOfTerritory($scope.players, data.defending).name + ", defend with how many die?")*1;// @SY and here too
                        if (isNaN(die)) throw new Exception("Not a number");
                        var armies = map.getDeedForTerritory(territory);
                        if (die < 0 || die > 3) throw new Exception("Cannot use that many die. Must be between 1 and " + Math.min(armies, 2) + " inclusive");
                        data.defendingDie = die;
                        
                        console.log("set defending die to " + die);
                        
                    } else {
                        // send attack
                        var d = $q.defer();
                        getCurrentPlayer().$attack({
                            from: data.attacking.id,
                            to: data.defending.id, // could this just be $attack(data, d.resolve ....?
                            attackingDie: data.attackingDie,
                            defendingDie: data.defendingDie
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
        $scope.turnOrder.$automateSetup({}, function () {
            $scope.players = Player.query();
            
        }, Toast.error);
    };
    
    $scope.automatePlacearmies = function () {
        $scope.turnOrder.$automatePlacearmies({}, function () {
            $scope.players = Player.query();
        }, Toast.error);
    };
    
    $scope.onMapClick = function (e) {
        handleAction($scope.states[$scope.turnOrder.state][$scope.turnOrder.action].mapClick, [e]);
    }
    
    function handleAction(func, args) {
        var d = $q.defer();
        try {
            var value = func.apply(null, args);
            if (value === undefined || !value.hasOwnProperty("then")) return;
            d.resolve(value);
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
            console.error("yikes!");
            console.log(error);
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
