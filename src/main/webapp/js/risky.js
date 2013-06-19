var risky = angular.module('risky', []);

// Array Remove - By John Resig (MIT Licensed)
Array.prototype.remove = function(from, to) {
    var rest = this.slice((to || from) + 1 || this.length);
    this.length = from < 0 ? this.length + from : from;
    return this.push.apply(this, rest);
};

risky.controller('LobbyController', function ($scope) {
    $scope.playerCount = 0;
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
    
    $scope.startMatch = function () {
        document.getElementById('submitForm').submit();
    }
});
