import { Text } from 'pixi.js'
import layers from './layers'

class GameInfo {
    constructor(gameContext, x, y) {
        this.gameContext = gameContext;
        this.gameContext.gameInfo = this;
        this.message = new Text(
            "Hello Pixi!",
            {fontFamily: "Lato", fontSize: 20, fill: "white"}
            //{fontFamily: "'Saira Extra Condensed'", fontSize: 24, fill: "white"}
            //{fontFamily: "Arial", fontSize: 20, fill: "white"}
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
        this.gameContext.controls.fpsSubject.bufferTime(1000).subscribe(samples => this.fps = samples.length);
    }

    update(angle) {
        this.message.text = `FPS: ${this.fps}, angle: ${angle.toFixed(2)}, coords: {x: ${this.gameContext.player.coordinates.x.toFixed(2)}, y: ${this.gameContext.player.coordinates.y.toFixed(2)}}`;
        //this.message.text = `coordinates: ${JSON.stringify(this.gameContext.player.coordinates)}, d: ${this.infoDisplayed}`;
        //this.message.text = `coordinates: ${JSON.stringify(player.coordinates)}, md: ${mouseDown}, d: ${infoDisplayed}`;
        this.message.visible = this.infoDisplayed;
    }
}

export default GameInfo
