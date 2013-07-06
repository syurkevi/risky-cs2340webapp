var risky = angular.module("risky", ["ngResource"]);
risky.service("Toast", function ($rootScope) {
    this.send = function (id, type, message) {
        if (arguments.length === 1) {
            console.log(id);
            
        } else if (message === undefined) {
            message = id;// if no message is given, assume id is message, and they don't want to be able to overwrite it later
            id = undefined;
        }
        
        // TODO: inject a <div id="{{id}}" class="toast toast-{{type}}">{{message}}</div> into <div class="toasts"></div>
        
        var type = (type === "error") ? "error" : "log";
        console[type](message);
        
        if (message.data && message.data.cause && message.data.cause.message) message = message.data.cause.message;
        alert(message);// remove once that TODO is TODONE
    }
    this.notify = function (id, message) {
        this.send(id, "notice", message);
    };
    this.error = function (id, message) {
        this.send(id, "error", message);
    };
    
}).directive("swatch", function ($timeout) {
    return {// <swatch color="#00ff00"></swatch> to <span class="color-swatch" background-color="#00ff00"></span>
        restrict: "E",
        replace: true,
        template: "<span class=\"color-swatch\"></span>",
        link: function ($scope, $element, $attrs) {
            $scope.$watch($attrs.color, function () {
                $element.css("backgroundColor", $attrs.color);
            });
        }
    };
    
}).factory("Map", function ($resource) {
    return $resource("/risky/api/map");
    
}).factory("Player", function ($resource) {
    return $resource("/risky/api/player/:id", {
        id: "@id"
    }, {
        "update": {method: "PUT"},
        "attack": {method: "POST", params: {action: "attack", attacking: "", defending: "", attackingDie: 0, defendingDie: 0}},
        "fortify": {method: "POST", params: {action: "fortifyTerritory", from: "", to: "", armies: 0}},
        "seize": {method: "POST", params: {action: "seizeTerritory", territory: ""}},
        "quit": {method: "POST", params: {action: "quit"}}
    });
    
}).factory("Lobby", function ($resource) {
    return $resource("/risky/api/lobby/:id", {
        id: "@id"
    }, {
        "update": {method: "PUT"}
    });
    
}).factory("TurnOrder", function ($resource) {
    return $resource("/risky/api/turnOrder", {}, {
        "nextAction": {method: "POST", params: {action: "nextAction"}},
        "nextTurn": {method: "POST", params: {action: "nextTurn"}}
    });
});

risky.filter("iif", function () {// ternary operator for {{}}'d things
    return function(input, trueValue, falseValue) {
        return input ? trueValue : falseValue;
    };
});

// Array Remove - By John Resig (MIT Licensed)
Array.prototype.remove = function(from, to) {
    var rest = this.slice((to || from) + 1 || this.length);
    this.length = from < 0 ? this.length + from : from;
    return this.push.apply(this, rest);
};

function generateRandomColor() {
    return "#"+Math.floor(Math.random()*16777215).toString(16);
}

function pointInPoly(point, polygon) {
    var i, j, c = false, vertexes = polygon.vertexes;
    for (i = 0, j = vertexes.length - 1; i < vertexes.length; j = i++) {
        if (((vertexes[i][1] > point[1]) != (vertexes[j][1] > point[1])) && (point[0] < (vertexes[j][0] - vertexes[i][0]) * (point[1] - vertexes[i][1]) / (vertexes[j][1] - vertexes[i][1]) + vertexes[i][0])) {
            c = !c;
        }
    }
    return c;
}

function CanvasMap(canvas, map, players, config) {
    this.canvas = canvas;
    this.context = this.canvas.getContext("2d");
    this.territories = map.territories;
    this.players = players;
    this.config = {
        "scale": config.scale || 10
    };
}

CanvasMap.prototype.getOwnershipMap = function () {
    var map = {};// territoryId -> player
    
    for (var i=0 ; i < this.players.length ; i++) {
        var player = this.players[i];
        for (var territoryId in player.territories) {
            map[territoryId] = player;
        }
    }
    
    return map;
};

CanvasMap.prototype.labelTerritory = function (territory, player) {
    var text = "";
    if (player) {
        text = (player.territories[territory.id]) ? player.territories[territory.id].armies : player.name;
    }
    
    var x = 0;
    var y = 0;
    
    for (var i=0 ; i < territory.vertexes.length ; i++) {
        x += territory.vertexes[i][0];
        y += territory.vertexes[i][1];
    }
    x *= this.config.scale / territory.vertexes.length;
    y *= this.config.scale / territory.vertexes.length;
    
    var textSize = this.context.measureText(text);
    
    x -= textSize.width/2;// center horizontally
    x = Math.min(this.canvas.width-5, Math.max(x, 0));// keep it within the canvas bounds
    y = Math.min(this.canvas.height-5, Math.max(y, 10));
    
    this.context.fillStyle = "#333";// dark grey drop-shadow
    this.context.fillText(text, x+0.5, y+0.5);
    
    this.context.fillStyle = "#fff";// white text
    this.context.fillText(text, x, y);
};

CanvasMap.prototype.drawTerritory = function (territory, player) {
    this.context.fillStyle = (player) ? player.color : "#ddd";
    this.context.beginPath();
    
    // half-pixel offsets to avoid heavy-looking lines
    if (!territory.vertexes) return;
    this.context.moveTo(territory.vertexes[0][0]*this.config.scale - 0.5, territory.vertexes[0][1]*this.config.scale - 0.5);
    
    for (var j=1 ; j < territory.vertexes.length ; j++) {;
        this.context.lineTo(territory.vertexes[j][0]*this.config.scale - 0.5, territory.vertexes[j][1]*this.config.scale - 0.5);
    }
    
    this.context.closePath();// imagine "context.moveTo(first vertex)"
    this.context.fill();
    
    this.context.stroke();// actually draw
    
    this.labelTerritory(territory, player);
};

CanvasMap.prototype.draw = function () {
    this.canvas.width = this.canvas.width;// clear canvas
    this.context.strokeStyle = "#333";
    
    var ownershipMap = this.getOwnershipMap();
    
    if (!this.territories) return;
    for (var i=0 ; i < this.territories.length ; i++) {
        var territory = this.territories[i];
        this.drawTerritory(territory, ownershipMap[territory.id]);
    }
};

CanvasMap.prototype.toMapPoint = function (point) {
    return [(point[0]-this.canvas.offsetLeft)/this.config.scale, (point[1]-this.canvas.offsetTop)/this.config.scale];
};

CanvasMap.prototype.getTerritoryAt = function (point) {
    for (var i=0 ; i < this.territories.length ; i++) {
        if (pointInPoly(point, this.territories[i])) {
            return this.territories[i];
        }
    }
};
