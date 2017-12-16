package org.superbiz.game.msg;

public class MessageBuilder {
    private final Message message;
    private EatenFood eatenFood;

    private MessageBuilder() {
        this.message = new Message();
    }

    public static MessageBuilder create() {
        return new MessageBuilder();
    }

    public String toJson() {
        return this.build().toJson();
    }

    private Message build() {
        return this.message;
    }

    public MessageBuilder setWorldInfo(WorldInfo worldInfo) {
        this.message.setWorldInfo(worldInfo);
        return this;
    }

    public MessageBuilder setDotsUpdate(DotsUpdate dotsUpdate) {
        this.message.setDotsUpdate(dotsUpdate);
        return this;
    }

    public MessageBuilder setEatenFood(EatenFood eatenFood) {
        this.message.setEatenFood(eatenFood);
        return this;
    }
}
