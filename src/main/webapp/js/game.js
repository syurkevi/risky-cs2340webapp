risky.controller('GameController', function ($scope, $q, modelloader) {
    $scope.turnOwner = 0;
    

    var polygons = [
        {"id": 0, "vertexes": [[4, 4], [16, 4], [12, 20], [6, 18]]},
        {"id": 1, "vertexes": [[16, 4], [14, 12], [20, 18], [24, 10]]},
        {"id": 2, "vertexes": [[20, 7], [28, 6], [26, 18], [20, 18], [24, 10]]},
        {"id": 3, "vertexes": [[12, 20], [14, 12], [20, 18], [26, 18], [24, 24], [16, 32]]},
        {"id": 4, "vertexes": [[30, 24], [34, 20], [38, 22], [36, 24], [32, 26]]},
        {"id": 5, "vertexes": [[34, 8], [42, 4], [50, 6], [58, 4], [50, 12]]},
        {"id": 6, "vertexes": [[58, 4], [68, 6], [66, 14], [56, 6]]},
        {"id": 7, "vertexes": [[56, 6], [66, 14], [62, 16], [54, 8]]},
        {"id": 8, "vertexes": [[58, 12], [58, 21], [65, 22], [68, 18], [70, 12], [67, 10], [66, 14], [62, 16]]},
        {"id": 9, "vertexes": [[42, 10], [50, 12], [54, 8], [58, 12], [58, 18], [52, 18]]},
        {"id": 10, "vertexes": [[48, 26], [47, 14], [52, 18], [58, 18], [58, 21], [55, 23]]},
        {"id": 11, "vertexes": [[68, 18], [65, 22], [62, 40], [68, 36], [71, 24]]},
        {"id": 12, "vertexes": [[55, 23], [58, 21], [65, 22], [64, 28], [58, 30]]},
        {"id": 13, "vertexes": [[52, 22], [55, 23], [58, 30], [56, 34], [54, 32]]},
        {"id": 14, "vertexes": [[57, 32], [58, 30], [64, 28], [63, 34], [61, 32]]},
        {"id": 15, "vertexes": [[55, 36], [57, 32], [61, 32], [63, 34], [62, 40]]},
        {"id": 16, "vertexes": [[46, 34], [50, 38], [48, 42], [42, 38]]},
        {"id": 17, "vertexes": [[46, 34], [44, 36], [42, 38], [45, 40], [38, 42], [35, 41], [32, 38], [36, 36]]},
        {"id": 18, "vertexes": [[35, 41], [30, 44], [26, 40], [28, 38], [32, 38]]},
        {"id": 19, "vertexes": [[34, 37], [30, 34], [27, 35], [28, 38], [32, 38]]},
        {"id": 20, "vertexes": [[27, 35], [28, 38], [26, 40], [27, 41], [22, 42], [20, 34]]},
        {"id": 21, "vertexes": [[28, 42], [18, 46], [12, 34], [20, 34], [22, 42], [27, 41]]},
        {"id": 22, "vertexes": [[16, 42], [4, 40], [2, 30], [11, 32]]},
        {"id": 23, "vertexes": [[32, 26], [36, 24], [38, 22], [40, 24], [37, 27], [33, 27]]}
    ];
    for(i=0;i<polygons.length;i++) {
        polygons[i].owner=({"color":"#EEE","armies":0});
    }
    var map = new Map(document.getElementById("map"), polygons, {});
    map.draw();

    $scope.players=$q.when(modelloader.get());
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
