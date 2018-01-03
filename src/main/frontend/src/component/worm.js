import { Sprite, Container, loader } from 'pixi.js'
import layers from './layers'
const moveSnake = require('./wormMovement').moveSnake;


let resources = loader.resources;

const LENGTH_PER_PART = 10;

class Worm {
    constructor({skin, speed = 1.0, rotation = 0.0, path = [], id = 'noname', gameContext, length = 150, doNotDraw = false} = {}) {
        this.coordinates = path.length == 0 ? {x: undefined, y: undefined} : {x: path[0].x, y: path[0].y};
        this.skinColor = skin;
        this.angle = rotation;
        this.partDistance = 20.0;
        this.path = path;
        this.speed = speed;
        this.gameContext = gameContext;
        this.length = length;
        this.id = id;
        if (this.path.length == 0) {
            for (let index = 0, lengthIndex = 1; lengthIndex <= this.length; index++, lengthIndex += LENGTH_PER_PART) {
                this.path.push({x: -this.partDistance * index, y: 0.0, r: 0});
            }
        }
        if (!doNotDraw) {
            this.container = new Container();
            for (let index = this.path.length-1; index >= 0; index--) {
                const part = this.path[index];
                //const sprite = index == 0 ? this.head_sprite_factory() : this.tail_sprite_factory();
                const sprite = this.sprite_factory();
                sprite.pathIndex = index;
                sprite.position.set(part.x, part.y);
                sprite.rotation = part.r;
                sprite.tint = this.skinColor;
                //sprite.tint = 0x802020;
                this.container.addChild(sprite);
                //this.sprites.push(sprite);
            }

            const eyeLeft = new Sprite(resources['images/spritesheet.json'].textures['eye.png']);
            eyeLeft.metaInf = 'eye';
            eyeLeft.scale.set(0.4, 0.4);
            eyeLeft.pivot.set(-5, 47);
            eyeLeft.displayGroup = layers.tailLayer;
            // eyeLeft.position.set(0, 0);
            this.container.addChild(eyeLeft);

            const eyeRight = new Sprite(resources['images/spritesheet.json'].textures['eye.png']);
            eyeRight.metaInf = 'eye';
            eyeRight.scale.set(0.4, 0.4);
            eyeRight.pivot.set(-5, -4);
            eyeRight.displayGroup = layers.tailLayer;
            //eyeRight.position.set(0, 0);
            this.container.addChild(eyeRight);

            this.gameContext.stage.addChild(this.container);
        }
    }
    // spriteNameHead() {
    //     return `basic_head_${this.skin}.png`;
    // }
    //
    // spriteNameTail() {
    //     return `basic_tail_${this.skin}.png`;
    // }

    // head_sprite_factory = () => {
    //     const head = new Sprite(resources['images/spritesheet.json'].textures['tail-mod2-white.png']);
    //     //let head = new Sprite(resources["images/sprites.json"].textures[this.spriteNameHead()]);
    //     head.scale.set(0.4, 0.4);
    //     head.anchor.set(0.5, 0.5);
    //     //head.displayGroup = layers.headLayer;
    //     head.displayGroup = layers.tailLayer; //layers.headLayer;
    //     return head;
    // }
    //
    // tail_sprite_factory = () => {
    //     const tail = new Sprite(resources['images/spritesheet.json'].textures['tail-mod2-white.png']);
    //     //let tail = new Sprite(resources["images/sprites.json"].textures[this.spriteNameTail()]);
    //     tail.anchor.set(0.5, 0.5);
    //     tail.scale.set(0.4, 0.4);
    //     tail.displayGroup = layers.tailLayer;
    //     return tail;
    // };

    sprite_factory = () => {
        const sprite = new Sprite(resources['images/spritesheet.json'].textures['tail-mod2-white.png']);
        //let tail = new Sprite(resources["images/sprites.json"].textures[this.spriteNameTail()]);
        sprite.anchor.set(0.5, 0.5);
        sprite.scale.set(0.4, 0.4);
        sprite.displayGroup = layers.tailLayer;
        return sprite;
    };

    update(elapsedTime) {
        // if (this.gameContext.communication.commId != this.id) {
        //     this.path[0].x = 0.0, this.path[0].y = 0.0;
        //     //debugger;
        // }
        const missingParts = Math.floor(this.length / LENGTH_PER_PART) - this.path.length;
        if (missingParts > 0) {
            //console.info(`nesedi delka -> musi se pridat ${missingParts}`);
            const originalLength = this.path.length;
            for (let index = 0; index < missingParts; index++) {
                const endPart = this.path[this.path.length-1];
                const part = JSON.parse(JSON.stringify(endPart));
                this.path.push(part);

                const sprite = this.sprite_factory();
                sprite.pathIndex = index + originalLength;
                sprite.position.set(part.x, part.y);
                sprite.rotation = part.r;
                sprite.tint = this.skinColor;
                this.container.addChildAt(sprite, 0);
            }
        }

        let newPath = moveSnake(this.path, this.angle, this.speed, this.partDistance);
        this.path = newPath.path;

        for (let indexSprite=this.container.children.length-1; indexSprite>=0; indexSprite--) {
            //console.info(indexSprite, indexPath);
            const sprite = this.container.children[indexSprite];
            //debugger;
            if (sprite.hasOwnProperty('pathIndex')) {
                const part = this.path[sprite.pathIndex];
                sprite.position.set(part.x, part.y);
                sprite.rotation = part.r;
                //sprite.zOrder = indexSprite;
            } else if (sprite.hasOwnProperty('metaInf')) {
                const part = this.path[0];
                sprite.position.set(part.x, part.y);
                sprite.rotation = part.r;
            }
        }
    }

    updateFromServer({speed = 1.0, rotation = 0.0, path = []} = {}) {
        this.coordinates = {x: path[0].x, y: path[0].y};
        this.angle = rotation;
        //this.partDistance = 20.0;
        this.path = path;
        this.speed = speed;
    }
}

export default Worm;