import { Sprite, Container, loader } from 'pixi.js'
import layers from './layers'
import Controls from './controls'
let moveSnake = require('./wormMovement').moveSnake;


let resources = loader.resources;

class Worm {
    constructor(gameContext, spriteName) {
        this.gameContext = gameContext;
        this.coordinates = {x: undefined, y: undefined};
        this.spriteName = spriteName;
        //this.spriteHead = this.head_sprite_factory();
        //new Sprite(resources["images/scene.json"].textures[this.spriteNameHead()]);
        this.lastAngle = 0.0;
        this.partDistance = 20.0;
        this.path = [];
        for (var index = 0; index < 15; index++) {
            this.path.push({x: -this.partDistance * index, y: 0.0, rotation: 0});
        }
        this.container = new Container();
        this.sprites = [];
        for (var index = 0; index < this.path.length; index++) {
            let part = this.path[index];
            let sprite = index == 0 ? this.head_sprite_factory() : this.tail_sprite_factory();
            sprite.position.set(part.x, part.y);
            sprite.rotation = part.rotation;
            this.container.addChild(sprite);
            this.sprites.push(sprite);
        }
    }
    spriteNameHead() {
        return `${this.spriteName}_head.png`;
    }

    spriteNameTail() {
        return `${this.spriteName}_tail.png`;
    }

    head_sprite_factory = () => {
        if (this.spriteName == 'basic_body') {
            let head = new Sprite(resources["images/sprites.json"].textures['basic_head.png']);
            head.scale.set(0.4, 0.4);
            head.anchor.set(0.5, 0.5);
            head.displayGroup = layers.headLayer;
            return head;
            /*let headContainer = new Container();
            let head = new Sprite(resources["images/scene.json"].textures[`${this.spriteName}.png`]);
            headContainer.addChild(head);
            return headContainer;*/
        } else {
            let head = new Sprite(resources["images/scene.json"].textures[this.spriteNameHead()]);
            head.scale.set(0.5, 0.5);
            head.anchor.set(0.5, 0.5);
            head.displayGroup = layers.headLayer;
            return head;

        }
    }


/*    spritesTail = [];
    path = [];
    length = 32;*/

    tail_sprite_factory = () => {
        if (this.spriteName == 'basic_body') {
            let head = new Sprite(resources["images/sprites.json"].textures['basic_tail.png']);
            head.anchor.set(0.5, 0.5);
            head.scale.set(0.4, 0.4);
            head.displayGroup = layers.tailLayer;
            return head;
        } else {
            let sprite = new Sprite(resources["images/scene.json"].textures[this.spriteNameTail()]);
            sprite.anchor.set(0.5, 0.5);
            sprite.scale.set(0.5, 0.5);
            sprite.displayGroup = layers.tailLayer;
            return sprite;
        }
    };

    update(askedAngle, elapsedTime) {
        let baseSpeed = 1.0;
        let speed = 5.0 * (this.gameContext.controls.isMouseDown() ? baseSpeed * 2 : baseSpeed); // * elapsedTime * 0.06;
        let angle = Controls.computeAllowedAngle(askedAngle, this.lastAngle, elapsedTime, this.gameContext, baseSpeed, speed);
        this.lastAngle = angle;

        let newPath = moveSnake(this.path, angle, speed, this.partDistance);
        this.path = newPath.path;

        //console.info(this.path);

        for (var index = 0; index < this.path.length; index++) {
            let part = this.path[index];
            let sprite = this.sprites[index];
            sprite.x = part.x;
            sprite.y = part.y;
            sprite.rotation = part.rotation;
            sprite.zOrder = index;
        }



    }
}

export default Worm;