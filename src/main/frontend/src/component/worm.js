import { Sprite, Container, loader } from 'pixi.js'
import layers from './layers'
let moveSnake = require('./wormMovement').moveSnake;


let resources = loader.resources;

class Worm {
    constructor({skin, speed = 1.0, rotation = 0.0, path = []} = {}) {
        // this.gameContext = gameContext;
        this.coordinates = path.length == 0 ? {x: undefined, y: undefined} : {x: path[0].x, y: path[0].y};
        this.spriteName = skin;
        //this.spriteHead = this.head_sprite_factory();
        //new Sprite(resources["images/scene.json"].textures[this.spriteNameHead()]);
        this.angle = rotation;
        this.partDistance = 20.0;
        this.path = path;
        this.speed = speed;
        if (this.path.length == 0) {
            for (var index = 0; index < 15; index++) {
                this.path.push({x: -this.partDistance * index, y: 0.0, r: 0});
            }
        }
        this.container = new Container();
        this.sprites = [];
        for (var index = 0; index < this.path.length; index++) {
            let part = this.path[index];
            let sprite = index == 0 ? this.head_sprite_factory() : this.tail_sprite_factory();
            sprite.position.set(part.x, part.y);
            sprite.rotation = part.r;
            this.container.addChild(sprite);
            this.sprites.push(sprite);
        }
    }
    spriteNameHead() {
        return `basic_head_${this.spriteName}.png`;
    }

    spriteNameTail() {
        return `basic_tail_${this.spriteName}.png`;
    }

    head_sprite_factory = () => {
        let head = new Sprite(resources["images/sprites.json"].textures[this.spriteNameHead()]);
        head.scale.set(0.4, 0.4);
        head.anchor.set(0.5, 0.5);
        head.displayGroup = layers.headLayer;
        return head;
    }


/*    spritesTail = [];
    path = [];
    length = 32;*/

    tail_sprite_factory = () => {
        let head = new Sprite(resources["images/sprites.json"].textures[this.spriteNameTail()]);
        head.anchor.set(0.5, 0.5);
        head.scale.set(0.4, 0.4);
        head.displayGroup = layers.tailLayer;
        return head;
    };

    update(elapsedTime) {
        let newPath = moveSnake(this.path, this.angle, this.speed, this.partDistance);
        this.path = newPath.path;

        //console.info(this.path);

        for (var index = 0; index < this.path.length; index++) {
            let part = this.path[index];
            let sprite = this.sprites[index];
            sprite.x = part.x;
            sprite.y = part.y;
            sprite.rotation = part.r;
            sprite.zOrder = index;
        }
    }
}

export default Worm;