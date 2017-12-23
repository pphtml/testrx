package org.superbiz.game.msg;

public class TimeInfo {
    private long initiated;
    private long processing;

    public TimeInfo(long initiated, long processing) {
        this.initiated = initiated;
        this.processing = processing;
    }

    public TimeInfo() {
    }

    public long getInitiated() {
        return initiated;
    }

    public void setInitiated(long initiated) {
        this.initiated = initiated;
    }

    public long getProcessing() {
        return processing;
    }

    public void setProcessing(long processing) {
        this.processing = processing;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TimeInfo{");
        sb.append("initiated=").append(initiated);
        sb.append(", processing=").append(processing);
        sb.append('}');
        return sb.toString();
    }
}
