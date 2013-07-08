

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
    
    var center = this.calculateCenter(territory);
    var x = center[0];
    var y = center[1];
    
    var textSize = this.context.measureText(text);
    
    x -= textSize.width/2;// center horizontally
    x = Math.min(this.canvas.width-5, Math.max(x, 0));// keep within the canvas bounds
    y = Math.min(this.canvas.height-5, Math.max(y, 10));
    
    this.context.font = "12px Sans-serif";
    
    this.context.strokeStyle = "#666";// dark grey drop-shadow
    this.context.lineWidth = 3;
    this.context.strokeText(text, x, y);
    
    this.context.fillStyle = "white";// white text
    this.context.fillText(text, x, y);
};

CanvasMap.prototype.drawTerritory = function (territory, player) {
    this.context.fillStyle = (player) ? player.color : "#ddd";
    this.context.strokeStyle = "black";
    this.context.lineWidth = 1;
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

CanvasMap.prototype.calculateCenter = function (territory) {
    if (territory.center) return territory.center;
    
    var center = [0, 0];
    for (var i=0 ; i < territory.vertexes.length ; i++) {
        center[0] += territory.vertexes[i][0];
        center[1] += territory.vertexes[i][1];
    }
    center[0] *= this.config.scale / territory.vertexes.length;
    center[1] *= this.config.scale / territory.vertexes.length;
    return center;
};

CanvasMap.prototype.getTerritoryAt = function (point) {
    for (var i=0 ; i < this.territories.length ; i++) {
        if (pointInPoly(point, this.territories[i])) {
            return this.territories[i];
        }
    }
    throw new Error("Not a territory");
};
