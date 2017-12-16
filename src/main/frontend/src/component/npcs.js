import { Sprite, Container, Graphics, loader, filters, BLEND_MODES } from 'pixi.js'
import layers from './layers'
let resources = loader.resources;


class NPCS {
    constructor(gameContext) {
        /*this.blurFilter = new filters.BlurFilter();
        this.blurFilter.blur = 20;*/
        //this.glowFilter = new filters.GlowFilter(15, 2, 1, 0x66FF66, 0.5);
        // this.glowFilter.quality = 0.1;
        // this.glowFilter.color = 0x66FF66;

        this.gameContext = gameContext;
        this.container = new Container();

        // let dot = new Sprite(resources["images/scene.json"].textures["dot.png"]);
        // dot.anchor.set(0.5, 0.5);
        // dot.scale.set(0.2, 0.2);
        //dot.tint = 0x66FF66;
        //dot.filters = [this.glowFilter];
        // this.container.filters = [this.glowFilter];
        //this.container.addChild(dot);


        // var mask = new Graphics();
        // mask.beginFill(0x000000);
        // mask.drawRect(256, 112, 100, 100);
        // mask.endFill();


        // let dot2 = graphics.beginFill(0xFF9900);
        // graphics.drawCircle(x-8, y-8, 16);
        // graphics.endFill();

        // let circle = new Graphics();
        // //circle.fillAlpha = 0.9;
        // circle.alpha = 0.3;
        // circle.beginFill(0x66FF66);
        // circle.drawCircle(0, 0, 8);
        // circle.endFill();
        // //circle.filters = [this.glowFilter];
        // circle.x = 256;
        // circle.y = 112;
        // circle.mask = null;
        // this.container.addChild(circle);
        //
        // let circle2 = new Graphics();
        // circle2.alpha = 0.1;
        // circle2.beginFill(0x66FF66);
        // circle2.drawCircle(0, 0, 50);
        // circle2.endFill();
        // //circle.filters = [this.glowFilter];
        // circle2.x = 256;
        // circle2.y = 112;
        // this.container.addChild(circle2);

        this.dots = {};

        this.gameContext.communication.subject.filter(msg => msg.dotsUpdate).subscribe(
            (msg) => this.addPositions(msg.dotsUpdate.dots),
            (err) => console.log(err),
            () => console.log('complete')
        );

        this.gameContext.communication.subject.filter(msg => msg.eatenFood).subscribe(
            (msg) => this.eatPositions(msg.eatenFood.dots),
            (err) => console.log(err),
            () => console.log('complete')
        );
    }

    eatPositions(positions) {
        let removals = {};
        positions.forEach(position => {
            let key = `${position.x},${position.y}`;
            removals[key] = position;
        });

        for (var i = this.container.children.length - 1; i >= 0; i--) {
            let sprite = this.container.children[i];
            let x = sprite.x, y = sprite.y;
            let key = `${x},${y}`;
            if (key in removals) {
                delete this.dots[key];
                this.container.removeChild(sprite);
            }
        }
    }

    addPositions(positions) {
        positions.forEach(position => {
            let key = `${position.x},${position.y}`;
            if (!(key in this.dots)) {
                //let dot = new Sprite(resources["images/glowing-dot2white.png"].texture);
                let dot = new Sprite(resources["images/sprites.json"].textures['glowing-dot2white.png']);

                dot.x = position.x;
                dot.y = position.y;
                dot.anchor.set(0.5, 0.5);
                dot.scale.set(0.25, 0.25);
                // dot.scale.set(0.5, 0.5);
                //dot.tint = 0xff0000;
                dot.tint = this.translateColor(position.c);
                //dot.blendMode = BLEND_MODES.ADD;
                //dot.blendMode = BLEND_MODES.ADD_NPM;
                dot.blendMode = BLEND_MODES.SCREEN_NPM;
                dot.displayGroup = layers.npcLayer;
                this.container.addChild(dot);
                this.dots[key] = dot;
            }
        });

        let left = this.gameContext.player.coordinates.x - this.gameContext.width / 2,
            right = this.gameContext.player.coordinates.x + this.gameContext.width / 2,
            top = this.gameContext.player.coordinates.y - this.gameContext.height / 2,
            bottom = this.gameContext.player.coordinates.y + this.gameContext.height / 2;

        for (var i = this.container.children.length - 1; i >= 0; i--) {
            let sprite = this.container.children[i];
            let x = sprite.x, y = sprite.y;

            if (x < left || x > right || y < top || y > bottom) {
                let key = `${x},${y}`;
                delete this.dots[key];
                this.container.removeChild(sprite);
            }
        }
        // console.info(`length: ${this.container.children.length}`);
    }

    translateColor(color) {
        var result;
        switch(color) {
            case 'red':
                result = 0xff0000;
                break;
            case 'green':
                result = 0x00ff00;
                break;
            case 'blue':
                result = 0x0000ff;
                break;
            default:
                result = 0xffffff;
        }
        return result;
    }

    update() {
        //this.container.position.set(this.gameContext.player.coordinates.x, this.gameContext.player.coordinates.y);
        let x = this.gameContext.middle.x - this.gameContext.player.coordinates.x;
        let y = this.gameContext.middle.y - this.gameContext.player.coordinates.y;
        this.container.position.set(x, y);
        //console.info(this.gameContext.player.coordinates.x, this.gameContext.player.coordinates.y);
    }
}

export default NPCS;