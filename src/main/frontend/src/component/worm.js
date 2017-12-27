import { Sprite, Container, loader } from 'pixi.js'
import layers from './layers'
let moveSnake = require('./wormMovement').moveSnake;


let resources = loader.resources;

class Worm {
    constructor({skin, speed = 1.0, rotation = 0.0, path = [], id = 'noname', gameContext} = {}) {
        this.coordinates = path.length == 0 ? {x: undefined, y: undefined} : {x: path[0].x, y: path[0].y};
        this.skin = skin;
        this.angle = rotation;
        this.partDistance = 20.0;
        this.path = path;
        this.speed = speed;
        this.gameContext = gameContext;
        this.id = id;
        if (this.path.length == 0) {
            for (var index = 0; index < 15; index++) {
                this.path.push({x: -this.partDistance * index, y: 0.0, r: 0});
            }
        }
        this.container = new Container();
        for (var index = this.path.length-1; index >= 0; index--) {
            let part = this.path[index];
            let sprite = index == 0 ? this.head_sprite_factory() : this.tail_sprite_factory();
            sprite.position.set(part.x, part.y);
            sprite.rotation = part.r;
            this.container.addChild(sprite);
            //this.sprites.push(sprite);
        }
    }
    spriteNameHead() {
        return `basic_head_${this.skin}.png`;
    }

    spriteNameTail() {
        return `basic_tail_${this.skin}.png`;
    }

    head_sprite_factory = () => {
        let head = new Sprite(resources["images/sprites.json"].textures[this.spriteNameHead()]);
        head.scale.set(0.4, 0.4);
        head.anchor.set(0.5, 0.5);
        head.displayGroup = layers.headLayer;
        //head.displayGroup = layers.tailLayer; //layers.headLayer;
        return head;
    }

    tail_sprite_factory = () => {
        let tail = new Sprite(resources["images/sprites.json"].textures[this.spriteNameTail()]);
        tail.anchor.set(0.5, 0.5);
        tail.scale.set(0.4, 0.4);
        tail.displayGroup = layers.tailLayer;
        return tail;
    };

    update(elapsedTime) {
        // if (this.gameContext.communication.commId != this.id) {
        //     this.path[0].x = 0.0, this.path[0].y = 0.0;
        //     //debugger;
        // }

        let newPath = moveSnake(this.path, this.angle, this.speed, this.partDistance);
        this.path = newPath.path;

        for (let indexSprite=this.container.children.length-1, indexPath=0; indexSprite>=0; indexSprite--, indexPath++) {
            //console.info(indexSprite, indexPath);
            let part = this.path[indexPath];
            let sprite = this.container.children[indexSprite];
            sprite.position.set(part.x, part.y);
            sprite.rotation = part.r;
            //sprite.zOrder = indexSprite;
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