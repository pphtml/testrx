import rx from 'rxjs'
import shortid from 'shortid';

// import { QueueingSubject } from 'queueing-subject'
// import websocketConnect from 'rxjs-websockets'

class Communication {
    constructor(gameContext) {
        this.gameContext = gameContext;
        //this.input = new QueueingSubject();
        this.commId = shortid.generate();

        let originalUrlChunk = /http(s?:\/\/.*?\/)/g.exec(window.location.href)[1];
        let wsUrl = `ws${originalUrlChunk}dot?id=${this.commId}`;
        // console.info(wsUrl);
        this.subject = rx.Observable.webSocket(wsUrl);
        // subject.subscribe(
        //     (msg) => console.log('message received: ' + JSON.stringify(msg)),
        //     (err) => console.log(err),
        //     () => console.log('complete')
        // );

        //this.subject.next(JSON.stringify({ op: 'hello' }));


        // const { messages, connectionStatus } = websocketConnect(wsUrl, this.input);
        // this.input.next('ahoj abc 123');

        // let keyDowns = rx.Observable.fromEvent(document, 'keydown');
        // let keyUps = rx.Observable.fromEvent(document, 'keyup');
        // this.keyActions = rx.Observable.merge(keyDowns, keyUps);
    }
}

export default Communication