risky.controller('LobbyController', function ($scope) {
    $scope.players = [];
    $scope.lobby = {
        'title': ''
    };
    
    $scope.addPlayer = function () {
        if ($scope.playerName.length <= 0) return;
        var name = $scope.playerName;
        for (var id in $scope.players) {
            if ($scope.players[id] === name) return;// disallow players with the same name
        }
        $scope.players.push(name);
        $scope.playerName = '';
    };
    
    $scope.removePlayer = function (index) {
        $scope.players.remove(index);
    };
    
    $scope.buildDefaultLobby = function () {
        $scope.lobby.title = 'House of the Pizza Power';
        $scope.players = ['Lenny', 'Ralph', 'Don', 'Mikey'];
    };
    
    $scope.startMatch = function () {
        document.getElementById('submitForm').submit();
    };
});
