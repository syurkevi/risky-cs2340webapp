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
        "placearmies":{
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
                    Toast.notify("territory fortified");
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
                    if (!data.attacking) {
                        Toast.notify(getCurrentPlayer()+" is attacking!");
                        Toast.notify("chose attack origin");
                        var territory = map.getTerritoryAt(map.toMapPoint([e.pageX, e.pageY]));
                        if (map.getOwnerOfTerritory($scope.players, territory.id).name != getCurrentPlayer().name) throw new Exception("You do not own this territory");
                        data['attacking'] = territory;
                        
                        Toast.notify("attacking territory " + territory.id);
                        
                    } else if (!data.defending) {
                        var territory = map.getTerritoryAt(map.toMapPoint([e.pageX, e.pageY]));
                        if (map.getOwnerOfTerritory($scope.players, territory.id).name == getCurrentPlayer().name) throw new Exception("You own this territory");
                        data['defending'] = territory;
                        
                        //Toast.notify(data);
                        //Toast.notify(data.attacking);
                        //Toast.notify(data.attacking.id);
                        Toast.notify("defending territory " + territory.id);
                        //Toast.notify(map.getOwnerOfTerritory($scope.players, data.attacking.id));
                        var armiesButton=[]
                        for(var i = 0; i < 5; i++) {
                            armiesButton.push({"name":""+(i+1),"value":i+1});
                        }
                        var die = Toast.request(map.getOwnerOfTerritory($scope.players, data.attacking).name + ", attack with how many die?",armiesButton)*1;// eventually Toast.prompt
                        if (isNaN(die)) throw new Exception("Not a number");
                        var armies = map.getDeedForTerritory(territory);
                        if (die < 1 || die >= armies) throw new Exception("Cannot use that many die. Must be between 1 exclusive and " + (armies-1) + " inclusive");
                        data.attackingDie = die;
                        
                        Toast.notify("set attacking die to " + die);
                        
                        var die = prompt(map.getOwnerOfTerritory($scope.players, data.defending).name + ", defend with how many die?")*1;// eventually Toast.prompt
                        if (isNaN(die)) throw new Exception("Not a number");
                        var armies = map.getDeedForTerritory(territory);
                        if (die < 0 || die > 3) throw new Exception("Cannot use that many die. Must be between 1 and " + Math.min(armies, 2) + " inclusive"); // Does not return anythig yet (not even a promise)
                        data.defendingDie = die;
                        
                        Toast.notify("set defending die to " + die);
                        
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
