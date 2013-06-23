risky.controller('GameController', function ($scope, $q, asynchttp) {
    $scope.turnOwner = 0;
    

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
            polygons[i].owner = {"id":shuffle[i % shuffle.length], "color": '#'+((polcolor.replace(/1/ig,'DD')).replace(/C/ig,'CC')).replace(/0/ig,'CC'),"armies":/*ret_players[shuffle[i%shuffle.length]].armies*/15}; // bin to color, only works well up to 7
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
