//import { Sprite, Container, DisplayList, DisplayGroup, loader } from 'pixi.js'
import Worm from './worm'
//let resources = loader.resources;

class Player extends Worm {
    constructor(gameContext, spriteName = 'cow') {
        super(gameContext, spriteName);
        this.gameContext = gameContext;
        this.coordinates = {x: 0, y: 0};

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
        //console.info(this.path[0]);
        this.coordinates = {x: this.path[0].x, y: this.path[0].y};
        this.container.position.set(this.gameContext.middle.x - this.coordinates.x, this.gameContext.middle.y - this.coordinates.y);

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
