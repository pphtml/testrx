import Worm from './worm'
import Controls from './controls'

class Player extends Worm {
    constructor(gameContext, spriteName = 'cow') {
        super({skin: spriteName});
        this.gameContext = gameContext;
        this.coordinates = {x: 0, y: 0};
        this.resize();
    }

    updatePosition() {
        this.container.position.set(this.gameContext.middle.x - this.coordinates.x, this.gameContext.middle.y - this.coordinates.y);
    }

    resize() {
        this.updatePosition();
    }

    update(askedAngle, elapsedTime) {
        let baseSpeed = 1.0;
        this.speed = 5.0 * (this.gameContext.controls.isMouseDown() ? baseSpeed * 2 : baseSpeed); // * elapsedTime * 0.06;
        this.angle = Controls.computeAllowedAngle(askedAngle, this.angle, elapsedTime, this.gameContext, baseSpeed, this.speed);
        //this.lastAngle = angle;


        super.update(elapsedTime);
        this.coordinates = {x: this.path[0].x, y: this.path[0].y};
        this.updatePosition();

        this.gameContext.communication.subject.next(
            JSON.stringify({
                playerMoved: {
                    x: this.coordinates.x.toFixed(2),
                    y: this.coordinates.y.toFixed(2),
                    path: this.path.map(p => {
                        return {x: p.x.toFixed(2), y: p.y.toFixed(2), r: p.r.toFixed(2)}
                    }),
                    sent: Date.now()
                }
            }));
    }
}

export default Player
