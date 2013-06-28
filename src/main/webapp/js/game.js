risky.controller('GameController', function ($scope, $q, asynchttp) {
    $scope.turnOwner = 0;
    // Get necesarry http requests (player data and map json)
    polygon_promise = asynchttp.get("map");
    players_promise = asynchttp.get("players");
    var map = new Map(document.getElementById("map"), polygon_promise, {});

    polygon_promise.then(function(ret_polygons){
        $scope.polygons=ret_polygons;
        map.draw();
    });
    players_promise.then(function(ret_players){
        $scope.players=ret_players;
        map.draw();
    });

    // Set up the game infrastructure to work with promises
    $scope.nextTurn = function() {
        players_promise.then(function(ret_players){
            $scope.turnOwner = ($scope.turnOwner+1) % ret_players.length;
            map.draw();
        });
    }