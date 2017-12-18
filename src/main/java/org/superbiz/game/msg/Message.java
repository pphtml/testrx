package org.superbiz.game.msg;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.superbiz.ws.WSTest;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.superbiz.game.msg.JSONMapper.getJSONMapper;

public class Message {
    private static final Logger LOGGER = Logger.getLogger(Message.class.getName());

    private static final JSONMapper MAPPER = getJSONMapper();


    @JsonProperty(required = false)
    private Resize resize;
    @JsonProperty(required = false)
    private PlayerMoved playerMoved;
    @JsonProperty(required = false)
    private WorldInfo worldInfo;
    @JsonProperty(required = false)
    private DotsUpdate dotsUpdate;
    @JsonProperty(required = false)
    private EatenFood eatenFood;
    @JsonProperty(required = false)
    private SnakesUpdate snakesUpdate;

    public Resize getResize() {
        return resize;
    }

    public void setResize(Resize resize) {
        this.resize = resize;
    }

    public PlayerMoved getPlayerMoved() {
        return playerMoved;
    }

    public void setPlayerMoved(PlayerMoved playerMoved) {
        this.playerMoved = playerMoved;
    }

    public WorldInfo getWorldInfo() {
        return worldInfo;
    }

    public void setWorldInfo(WorldInfo worldInfo) {
        this.worldInfo = worldInfo;
    }

    public DotsUpdate getDotsUpdate() {
        return dotsUpdate;
    }

    public void setDotsUpdate(DotsUpdate dotsUpdate) {
        this.dotsUpdate = dotsUpdate;
    }

    public EatenFood getEatenFood() {
        return eatenFood;
    }

    public void setEatenFood(EatenFood eatenFood) {
        this.eatenFood = eatenFood;
    }

    public SnakesUpdate getSnakesUpdate() {
        return snakesUpdate;
    }

    public void setSnakesUpdate(SnakesUpdate snakesUpdate) {
        this.snakesUpdate = snakesUpdate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb.append("resize=").append(resize);
        sb.append(", playerMoved=").append(playerMoved);
        sb.append(", worldInfo=").append(worldInfo);
        sb.append(", dotsUpdate=").append(dotsUpdate);
        sb.append(", eatenFood=").append(eatenFood);
        sb.append(", snakesUpdate=").append(snakesUpdate);
        sb.append('}');
        return sb.toString();
    }

    public String toJson() {
        try {
            return MAPPER.writer().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
