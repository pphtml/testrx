package org.superbiz.game.msg;

import org.superbiz.game.Dot;

import java.util.Collection;

public class DotsUpdate {
    private Collection<Dot> dots;

    public DotsUpdate(Collection<Dot> dots) {
        this.dots = dots;
    }

    public DotsUpdate() {
    }

    public Collection<Dot> getDots() {
        return dots;
    }

    public void setDots(Collection<Dot> dots) {
        this.dots = dots;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DotsUpdate{");
        sb.append("dots=").append(dots);
        sb.append('}');
        return sb.toString();
    }
}
