import Worm from './worm'

class Player extends Worm {
    constructor(gameContext, spriteName = 'cow') {
        super(gameContext, spriteName);
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
        super.update(askedAngle, elapsedTime);
        this.coordinates = {x: this.path[0].x, y: this.path[0].y};
        this.updatePosition();

        this.gameContext.communication.subject.next(
            JSON.stringify({playerMoved:{x:this.coordinates.x.toFixed(2), y:this.coordinates.y.toFixed(2),
            path: this.path.map(p => { return {x: p.x.toFixed(2), y: p.y.toFixed(2), r: p.rotation.toFixed(2)}})}}));
    }
}

export default Player
