let PI_HALF = Math.PI / 2;
let PI_DOUBLE = Math.PI * 2;

exports.moveSnake = function(snakePath, angle, distance, partDistance) {
    // returns path, xStep, yStep
    let path = JSON.parse(JSON.stringify(snakePath));

    let xStep = Math.cos(angle) * distance;
    let yStep = Math.sin(angle) * distance;

    //console.info(`angle: ${angle}, speed: ${speed}, xStep: ${xStep}, y_step: ${y_step}`);

    let head = path[0];
    head.x += xStep;
    head.y += yStep;
    head.r = angle;

    //console.info(JSON.stringify(path));

    for (var index = 0; index < path.length - 1; index++) {
        let previous = path[index], current = path[index + 1];
        let xDiff = previous.x - current.x, yDiff = previous.y - current.y;
        var angleDiff = -Math.atan2(xDiff, yDiff) + PI_HALF;
        if (angleDiff < 0) {
            angleDiff += PI_DOUBLE;
        }
        current.x = previous.x - Math.cos(angleDiff) * partDistance;
        current.y = previous.y - Math.sin(angleDiff) * partDistance;
        current.r = angleDiff;
        //console.info(`angleDiff: ${angleDiff}, xDiff: ${xDiff}, yDiff: ${yDiff}`);
        //console.info(angleDiff);


        /*let xDiff = Math.abs(left.x - right.x), yDiff = Math.abs(left.y - right.y);
        let square = xDiff * xDiff + yDiff * yDiff;
        let diff = Math.sqrt(square);*/
    }

    return {path: path, x: xStep, y: yStep};
};
