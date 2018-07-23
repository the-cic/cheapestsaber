package com.mush.cheapestsaber.game.sequence;

import android.graphics.Point;

/**
 * Created by mush on 25/06/2018.
 */
public class Target extends SequenceItem {

    public final static int SIDE_LEFT = -1;
    public final static int SIDE_RIGHT = 1;

    private double duration;
    private double endTime;
    private boolean hit;
    private boolean miss;
    private double hitTime;

    private int side;
    private Point direction;
    private float xOffset;
    private float yOffset;

    public Target(double delayTime, double duration) {
        super(delayTime);
        this.duration = duration;
        setTimeFrom(0);
        reset();
    }

    public void reset() {
        super.reset();
        this.hit = false;
        this.miss = false;
        setCurrentTime(0);
    }

    public Target setShape(int side, int horizontal, int vertical) {
        this.side = side;
        this.direction = new Point(horizontal, vertical);
        return this;
    }

    public Target setOffset(float x, float y) {
        this.xOffset = x;
        this.yOffset = y;
        return this;
    }

    public void setTimeFrom(double previousTime) {
        super.setTimeFrom(previousTime);
        endTime = getStartTime() + duration;
    }

    protected boolean calculateActive(double currentTime) {
        return super.calculateActive(currentTime) && endTime > currentTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public double getDuration() {
        return duration;
    }

    public void setHit() {
        clearActive();
        hit = true;
        hitTime = getCurrentTimeStartOffset();
    }

    public double getHitTime() {
        return hitTime - getCurrentTimeStartOffset();
    }

    public void setMiss() {
        miss = true;
    }

    public Point getDirection() {
        return direction;
    }

    public int getSide() {
        return side;
    }

    public boolean isHit() {
        return hit;
    }

    public boolean isMiss() {
        return miss;
    }

    public float getxOffset() {
        return xOffset;
    }

    public float getyOffset() {
        return yOffset;
    }
}
