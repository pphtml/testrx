import { Sprite, Container, Graphics, loader, filters, BLEND_MODES } from 'pixi.js'
import Worm from './worm'
// import layers from './layers'
// let resources = loader.resources;

class Worms {
    constructor(gameContext) {
        this.gameContext = gameContext;
        this.gameContext.worms = this;
        this.container = new Container();
        this.map = {};

        // let rectangle = new Graphics();
        // rectangle.beginFill(0x0033CC);
        // rectangle.lineStyle(4, 0xFF0000, 1);
        // rectangle.drawRect(0, 0, 96, 96);
        // rectangle.endFill();
        // rectangle.x = 64;
        // rectangle.y = 64;
        // rectangle.alpha = 0.5;
        // this.container.addChild(rectangle);

        this.gameContext.communication.subject.subscribe(msg => {
            if (msg.hasSnakesupdate()) {
                this.updateWorms(msg.getSnakesupdate().getSnakesList());
            }
            if (msg.hasPlayerresp()) {
                this.handlePlayerResp(msg.getPlayerresp());
            }
            if (msg.hasClientdisconnect()) {
                console.info('vyhodit hada z mapy');
                console.info('vymazat sprity hada');
            }
        });
    }

    updateWorms(worms) {
        // for (let i = this.container.children.length - 1; i >= 0; i--) {
        //     let sprite = this.container.children[i];
        //     this.container.removeChild(sprite);
        // }

        for (const wormData of worms) {
            const id = wormData.getId();
            // if (this.gameContext.featureMatrix.drawOwnSnakeFromServer)
            if (this.gameContext.communication.commId != id || this.gameContext.featureMatrix.drawOwnSnakeFromServer) {
                //console.info(`${id} -> ${JSON.stringify(wormData)}`);
                const path = wormData.getPathList().map(part => {
                    return { x: part.getX(), y: part.getY(), r: part.getRotation() };
                });
                const skin = wormData.getSkin();
                const rotation = wormData.getRotation();
                const speed = wormData.getSpeed();

                let existingWorm = this.map[id];
                if (!existingWorm) {
                    let worm = new Worm({skin: skin, speed: speed, rotation: rotation, path: path, id: id, gameContext: this.gameContext});
                    this.map[id] = worm;
                    this.container.addChild(worm.container);
                } else {
                    //console.info(`updateFromServer ${path[0].x}, ${path[0].y}`);
                    existingWorm.updateFromServer({speed: speed, rotation: rotation, path: path});
                }

                //console.info(worm);


                // for (let index = path.length - 1; index >= 0; index--) {
                //     let part = path[index];
                //     let sprite = index == 0 ? this.head_sprite_factory() : this.tail_sprite_factory();
                //     console.info(sprite);
                //     // sprite.x = part.x;
                //     // sprite.y = part.y;
                //     sprite.position.set(part.x, part.y);
                //     sprite.rotation = part.r;
                //     this.container.addChild(sprite);
                //

                // let rectangle = new Graphics();
                // rectangle.beginFill(0x0033CC);
                // rectangle.lineStyle(4, 0xFF0000, 1);
                // rectangle.drawRect(0, 0, 96, 96);
                // rectangle.endFill();
                // rectangle.x = 0;
                // rectangle.y = 0;
                // rectangle.alpha = 0.5;
                // this.container.addChild(rectangle);


                //worm.container.addChild(sprite);
                //
                // }
            }
        }
    }


    updatePosition() {
        //this.container.position.set(this.gameContext.middle.x - this.coordinates.x, this.gameContext.middle.y - this.coordinates.y);
        const middleCoordinates = this.gameContext.middleCoordinates();
        this.container.position.set(middleCoordinates.x, middleCoordinates.y);
    }

    resize() {
        this.updatePosition();
    }

    update(elapsedTime) {
        this.updatePosition();
        for (const [id, worm] of Object.entries(this.map)) {
            worm.update(elapsedTime);
        }
    }

    handlePlayerResp(response) {
        const id = this.gameContext.communication.commId;

        this.updateWorm({
            id,
            path: response.getPartsList(),
            rotation: response.getRotation(),
            speed: response.getSpeed(),
            length: response.getLength(),
            skin: this.gameContext.controls.skin
        });

        // TODO zoptimalizovat - staci jenom kdyz se zmeni length, pridat do protobufu lengthChanged priznak?
        this.gameContext.controls.scoreUpdateSubject.next({id, length: response.getLength(), currentPlayer: true, type: 'update'});
        // TimeInfo timeInfo = 1;
        // float x = 2;
        // float y = 3;
        // float rotation = 4;
        // //float rotationAsked = 5;
        // uint32 length = 6;
        // repeated Part parts = 7;

    }

    updateWorm({id, path = [], skin, rotation, speed, length} = {}) {
        const translatedPath = path.map(part => {
            return { x: part.getX(), y: part.getY(), r: part.getRotation() };
        });
        // const skin = wormData.getSkin();
        // const rotation = wormData.getRotation();
        // const speed = wormData.getSpeed();

        const existingWorm = this.map[id];
        if (!existingWorm) {
            const worm = new Worm({skin, speed, rotation, path: translatedPath, id, length,
            gameContext: this.gameContext});
            this.map[id] = worm;
            this.container.addChild(worm.container);
        } else {
            //console.info(`updateFromServer ${path[0].x}, ${path[0].y}`);
            existingWorm.updateFromServer({speed, rotation, path: translatedPath});
        }
    }
}

export default Worms;
