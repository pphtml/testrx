import { Sprite, Container, Graphics, loader, filters, BLEND_MODES } from 'pixi.js'
import Worm from './worm'
import layers from './layers'
let resources = loader.resources;

class Worms {
    constructor(gameContext) {
        this.gameContext = gameContext;
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

        // this.gameContext.communication.subject.filter(msg => msg.snakesUpdate).subscribe(
        //     (msg) => this.updateWorms(msg.snakesUpdate.snakes)
        // );
    }

    head_sprite_factory = () => {
        let head = new Sprite(resources["images/sprites.json"].textures['basic_head_darkred.png']);
        head.scale.set(0.4, 0.4);
        head.anchor.set(0.5, 0.5);
        head.displayGroup = layers.headLayer;
        return head;
    }

    tail_sprite_factory = () => {
        let head = new Sprite(resources["images/sprites.json"].textures['basic_tail_darkred.png']);
        head.scale.set(0.4, 0.4);
        head.anchor.set(0.5, 0.5);
        head.displayGroup = layers.headLayer;
        return head;
    }

    updateWorms(worms) {
        for (let i = this.container.children.length - 1; i >= 0; i--) {
            let sprite = this.container.children[i];
            this.container.removeChild(sprite);
        }

        //let that = this;
        for (let id in worms) {
            if (this.gameContext.communication.commId != id) {
                let wormData = worms[id];
                //console.info(`${id} -> ${JSON.stringify(wormData)}`);
                let path = wormData.path;
                let skin = wormData.skin;
                let rotation = wormData.rotation;
                let speed = wormData.speed;

                //debugger;
                let worm = new Worm({skin: skin, speed: speed, rotation: rotation, path: path});
                this.map[id] = worm;

                //console.info(worm);
                this.container.addChild(worm.container);


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
                //     let rectangle = new Graphics();
                //     rectangle.beginFill(0x0033CC);
                //     rectangle.lineStyle(4, 0xFF0000, 1);
                //     rectangle.drawRect(0, 0, 96, 96);
                //     rectangle.endFill();
                //     rectangle.x = 0;
                //     rectangle.y = 0;
                //     rectangle.alpha = 0.5;
                //     worm.container.addChild(rectangle);



                //worm.container.addChild(sprite);
                //
                // }
            }
        }
    }

    update(elapsedTime) {
        let middleCoordinates = this.gameContext.middleCoordinates();
        this.container.position.set(middleCoordinates.x, middleCoordinates.y);
        //console.info(this.gameContext.player.coordinates.x, this.gameContext.player.coordinates.y);
        for (const [id, worm] of Object.entries(this.map)) {
            worm.update(elapsedTime);
        }
    }
}

export default Worms;
