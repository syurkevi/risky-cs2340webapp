var risky = angular.module('risky', []);

risky.controller('LobbyController', function ($scope) {
    $scope.playerCount = 0;
    $scope.players = {};
    $scope.lobby = {
        'title': ''
    };
    
    $scope.addPlayer = function () {
        if ($scope.playerName.length <= 0) return;
        var name = $scope.playerName;
        for (var id in $scope.players) {
            if ($scope.players[id] === name) {
                return;// disallow players with the same name
            }
        }
        $scope.players[$scope.playerCount++] = name;
        $scope.playerName = '';
    };
    
    $scope.removePlayer = function (id) {
        delete $scope.players[id];
        $scope.playerCount--;
    };
    
    $scope.startMatch = function () {
        document.getElementById('submitForm').submit();
    }
});
