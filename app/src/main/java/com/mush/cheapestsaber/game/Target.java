package com.mush.cheapestsaber.game;

import android.graphics.Point;

/**
 * Created by mush on 25/06/2018.
 */
public class Target {

    public interface ActivationDelegate {
        public void onBecameActive(Target target);
        public void onStoppedActive(Target target);
    }

    public final static int SIDE_LEFT = -1;
    public final static int SIDE_RIGHT = 1;

    private double delayTime;
    private double startTime;
    private double duration;
    private double endTime;
    private double currentTimeStartOffset;
    private boolean active;
    private boolean wasActive;
    private boolean hit;

    private int side;
//    public int horizontalDirection;
//    public int verticalDirection;
    private Point direction;
    public int xOffset;
    public int yOffset;
    public ActivationDelegate delegate;

    public Target(double delayTime, double duration) {
        this.delayTime = delayTime;
        this.duration = duration;
        setTimeFrom(0);
        reset();
    }

    public void reset() {
        this.active = false;
        this.wasActive = false;
        this.hit = false;
        setCurrentTime(0);
    }

    public Target setShape(int side, int horizontal, int vertical) {
        this.side = side;
//        this.horizontalDirection = horizontal;
//        this.verticalDirection = vertical;
        this.direction = new Point(horizontal, vertical);
        return this;
    }

    public Target setOffset(int x, int y) {
        this.xOffset = x;
        this.yOffset = y;
        return this;
    }

    public void setTimeFrom(double previousTime) {
        startTime = previousTime + delayTime;
        endTime = startTime + duration;
    }

    public void setCurrentTime(double currentTime) {
        currentTimeStartOffset = startTime - currentTime;
        active = startTime < currentTime && endTime > currentTime;
        if (active != wasActive && delegate != null) {
            if (active) {
                delegate.onBecameActive(this);
            } else {
                delegate.onStoppedActive(this);
            }
        }
        wasActive = active;
    }

    public double getEndTime() {
        return endTime;
    }

    public double getStartTime() {
        return startTime;
    }

    public double getDuration() {
        return duration;
    }

    public double getCurrentTimeStartOffset() {
        return currentTimeStartOffset;
    }

    public boolean isActive() {
        return active;
    }

    public void setHit() {
        active = false;
        hit = true;
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
}
