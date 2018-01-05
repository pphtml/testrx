// import Worm from './worm'
// import Controls from './controls'
// const moveSnake = require('./wormMovement').moveSnake;

class Player {
    constructor(gameContext, skin) {
        this.skin = skin;
        this.gameContext = gameContext;

        this.gameContext.communication.subject.filter(msg => msg.hasEatenfood()).subscribe(
            msg => {
                for (const dot of msg.getEatenfood().getDotsList()) {
                    this.length += (dot.getSize() + 1);
                }

                this.gameContext.controls.scoreUpdateSubject.next({id: this.id, length: this.length, currentPlayer: true, type: 'update'});
            }
        );

        const playerStartReq = new proto.PlayerStartReq();
        playerStartReq.setSkin(`${this.skin}`);
        playerStartReq.setInitiated(Date.now());
        const message = new proto.Message();
        message.setPlayerstartreq(playerStartReq);
        const msgBytes = message.serializeBinary();
        this.gameContext.communication.subject.next(msgBytes);
    }

    update(askedAngle, elapsedTime) {
        // let baseSpeed = 1.0;
        // this.speed = 5.0 * (this.gameContext.controls.isMouseDown() ? baseSpeed * 2 : baseSpeed); // * elapsedTime * 0.06;
        // this.angle = Controls.computeAllowedAngle(askedAngle, this.angle, elapsedTime, this.gameContext, baseSpeed, this.speed);

        const playerUpdateReq = new proto.PlayerUpdateReq();
        playerUpdateReq.setRotationasked(askedAngle);
        playerUpdateReq.setSpeedmultiplier(this.gameContext.controls.isMouseDown() ? 2.0 : 1.0);
        playerUpdateReq.setInitiated(Date.now());
        const message = new proto.Message();
        message.setPlayerupdatereq(playerUpdateReq);
        const msgBytes = message.serializeBinary();
        this.gameContext.communication.subject.next(msgBytes);


        // const path = this.path.map(p => {
        //     const part = new proto.Part();
        //     part.setX(p.x);
        //     part.setY(p.y);
        //     part.setRotation(p.r);
        //     return part;
        // });
        //
        // const playerMoved = new proto.PlayerMoved();
        // playerMoved.setX(this.coordinates.x);
        // playerMoved.setY(this.coordinates.y);
        // playerMoved.setPartsList(path);
        // playerMoved.setInitiated(Date.now());
        // playerMoved.setSkin(`${this.skin}`);
        // playerMoved.setSpeed(this.speed);
        // playerMoved.setRotation(this.angle);
        // const message = new proto.Message();
        // message.setPlayermoved(playerMoved);
        // const bytes = message.serializeBinary();
        // this.gameContext.communication.subject.next(bytes);
    }
}

export default Player
