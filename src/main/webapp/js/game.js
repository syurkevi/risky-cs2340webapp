risky.controller("GameController", function ($scope, Map, Lobby, Player) {
    
    $scope.lobby = Lobby.get();
    
    $scope.setState = function (newState) {
        $scope.state = newState;
        $scope.turnOwner = 0;
        $scope.currentAction = 0;
        if ($scope.states[$scope.state].init) {
            $scope.states[$scope.state].init();
        }
        if ($scope.states[$scope.state].actions[0].init) {
            $scope.states[$scope.state].actions[0].init();
        }
    };
    
    $scope.nextTurn = function (forceful) {
        if ($scope.turnOwner == $scope.players.length-1 && !forceful) {// if last player
            if ($scope.states[$scope.state].deinit) {
                $scope.states[$scope.state].deinit();// deinitialize the state
            }
        } else {// otherwise
            $scope.turnOwner = ++$scope.turnOwner % $scope.players.length;// move to the next player
            if ($scope.states[$scope.state].actions[$scope.currentAction].init) {
                $scope.states[$scope.state].actions[$scope.currentAction = 0].init();// reset action and initialize
            }
        }
    };
    
    $scope.nextAction = function () {
        if ($scope.states[$scope.state].actions[$scope.currentAction].deinit) {
            $scope.states[$scope.state].actions[$scope.currentAction].deinit();// deinitialize the previous action
        }
        if ($scope.currentAction == $scope.states[$scope.state].actions.length-1) {// if last action
            $scope.nextTurn();// move to the next player
        } else if ($scope.states[$scope.state].actions[++$scope.currentAction].init) {
            $scope.states[$scope.state].actions[++$scope.currentAction].init();// otherwise move to next state
        }
    };
    
    $scope.states = {
        /*"default": {
            "init": function () {},
            "actions": {
                0: {
                    "init": function () {},
                    "mapClick": function () {},
                    "deinit": funciton () {}
                }
            },
            "deinit": function () {}
        },*/
        "setup": {
            "init": function () {},
            "actions": {
                0: {
                    "mapClick": function (e) {
                        var territory = map.getTerritoryAt(map.toMapPoint([e.pageX, e.pageY]));
                        if (!territory) return; // alert user of invalid territory
                        if (territory.owner) return; // alert user of already taken territory
                        territory.owner = $scope.players[$scope.turnOwner];
                        map.draw();
                        $scope.nextTurn();
                    }
                }
            },
            "deinit": function () {
                if (map.allTerritoriesOwned()) {
                    $scope.setState("placearmies");
                } else {
                    $scope.nextTurn(true);
                }
            }
        },
        "placearmies": {
            "init": function () {},
            "actions": {
                0: {
                    "init": function () {
                        var player = $scope.players[$scope.turnOwner];
                        player.armies.availableThisTurn += 1*player.armies.available;
                        player.armies.available = 0;
                    },
                    "mapClick": function (e) {
                        var territory = map.getTerritoryAt(map.toMapPoint([e.pageX, e.pageY]));
                        if (!territory) return; // alert user of invalid territory
                        if (territory.owner != $scope.players[$scope.turnOwner]) return;
                        
                        if (!territory.armies) territory.armies = 0;
                        territory.armies++;
                        if (--territory.owner.armies.availableThisTurn <= 0) $scope.nextTurn();
                        map.draw();
                    }
                }
            },
            "deinit": function () {
                $scope.setState("play");
            }
        },
        "play": {
            "init": function () {},
            "actions": {
                0: {
                    "init": function () {
                        var player = $scope.players[$scope.turnOwner];
                        var allocation = Math.min(player.armies.available, 5);
                        player.armies.available -= allocation;
                        player.armies.availableThisTurn += allocation;
                    },
                    "mapClick": function (e) {
                        var territory = map.getTerritoryAt(map.toMapPoint([e.pageX, e.pageY]));
                        if (!territory) return;
                        if (territory.owner != $scope.players[$scope.turnOwner]) return;
                        
                        territory.armies++;
                        if (--territory.owner.armies.availableThisTurn <= 0) $scope.nextTurn();
                    }
                }, 
                1: {
                    "mapClick": function (e) {
                        
                    }
                }, 
                2: {
                    "mapClick": function (e) {
                        
                    }
                }, 
                3: {
                    "mapClick": function (e) {
                        
                    }
                }
            },
            "deinit": function () {
                $scope.setState("play");
            }
        }
    }
    
    $scope.setState("setup");
    
    $scope.players = Player.query();
    
    var map;
    $scope.map = Map.get({}, function () {
        map = new CanvasMap(document.getElementById("map"), $scope.map, {});
        map.draw();
    });
    
    $scope.onMapClick = function (e) {
        $scope.states[$scope.state].actions[$scope.currentAction].mapClick(e);
    };
    
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
    
    $scope.$watch("$scope.map", function () {
        if (map) map.draw();
    });
    
    $scope.$watch("$scope.lobby.players", function () {
        if (map) map.draw();
    }, true);
    
});
