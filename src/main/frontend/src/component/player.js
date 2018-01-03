import Worm from './worm'
import Controls from './controls'
const moveSnake = require('./wormMovement').moveSnake;

class Player extends Worm {
    constructor(gameContext, skin) {
        if (gameContext.featureMatrix.drawOwnSnakeFromServer) {
            super({doNotDraw: true});
        } else {
            super({skin: skin, id: gameContext.communication.commId, gameContext: gameContext});
        }
        this.skin = skin;
        this.gameContext = gameContext;
        this.coordinates = {x: 0, y: 0};
        this.resize();

        this.gameContext.communication.subject.filter(msg => msg.hasEatenfood()).subscribe(
            msg => {
                for (const dot of msg.getEatenfood().getDotsList()) {
                    this.length += (dot.getSize() + 1);
                }

                this.gameContext.controls.scoreUpdateSubject.next({id: this.id, length: this.length, currentPlayer: true, type: 'update'});
            }
        );
    }

    updatePosition() {
        if (!this.gameContext.featureMatrix.drawOwnSnakeFromServer) {
            this.container.position.set(this.gameContext.middle.x - this.coordinates.x, this.gameContext.middle.y - this.coordinates.y);
        }
    }

    resize() {
        this.updatePosition();
    }

    update(askedAngle, elapsedTime) {
        let baseSpeed = 1.0;
        this.speed = 5.0 * (this.gameContext.controls.isMouseDown() ? baseSpeed * 2 : baseSpeed); // * elapsedTime * 0.06;
        this.angle = Controls.computeAllowedAngle(askedAngle, this.angle, elapsedTime, this.gameContext, baseSpeed, this.speed);

        if (this.gameContext.featureMatrix.drawOwnSnakeFromServer) {
            let newPath = moveSnake(this.path, this.angle, this.speed, this.partDistance);
            this.path = newPath.path;
        } else {
            super.update(elapsedTime);
        }

        this.coordinates = {x: this.path[0].x, y: this.path[0].y};
        this.updatePosition();

        const path = this.path.map(p => {
            const part = new proto.Part();
            part.setX(p.x);
            part.setY(p.y);
            part.setRotation(p.r);
            return part;
        });

        const playerMoved = new proto.PlayerMoved();
        playerMoved.setX(this.coordinates.x);
        playerMoved.setY(this.coordinates.y);
        playerMoved.setPartsList(path);
        playerMoved.setInitiated(Date.now());
        playerMoved.setSkin(`${this.skin}`);
        playerMoved.setSpeed(this.speed);
        playerMoved.setRotation(this.angle);
        const message = new proto.Message();
        message.setPlayermoved(playerMoved);
        const bytes = message.serializeBinary();
        this.gameContext.communication.subject.next(bytes);
    }
}

export default Player
