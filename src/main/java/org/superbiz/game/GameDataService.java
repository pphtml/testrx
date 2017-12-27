package org.superbiz.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Circle;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Rectangle;
import org.superbiz.game.msg.*;
import ratpack.func.Pair;
import rx.Observable;

import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;

import static com.github.davidmoten.rtree.geometry.Geometries.point;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class GameDataService {
    private final int radius;
    private final int powerRadius;
    private final SnakePositions snakePositions;
    private final Observable<String> snakesInterval;
    private final Observable<SnakesUpdate> snakeUpdate;
    @Inject
    private Logger logger;

    private final int width;
    private final int height;
    private final Random random;
    private final int widthDouble;
    private final int heightDouble;
    private final int colorCount;
    private final int levelCount;

    private final ObjectMapper mapper = new ObjectMapper();
    private final WorldGeometry worldGeometry;


    //private Map<String, Dot> dots = new HashMap<>();
    private RTree<Dot, Point> dotTree;

    @Inject
    public GameDataService(WorldGeometry worldGeometry, SnakePositions snakePositions) {
        this.worldGeometry = worldGeometry;
        this.snakePositions = snakePositions;
         dotTree = RTree.maxChildren(5).create();
        //dotTree = dotTree.add("DAVE", point(10, 20))


        this.radius = 3000;
        this.powerRadius = this.radius * this.radius;

        this.width = radius;
        this.height = radius;
        this.widthDouble = radius * 2;
        this.heightDouble = radius * 2;
        this.colorCount = 11;
        this.levelCount = 4;
        this.random = new Random();
        this.generate(2000);

        this.snakesInterval = Observable.interval(100, MILLISECONDS).map(x -> "S" + x);
        //this.observablePosition.subscribe(this::onPositionChanged);
        this.snakeUpdate = this.snakesInterval.withLatestFrom(snakePositions.getObservableSnakes(),
                //(timer, snakesUpdate) -> Pair.of(this, snakesUpdate));
                (timer, snakesUpdate) -> snakesUpdate);
//
//        this.snakeUpdate.subscribe(update -> {
//            logger.info(String.format("%s", update));
//        });
    }

    public Observable<SnakesUpdate> getSnakeUpdate() {
        return snakeUpdate;
    }

    private void generate(int count) {
        for (int i = 0; i < count; i++) {
            final Dot dot = generateDot();
            dotTree = dotTree.add(dot, dot.getPoint());
            //dots.put(dot.getKey(), dot);
        }
    }

    private Dot generateDot() {
        Dot dot;
        while (true) {
            int x = random.nextInt(widthDouble) - width;
            int y = random.nextInt(heightDouble) - height;
            if (x * x + y * y > powerRadius) {
                continue;
            }
            int color = random.nextInt(colorCount);
            int level = random.nextInt(levelCount);
            dot = Dot.create(x, y, color, level);
            break;
        }
        return dot;
    }

/*    public static void main(String[] args) throws JsonProcessingException {
        final GameDataService dotService = new GameDataService(512, 512);
        dotService.generate(10);
        // System.out.println(dotService.dots);
        String all = dotService.getDotsUpdate();
        System.out.println(all);
    }*/

    public DotsUpdate getDotsUpdate(Player player, Point position) {
        Rectangle viewport = player.getViewport(position);
        //logger.info(String.format("Viewport: %s", viewport));
        Observable<Entry<Dot, Point>> search = dotTree.search(viewport);
        Observable<List<Dot>> list = search.map(entry -> entry.value()).toList().single();
        List<Dot> values = list.toBlocking().first();
        return new DotsUpdate(values);
    }

    public void processMessage(Message message, Player player) {
        if (message.getPlayerMoved() != null) {
            final long processingStart = System.nanoTime();
            final Point position = point(message.getPlayerMoved().getX(), message.getPlayerMoved().getY());
            player.getObservablePosition().onNext(position);

            snakePositions.update(player, message.getPlayerMoved());

            EatenFood eatenFood = eatFood(player, position);
            if (eatenFood.hasAnyFood()) {
                //logger.info(String.format("TODO: %s", jsonMsg));
                final long processingEnd = System.nanoTime();
                eatenFood.setTimeInfo(new TimeInfo(message.getPlayerMoved().getSent(), processingEnd - processingStart));
                String jsonMsg = MessageBuilder.create().setEatenFood(eatenFood).toJson();
                player.getWebSocket().send(jsonMsg);
            }
        } else if (message.getResize() != null) {
            //player.getObservablePosition().onNext(point(message.getPlayerMoved().getX(), message.getPlayerMoved().getY()));
            player.setViewSize(point(message.getResize().getWidth(), message.getResize().getHeight()));
            DotsUpdate response = getDotsUpdate(player, null);
            String jsonMsg = MessageBuilder.create().setDotsUpdate(response).toJson();
            //logger.info(String.format("TODO: %s", jsonMsg));
            player.getWebSocket().send(jsonMsg);

        } else {
            logger.info(String.format("%s",  message));
        }
    }

    private EatenFood eatFood(Player player, Point position) {
        if (position != null) {
            List<Dot> result = new ArrayList<>();
            Circle foodCircle = player.getEatingCircle(position);
            Observable<Entry<Dot, Point>> foodSearch = dotTree.search(foodCircle);
            foodSearch.forEach(food -> {
                //logger.info(String.format("Food is at: %s", food.value()));
                dotTree = dotTree.delete(food.value(), food.geometry());
                result.add(food.value());
            });
            return new EatenFood(result);
        } else {
            return EatenFood.empty();
        }
    }

    public void processPeriodicUpdate(Player player, Point position) {
        //logger.info(String.format("Player %s, update at position %s",  player.getId(), position));
        DotsUpdate response = getDotsUpdate(player, position);
        String jsonMsg = MessageBuilder.create().setDotsUpdate(response).toJson();
        //logger.info(String.format("TODO: %s", jsonMsg));
        player.getWebSocket().send(jsonMsg);
    }
}
