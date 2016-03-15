
function drawArrows(d) {


    var yDiff = Math.abs(d.target.y - d.source.y);
    var xDiff = Math.abs(d.target.x - d.source.x);

    var sourceType = d.sourceType;
    var targetType = d.targetType;

    var sourceX = 0;
    var sourceY = 0;
    var targetY = 0;
    var targetX = 0;

    if (sourceType == "circle" || sourceType == "end" || sourceType == "throw") {
        sourceX = d.source.x + 30;
        sourceY = d.source.y + 30;
    }
    if (targetType == "circle" || targetType == "end" || targetType == "throw") {
        targetY = d.target.y + 30;
        targetX = d.target.x + 30;
    }
    if (sourceX == 0) {
        sourceX = d.source.x + 54;
    }
    if (sourceY == 0) {
        sourceY = d.source.y + 45;
    }
    if (targetY == 0) {
        targetY = d.target.y + 45;
    }
    if (targetX == 0) {
        targetX = d.target.x + 54;
    }

    if (sourceX == targetX || sourceY == targetY || yDiff < 95) {
        return "M " + sourceX + " " + sourceY + " L " + targetX + " " + targetY;
    } else {
        return "M " + sourceX + " " + sourceY + " L " + targetX + " " + sourceY + " L " + targetX + "," + targetY;
    }
}



