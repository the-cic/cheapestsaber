package com.mush.cheapestsaber.game;

/**
 * Created by mush on 25/06/2018.
 */
public class Target {

    private double delayTime;
    private double startTime;
    private double duration;
    private double endTime;
    private double currentTimeStartOffset;
    private boolean currentGoal;

    public int side;
    public int horizontal;
    public int vertical;
    public int xOffset;
    public int yOffset;

    public Target(double delayTime, double duration) {
        this.delayTime = delayTime;
        this.duration = duration;
        setTimeFrom(0);
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
        currentGoal = startTime < currentTime && endTime > currentTime;
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

    public boolean isCurrentGoal() {
        return currentGoal;
    }
}
