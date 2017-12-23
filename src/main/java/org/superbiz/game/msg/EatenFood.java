package org.superbiz.game.msg;

import org.superbiz.game.Dot;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;

public class EatenFood {
    private static final EatenFood EMPTY_INSTANCE = new EatenFood(new ArrayList<>());

    private Collection<Dot> dots;

    private TimeInfo timeInfo;

    public EatenFood(Collection<Dot> dots) {
        this.dots = dots;
    }

    public EatenFood() {
    }

    public Collection<Dot> getDots() {
        return dots;
    }

    public void setDots(Collection<Dot> dots) {
        this.dots = dots;
    }

    public TimeInfo getTimeInfo() {
        return timeInfo;
    }

    public void setTimeInfo(TimeInfo timeInfo) {
        this.timeInfo = timeInfo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EatenFood{");
        sb.append("dots=").append(dots);
        sb.append('}');
        return sb.toString();
    }

    public boolean hasAnyFood() {
        return this.dots != null && !this.dots.isEmpty();
    }

    public static EatenFood empty() {
        return EMPTY_INSTANCE;
    }
}
