risky.controller('GameController', function ($scope, $q, asynchttp) {
    $scope.turnOwner = 0;
    // Get necesarry http requests (player data and map json)
    polygon_promise = asynchttp.get("map");
    players_promise = asynchttp.get("players");
    var map = new Map(document.getElementById("map"), polygon_promise, {});

<<<<<<< HEAD
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
});

/*
    $scope.turnOwner = 0;
=======
>>>>>>> df56a7d734c3f51c4e7a29d20738a8db82ade4dd
    polygonspromise=asynchttp.get("map");
    polygonspromise.then(function(ret_map){
        polygons=ret_map;
        var map = new Map(document.getElementById("map"), polygons, {});
        map.draw();
        return ret_map;
    });

    $scope.players=asynchttp.get("players");
    $scope.players.then(function(ret_players){
        var shuffle = new Array();
        for(i=0;i<ret_players.length;i++) {
            shuffle[i]=i;
        }

        for(i=0;i<polygons.length;i++) {
            if(shuffle.length != 0 && i % shuffle.length === 0) {
                shuffle.sort(function(){return Math.floor(Math.random() * 4);}); // Lightly random sorting
            }
            var polcolor = (shuffle[i % shuffle.length]+1).toString(2); // dec to bin string
            while(polcolor.length<3)polcolor='C'+polcolor;
            polygons[i].owner = {"id":shuffle[i % shuffle.length], "color": '#'+((polcolor.replace(/1/ig,'DD')).replace(/C/ig,'CC')).replace(/0/ig,'CC'),"armies":/*ret_players[shuffle[i%shuffle.length]].armies//15}; // bin to color, only works well up to 7
                if(polygons[i].owner.id==$scope.turnOwner){
                    polygons[i].owner.color=polygons[i].owner.color.replace(/DD/ig,'D0');
                    polygons[i].owner.color=polygons[i].owner.color.replace(/CC/ig,'40');
                }else{
                    polygons[i].owner.color=polygons[i].owner.color.replace(/D0/ig,'DD');
                    polygons[i].owner.color=polygons[i].owner.color.replace(/40/ig,'CC');
                }
        } 
        map.draw();
        return ret_players;
    },function(ret_error){console.error(ret_error);});
    
    $scope.nextTurn = function () {
        $scope.turnOwner = ($scope.turnOwner+1) % $scope.players.length;
        for(i=0;i<polygons.length;i++){
            if(polygons[i].owner.id==$scope.turnOwner){
                polygons[i].owner.color=polygons[i].owner.color.replace(/DD/ig,'D0');
                polygons[i].owner.color=polygons[i].owner.color.replace(/CC/ig,'40');
            }else{
                polygons[i].owner.color=polygons[i].owner.color.replace(/D0/ig,'DD');
                polygons[i].owner.color=polygons[i].owner.color.replace(/40/ig,'CC');
            }
        }
        var map = new Map(document.getElementById("map"), polygons, {});
        map.draw();
    };
});
*/
