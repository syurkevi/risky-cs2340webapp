risky.controller("LobbyController", function ($scope, Toast, Player) {
    $scope.players = Player.query({filter: "isNotPlaying"});
    $scope.lobby = {
        title: "Risky Lobby"
    };
    
    $scope.addPlayer = function (name) {
        var p = Player.save({"name": name || $scope.playerName}, function () {
            if (p.error) {
                Toast.error(p.error);
                return;
            }
            $scope.playerName = "";
            $scope.players.push(p);
        });
    };
    
    $scope.removePlayer = function (index) {
        var response = Player.delete({id: $scope.players[index].id}, function () {
            if (response.error) {
                Toast.notify(response.error);
                return;
            }
            $scope.players.remove(index);
        });
    };
    
    $scope.loadDefaults = function () {
        $scope.lobby.title = "House of the Pizza Power";
        $scope.addPlayer("Lenny");
        $scope.addPlayer("Ralph");
        $scope.addPlayer("Don");
        $scope.addPlayer("Mikey");
    };
});
