risky.controller("LobbyController", function ($scope, Toast, Player) {
    $scope.players = Player.query();
    $scope.lobby = {
        title: "Risky Lobby"
    };
    
    $scope.addPlayer = function () {
        var p = Player.save({name: $scope.playerName}, function () {
            if (p.error) {
                Toast.error(p.error);
                return;
            }
            $scope.playerName = "";
            $scope.players.push(p);
        });
    };
    
    $scope.removePlayer = function (playerId) {
        var response = Player.delete({id: playerId}, function () {
            if (response.error) {
                Toast.notify(response.error);
                return;
            }
            $scope.players.remove(playerId);
        });
    };
    
    $scope.refreshPlayers = function () {
        $scope.players = Player.query({}, function () {
            console.log($scope.players);
        });
    };
    
    $scope.buildDefaultLobby = function () {
        $scope.lobby.title = "House of the Pizza Power";
        $scope.players = ["Lenny", "Ralph", "Don", "Mikey"];
    };
    
    $scope.startMatch = function () {
        document.getElementById("submitForm").submit();
    };
});
