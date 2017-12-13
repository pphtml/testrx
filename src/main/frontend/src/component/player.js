import { Sprite, Container, DisplayList, DisplayGroup, loader } from 'pixi.js'
import layers from './layers'
import Controls from './controls'

let resources = loader.resources;

class Worm {
    constructor(gameContext, spriteName) {
        this.gameContext = gameContext;
        this.coordinates = {x: undefined, y: undefined};
        this.spriteName = spriteName;
        this.spriteHead = this.head_sprite_factory();
            //new Sprite(resources["images/scene.json"].textures[this.spriteNameHead()]);
        this.lastAngle = 0.0;
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
            return head;
            /*let headContainer = new Container();
            let head = new Sprite(resources["images/scene.json"].textures[`${this.spriteName}.png`]);
            headContainer.addChild(head);
            return headContainer;*/
        } else {
            let head = new Sprite(resources["images/scene.json"].textures[this.spriteNameHead()]);
            head.scale.set(0.5, 0.5);
            return head;

        }
    }


    spritesTail = [];
    path = [];
    length = 32;
    container = new Container();

    tail_sprite_factory = () => {
        if (this.spriteName == 'basic_body') {
            let head = new Sprite(resources["images/sprites.json"].textures['basic_tail.png']);
            head.anchor.set(0.5, 0.5);
            head.scale.set(0.4, 0.4);
            return head;
        } else {
            let sprite = new Sprite(resources["images/scene.json"].textures[this.spriteNameTail()]);
            sprite.anchor.set(0.5, 0.5);
            sprite.scale.set(0.5, 0.5);
            return sprite;
        }
    };

    update(askedAngle, elapsedTime) {
        let baseSpeed = 1.0;
        let speed = 5.0 * (this.gameContext.controls.isMouseDown() ? baseSpeed * 2 : baseSpeed); // * elapsedTime * 0.06;
        let angle = Controls.computeAllowedAngle(askedAngle, this.lastAngle, elapsedTime, this.gameContext, baseSpeed, speed);
        this.lastAngle = angle;
        let x_step = Math.cos(angle) * speed;
        let y_step = Math.sin(angle) * speed;

        //console.info(`angle: ${angle}, speed: ${speed}, x_step: ${x_step}, y_step: ${y_step}`);
        this.coordinates.x += x_step;
        this.coordinates.y += y_step;

        this.spriteHead.rotation = angle;
        this.path.splice(0, 0, {x: x_step, y: y_step, speed: speed, angle: angle});
        this.path.splice(200, 200);

        var x = 0, y = 0, steps = 0, stepsTotal = 0, index_sprite = 0;
        this.path.forEach(value => {
            x -= value.x;
            y -= value.y;
            steps += value.speed;
            stepsTotal += value.speed;
            if (steps >= 20) {
                steps -= 20;
                if (stepsTotal <= 199) { // stop iteration
                    if (this.spritesTail.length <= index_sprite) {
                        // console.info('adding sprite');
                        let tail = this.tail_sprite_factory();
                        tail.x = x;
                        tail.y = y;
                        tail.zOrder = stepsTotal;
                        tail.rotation = value.angle;
                        tail.displayGroup = layers.tailLayer;
                        this.spritesTail.push(tail);
                        this.container.addChild(tail);
                    } else {
                        let tail = this.spritesTail[index_sprite];
                        tail.x = x;
                        tail.y = y;
                        tail.rotation = value.angle;
                    }
                    index_sprite++;
                } else if (index_sprite < this.spritesTail.length) {
                    console.info(`zbyvajici..., index_sprite: ${index_sprite}, tail_sprites: ${this.spritesTail.length}`);
                    let forRemoval = this.spritesTail.splice(index_sprite, 1000);
                    forRemoval.forEach(sprite => this.container.removeChild(sprite));
                }
            }
        });
    }
}

class Player extends Worm {
    constructor(gameContext, spriteName = 'cow') {
        super(gameContext, spriteName);
        this.gameContext = gameContext;
        this.coordinates = {x: 0, y: 0};

        this.spriteHead.position.set(0, 0);
        this.spriteHead.anchor.set(0.5, 0.5);
        //this.spriteHead.zOrder = 0;
        this.spriteHead.displayGroup = layers.headLayer;
//        console.info(this.spriteHead);
        this.container.addChild(this.spriteHead);
//        console.info(scene_middle);
//        console.info(this.container);
//        console.info(this.spriteHead);
        this.resize();
    }

    resize() {
        this.container.position.set(this.gameContext.middle.x, this.gameContext.middle.y);
    }

    update(askedAngle, elapsedTime) {
        super.update(askedAngle, elapsedTime);
        this.gameContext.communication.subject.next(
            JSON.stringify({playerMoved:{x:this.coordinates.x.toFixed(2), y:this.coordinates.y.toFixed(2)}}));
    }

        //{"playerMoved":{"x":10,"y":30}}
}

export default Player
/*module.exports = {
    Worm: Worm,
    Player: Player
}*/



// line.clear();
// line.lineStyle(1, 0xFFFFFF, 1);
// line.moveTo(scene_middle.x, scene_middle.y);
// var time_x = scene_middle.x + Math.cos(angle) * scene_middle.y;
// var time_y = scene_middle.y - Math.sin(angle) * scene_middle.y;
// line.lineTo(time_x, time_y);
