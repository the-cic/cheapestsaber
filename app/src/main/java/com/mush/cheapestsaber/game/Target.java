package com.mush.cheapestsaber.game;

/**
 * Created by mush on 25/06/2018.
 */
public class Target {

    public interface ActivationDelegate {
        public void onBecameActive(Target target);
        public void onStoppedActive(Target target);
    }

    private double delayTime;
    private double startTime;
    private double duration;
    private double endTime;
    private double currentTimeStartOffset;
    private boolean active;
    private boolean wasActive;

    public int side;
    public int horizontal;
    public int vertical;
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
        setCurrentTime(0);
    }

    public Target setShape(int side, int horizontal, int vertical) {
        this.side = side;
        this.horizontal = horizontal;
        this.vertical = vertical;
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
}
