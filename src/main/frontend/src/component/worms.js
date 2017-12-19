import { Sprite, Container, Graphics, loader, filters, BLEND_MODES } from 'pixi.js'
import layers from './layers'
let resources = loader.resources;

class Worms {
    constructor(gameContext) {
        this.gameContext = gameContext;
        this.container = new Container();

        // let rectangle = new Graphics();
        // rectangle.beginFill(0x0033CC);
        // rectangle.lineStyle(4, 0xFF0000, 1);
        // rectangle.drawRect(0, 0, 96, 96);
        // rectangle.endFill();
        // rectangle.x = 64;
        // rectangle.y = 64;
        // rectangle.alpha = 0.5;
        // this.container.addChild(rectangle);

        this.gameContext.communication.subject.filter(msg => msg.snakesUpdate).subscribe(
            (msg) => this.drawWorms(msg.snakesUpdate.snakes)
        );
    }

    head_sprite_factory = () => {
        let head = new Sprite(resources["images/sprites.json"].textures['basic_head_darkred.png']);
        head.scale.set(0.4, 0.4);
        head.anchor.set(0.5, 0.5);
        // head.displayGroup = layers.headLayer;
        return head;
    }

    tail_sprite_factory = () => {
        let head = new Sprite(resources["images/sprites.json"].textures['basic_tail_darkred.png']);
        head.scale.set(0.4, 0.4);
        head.anchor.set(0.5, 0.5);
        // head.displayGroup = layers.headLayer;
        return head;
    }

    drawWorms(worms) {
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

                for (let index = path.length - 1; index >= 0; index--) {
                //path.forEach(part => {
                    let part = path[index];
                    let sprite = index == 0 ? this.head_sprite_factory() : this.tail_sprite_factory();
                    // sprite.x = part.x;
                    // sprite.y = part.y;
                    sprite.position.set(part.x, part.y);
                    sprite.rotation = part.r;
                    this.container.addChild(sprite);

                    /*let rectangle = new Graphics();
                    rectangle.beginFill(0x0033CC);
                    rectangle.lineStyle(4, 0xFF0000, 1);
                    rectangle.drawRect(0, 0, 96, 96);
                    rectangle.endFill();
                    rectangle.x = part.x;
                    rectangle.y = part.y;
                    rectangle.alpha = 0.5;
                    that.container.addChild(rectangle);*/

                }
            }
        }
    }

    update() {
        let middleCoordinates = this.gameContext.middleCoordinates();
        this.container.position.set(middleCoordinates.x, middleCoordinates.y);
        //console.info(this.gameContext.player.coordinates.x, this.gameContext.player.coordinates.y);
    }
}

export default Worms;
