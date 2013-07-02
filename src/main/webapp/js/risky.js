var risky = angular.module("risky", ["ngResource"]);
risky.service("Toast", function ($rootScope) {
    this.send = function (id, type, message) {
        if (arguments.length < 2) {
            return;
        } else if (message === undefined) {
            message = id;
            id = undefined;
        }
        // inject a <div id="{{id}}" class="toast toast-{{type}}">{{message}}</div> into <div class="toasts"></div>
        if (type === "error") {
            console.error(message);
        } else {
            console.log(message);
        }
        
        alert(message);
    }
    this.notify = function (id, message) {
        this.send(id, "notice", message);
    };
    this.error = function (id, message) {
        this.send(id, "error", message);
    };
    
}).directive('swatch', function ($timeout) {
    return {
        restrict: 'E',
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
        "do": {method: "PUT", action: "nothing"}
    });
    
}).factory("Lobby", function ($resource) {
    return $resource("/risky/api/lobby/:id", {
        id: "@id"
    }, {
        "update": {method: "PUT"}
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

function CanvasMap(canvas, map, config) {
    this.canvas = canvas;
    this.context = this.canvas.getContext("2d");
    this.polygons = map.territories;
    this.config = {
        "scale": config.scale || 10
    };
}

CanvasMap.prototype.labelPolygon = function (polygon) {
    var text = '';
    if (polygon.armies) {
        text = polygon.armies;
    } else if (polygon.owner) {
        text = polygon.owner.name;
    } else {
        return;
    }
    
    var x = 0;
    var y = 0;
    
    for (var i=0 ; i < polygon.vertexes.length ; i++) {
        x += polygon.vertexes[i][0];
        y += polygon.vertexes[i][1];
    }
    x *= this.config.scale / polygon.vertexes.length;
    y *= this.config.scale / polygon.vertexes.length;
    
    var textSize = this.context.measureText(text);
    
    x -= textSize.width/2;// center horizontally
    x = Math.min(this.canvas.width-5, Math.max(x, 0));// keep it within the canvas bounds
    y = Math.min(this.canvas.height-5, Math.max(y, 10));
    
    this.context.fillStyle = "#333";// dark grey drop-shadow
    this.context.fillText(text, x+0.5, y+0.5);
    
    this.context.fillStyle = "#fff";// white text
    this.context.fillText(text, x, y);
};

CanvasMap.prototype.drawPolygon = function (polygon) {
    this.context.fillStyle = (polygon.owner) ? polygon.owner.color : polygon.color || '#ddd';
    this.context.beginPath();
    
    this.context.moveTo(polygon.vertexes[0][0]*this.config.scale - 0.5, polygon.vertexes[0][1]*this.config.scale - 0.5);
    
    for (var j=1 ; j < polygon.vertexes.length ; j++) {;
        this.context.lineTo(polygon.vertexes[j][0]*this.config.scale - 0.5, polygon.vertexes[j][1]*this.config.scale - 0.5);
    }
    
    this.context.closePath();// pretends to "context.moveTo(first vertex)"
    this.context.fill();
    
    this.context.stroke();// commit the strokes to the canvas
    
    this.labelPolygon(polygon);
};

CanvasMap.prototype.draw = function () {
    this.canvas.width = this.canvas.width;// clears the canvas
    this.context.strokeStyle = "#333";
    for (var i=0 ; i < this.polygons.length ; i++) {
        this.drawPolygon(this.polygons[i]);
    }
};

CanvasMap.prototype.toMapPoint = function (point) {
    return [(point[0]-this.canvas.offsetLeft)/this.config.scale, (point[1]-this.canvas.offsetTop)/this.config.scale];
};

CanvasMap.prototype.getTerritoryAt = function (point) {
    for (var i=0 ; i < this.polygons.length ; i++) {
        if (pointInPoly(point, this.polygons[i])) {
            return this.polygons[i];
        }
    }
};

CanvasMap.prototype.allTerritoriesOwned = function () {
    for (var i=0 ; i < this.polygons.length ; i++) {
        if (!this.polygons[i].owner) return false;
    }
    return true;
};
