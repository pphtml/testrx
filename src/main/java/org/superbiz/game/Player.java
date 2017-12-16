package org.superbiz.game;

import com.github.davidmoten.rtree.geometry.Circle;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Rectangle;
import ratpack.func.Pair;
import ratpack.websocket.WebSocket;
import rx.Observable;
import rx.subjects.PublishSubject;

import static com.github.davidmoten.rtree.geometry.Geometries.circle;
import static com.github.davidmoten.rtree.geometry.Geometries.point;
import static com.github.davidmoten.rtree.geometry.Geometries.rectangle;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Player {
    private final String id;
    private final Observable<String> playerInterval;
    private final Observable<Pair<Player, Point>> periodicUpdate;
    private Point position;
    private Point viewSize;

    private WebSocket webSocket;

    private final PublishSubject<Point> observablePosition;

    public Player(String id, WebSocket webSocket) {
        this.id = id;
        this.webSocket = webSocket;
        this.observablePosition = PublishSubject.create();
        this.playerInterval = Observable.interval(500, MILLISECONDS).map(x -> "S" + x);
        this.position = point(0, 0);
        this.viewSize = point(600, 400);

        this.observablePosition.subscribe(this::onPositionChanged);

        this.periodicUpdate = this.playerInterval.withLatestFrom(this.observablePosition, (timer, position) -> Pair.of(this, position));

    }

    private void onPositionChanged(Point position) {
        this.position = position;
    }

    public Rectangle getViewport(Point position) {
        Point latestPosition = position != null ? position : this.position;
        float left = latestPosition.x() - this.viewSize.x() / 2;
        float top = latestPosition.y() - this.viewSize.y() / 2;
        float right = latestPosition.x() + this.viewSize.x() / 2;
        float bottom = latestPosition.y() + this.viewSize.y() / 2;
        return rectangle(left, top, right, bottom);
    }

    public PublishSubject<Point> getObservablePosition() {
        return observablePosition;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public Observable<String> getPlayerInterval() {
        return playerInterval;
    }

    public Observable<Pair<Player, Point>> getPeriodicUpdate() {
        return periodicUpdate;
    }

    public String getId() {
        return id;
    }

    public void setViewSize(Point viewSize) {
        this.viewSize = viewSize;
    }

    public Circle getEatingCircle(Point position) {
        return circle(position.x(), position.y(), 50);
    }
}
