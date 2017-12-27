import {Text, Graphics, Sprite, loader, BLEND_MODES} from 'pixi.js'
import layers from './layers'
import FeatureMatrix from './featureMatrix'
let resources = loader.resources;

const featureMatrix = new FeatureMatrix();

class GameInfo {
    constructor(gameContext, x, y) {
        this.gameContext = gameContext;
        this.gameContext.gameInfo = this;
        this.message = new Text(
            "Hello Pixi!",
            featureMatrix.googleFont ?
            {fontFamily: "'Saira Extra Condensed'", fontSize: 24, fill: "white"} :
            {fontFamily: "Arial", fontSize: 20, fill: "white"}
        );
        this.coordinates = {x: x, y: y};
        this.message.position.set(this.coordinates.x, this.coordinates.y);
        this.message.displayGroup = layers.npcLayer;
        this.infoDisplayed = false;

        this.gameContext.controls.keyActions.subscribe(event => {
            if (event.type == 'keydown' && event.code == 'KeyI') {
                this.infoDisplayed = !this.infoDisplayed;
            }
        });

        this.fps = 0;
        this.roundTrip = '?';
        this.gameContext.controls.fpsSubject.bufferTime(1000).subscribe(samples => this.fps = samples.length);

        // let rectangle = new Graphics();
        // rectangle.beginFill(0x0033CC);
        // rectangle.lineStyle(4, 0xFF0000, 1);
        // rectangle.drawRect(0, 0, 96, 96);
        // rectangle.endFill();
        // rectangle.x = 64;
        // rectangle.y = 64;
        // rectangle.alpha = 0.5;
        // this.gameContext.stage.addChild(rectangle);


        // let circle = new Graphics();
        // //circle.beginFill(0xFF9933);
        // circle.beginFill(0xFF0000);
        // circle.alpha = 0.1;
        // //circle.lineStyle(4, 0x006600, 1);
        // circle.drawCircle(0, 0, 48);
        // circle.endFill();
        // circle.x = 100;
        // circle.y = 100;
        // this.gameContext.stage.addChild(circle);
        //
        // circle = new Graphics();
        // circle.beginFill(0xFF0000);
        // circle.alpha = 0.2;
        // circle.drawCircle(0, 0, 24);
        // circle.endFill();
        // circle.x = 100;
        // circle.y = 100;
        // this.gameContext.stage.addChild(circle);

        // this.tintDir = 10;
        this.baseColor = 0x010000;
        this.intensity = 130;

        this.foodSprite = new Sprite(resources['images/myfood.png'].texture);
        //let foodSprite = new Sprite(resources['images/food.json'].textures['food1.png']);
        this.foodSprite.x = 64;
        this.foodSprite.y = 64;
        this.foodSprite.scale.set(0.1, 0.1);
        this.foodSprite.tint = 0x800000;
        this.foodSprite.blendMode = BLEND_MODES.ADD;
        //foodSprite.blendMode = BLEND_MODES.ADD_NPM;
        //foodSprite.blendMode = BLEND_MODES.SCREEN_NPM;

        this.gameContext.stage.addChild(this.foodSprite);
    }

    update(angle) {
        this.message.text = `FPS: ${this.fps}, angle: ${angle.toFixed(2)}, coords: {x: ${this.gameContext.player.coordinates.x.toFixed(2)}, y: ${this.gameContext.player.coordinates.y.toFixed(2)}, roundTrip: ${this.roundTrip}`;
        //this.message.text = `coordinates: ${JSON.stringify(this.gameContext.player.coordinates)}, d: ${this.infoDisplayed}`;
        //this.message.text = `coordinates: ${JSON.stringify(player.coordinates)}, md: ${mouseDown}, d: ${infoDisplayed}`;
        this.message.visible = this.infoDisplayed;

        if (this.intensity == 250) {
            this.tintDir = -5;
        } else if (this.intensity == 130) {
            this.tintDir = 5;
        }
        this.intensity += this.tintDir;
        let color = this.baseColor * this.intensity;
        this.foodSprite.tint = color;
    }
}

export default GameInfo
