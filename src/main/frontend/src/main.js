import * as PIXI from 'pixi.js' // for pixi-display import
import 'pixi-display'
import 'pixi-filters'
import { Container, autoDetectRenderer, loader, DisplayList, DisplayGroup } from 'pixi.js'
import Player from './component/player'
import GameInfo from './component/gameInfo'
import Controls from './component/controls'
import Background from './component/background'
import NPCS from './component/npcs'
import Communication from "./component/communication";

PIXI.utils.skipHello();
let stage = new Container(),
    renderer = autoDetectRenderer(512, 512, {antialias: false, transparent: false, resolution: 1});
renderer.view.style.position = "absolute";
renderer.view.style.display = "block";
renderer.autoResize = true;
renderer.backgroundColor = 0x061639;

let gameContext = {
    // width: window.innerWidth,
    // height: window.innerHeight,
    // middle: { x: window.innerWidth / 2, y: window.innerHeight / 2 },
    stage: stage,
    renderer: renderer
};

stage.displayList = new DisplayList(); // zOrder

document.body.appendChild(renderer.view);

loader
//    .add("images/scene.json")
    .add("images/sprites.json")
//    .add("images/basic_head.png")
//    .add("images/background2.png")
//    .add("images/glowing-dot.png")
//    .add("images/glowing-dot2.png")
//    .add("images/glowing-dot2white.png")
    .load(setup);

function setup() {
    gameContext.communication = new Communication(gameContext);
    gameContext.controls = new Controls(gameContext);
    gameContext.controls.resizedHandler();
    let background = new Background(gameContext);
    gameContext.background = background;
    //let player = new Player(gameContext, 'cow');
    let player = new Player(gameContext, 'basic_body');
    gameContext.player = player;

    stage.addChild(player.container);
    let npcs = new NPCS(gameContext);
    stage.addChild(npcs.container);

    let gameInfo = new GameInfo(gameContext, 10, 10);
    stage.addChild(gameInfo.message);

    // var numFramesToAverage = 16;
    // var frameTimeHistory = [];
    // var frameTimeIndex = 0;
    // var totalTimeForFrames = 0;
    var before = Date.now();
    function gameLoop() {
        var now = Date.now();
        var elapsedTime = now - before;
        before = now;
        gameContext.controls.fpsSubject.next('a');

/*        totalTimeForFrames += elapsedTime - (frameTimeHistory[frameTimeIndex] || 0);
        frameTimeHistory[frameTimeIndex] = elapsedTime;
        frameTimeIndex = (frameTimeIndex + 1) % numFramesToAverage;
        var averageElapsedTime = totalTimeForFrames / numFramesToAverage;
        var fps = 1000 / averageElapsedTime;*/

        let angle = gameContext.controls.angle();

        player.update(angle, elapsedTime);
        gameInfo.update(angle);
        background.update();
        npcs.update();

        renderer.render(stage);

        requestAnimationFrame(gameLoop);
    }
    gameLoop();
}

