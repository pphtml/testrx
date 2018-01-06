import { Sprite, Container, Graphics, loader, filters, BLEND_MODES } from 'pixi.js'
import layers from './layers'
const rgbDimmer = require('../computation/rgbColor').rgbDimmer;
let resources = loader.resources;

const COLORS = [
    0xff0000,
    0xffff00,
    0xff00ff,
    0x00ff00,
    0x00ffff,
    0x0000ff,
    0x80ffff,
    0xff80ff,
    0xffff80,
    0xffffff,
    //0xffcad4
    0xFF8C00 // orange
];

const FLASHING_SPEED = 0.0117647;

class NPCS {
    constructor(gameContext) {
        this.gameContext = gameContext;
        this.container = new Container();
        this.dots = {};

        this.gameContext.communication.subject.filter(msg => msg.hasDotsupdate()).subscribe(
            (msg) => {
                //console.info(`velikost update: ${msg.getDotsupdate().getDotsList().length}`);
                this.addPositions(msg.getDotsupdate().getDotsList());
            },
            (err) => console.log(err),
            () => console.log('complete')
        );

        this.gameContext.communication.subject.filter(msg => msg.hasPlayerresp()).subscribe(
            msg => {
                const playerResp = msg.getPlayerresp();
                if (playerResp.getEatenfoodList().length > 0) {
                    //console.info(`Eaten food: ${playerResp.getEatenfoodList()}`);
                    this.eatPositions(playerResp.getEatenfoodList());
                }
            }
        );
    }

    eatPositions(positions) {
        const removals = new Set();
        positions.forEach(position => {
            const key = `${position.getX()},${position.getY()}`;
            removals.add(key);
        });

        for (let i = this.container.children.length - 1; i >= 0; i--) {
            const sprite = this.container.children[i];
            // let x = sprite.x, y = sprite.y;
            // let key = `${x},${y}`;
            if (removals.has(sprite._key)) {
                delete this.dots[sprite._key];
                this.container.removeChild(sprite);
            }
        }
    }

    addPositions(positions) {
        positions.forEach(position => {
            let key = `${position.getX()},${position.getY()}`;
            if (!(key in this.dots)) {
                const color = this.translateColor(position.getColor());

                // const circle = new Graphics();
                // circle.beginFill(color);
                // circle.drawCircle(0, 0, 48);
                // circle.endFill();
                // circle.position.set(position.x, position.y);
                // circle.displayGroup = layers.npcLayer;
                // circle._type = 'circle';
                // circle._key = key;
                // circle.alpha = 0.025;
                // this.container.addChild(circle);
                const outer = new Sprite(resources['images/spritesheet.json'].textures['myfood-outer.png']);
                outer._key = key;
                outer._type = 'circle';
                outer.position.set(position.getX(), position.getY());
                outer.anchor.set(0.5, 0.5);
                outer.scale.set(1.5, 1.5);
                outer.tint = color;
                outer.alpha = 0.02;
                // dot.intensity = (Math.random() * 0.5) + 0.5;
                // dot.tintDir = [-FLASHING_SPEED, FLASHING_SPEED][Math.floor((Math.random() * 2))];
                outer.blendMode = BLEND_MODES.ADD;
                outer.displayGroup = layers.npcLayer;
                this.container.addChild(outer);

                const dot = new Sprite(resources['images/spritesheet.json'].textures['myfood.png']);
                dot._key = key;
                dot._type = 'dot';
                dot.position.set(position.getX(), position.getY());
                dot.anchor.set(0.5, 0.5);
                dot.scale.set(0.15, 0.15);
                dot.baseColor = color;
                dot.intensity = (Math.random() * 0.5) + 0.5;
                dot.tintDir = [-FLASHING_SPEED, FLASHING_SPEED][Math.floor((Math.random() * 2))];
                dot.blendMode = BLEND_MODES.ADD;
                //dot.blendMode = BLEND_MODES.ADD_NPM;
                //dot.blendMode = BLEND_MODES.SCREEN_NPM;
                dot.displayGroup = layers.npcLayer;
                this.container.addChild(dot);
                this.dots[key] = dot;
            }
        });

        const viewPortDots = new Set();
        positions.forEach(position => {
            const key = `${position.getX()},${position.getY()}`;
            viewPortDots.add(key);
        });

        for (let i = this.container.children.length - 1; i >= 0; i--) {
            const sprite = this.container.children[i];
            // let x = sprite.x, y = sprite.y;
            // let key = `${x},${y}`;
            if (!viewPortDots.has(sprite._key)) {
                //console.info('')
                delete this.dots[sprite._key];
                this.container.removeChild(sprite);
            }
        }
    }

    translateColor(color) {
        return COLORS[color];
    }

    update() {
        let middleCoordinates = this.gameContext.middleCoordinates();
        this.container.position.set(middleCoordinates.x, middleCoordinates.y);

        for (let i = this.container.children.length - 1; i >= 0; i--) {
            const dot = this.container.children[i];
            if (dot.hasOwnProperty('intensity')) {
                dot.intensity += dot.tintDir;
                if (dot.intensity >= 1.0) {
                    dot.tintDir = -FLASHING_SPEED;
                } else if (dot.intensity <= 0.5) {
                    dot.tintDir = FLASHING_SPEED;
                }
                dot.tint = rgbDimmer(dot.baseColor, dot.intensity);
            }
        }
    }
}

export default NPCS;
