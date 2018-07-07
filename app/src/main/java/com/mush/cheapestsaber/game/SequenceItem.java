package com.mush.cheapestsaber.game;

import android.graphics.Point;

/**
 * Created by mush on 07/07/2018.
 */
public class SequenceItem {

    public interface ActivationDelegate {
        public void onBecameActive(SequenceItem item);
        public void onStoppedActive(SequenceItem item);
    }

    private double delayTime;
    private double startTime;
    private double currentTimeStartOffset;
    private boolean active;
    private boolean wasActive;

    public SequenceItem.ActivationDelegate delegate;

    public SequenceItem(double delayTime) {
        this.delayTime = delayTime;
        setTimeFrom(0);
        reset();
    }

    public void reset() {
        this.active = false;
        this.wasActive = false;
        setCurrentTime(0);
    }

    public void setTimeFrom(double previousTime) {
        startTime = previousTime + delayTime;
    }

    protected boolean calculateActive(double currentTime) {
        return startTime < currentTime /*&& endTime > currentTime*/;
    }

    public void setCurrentTime(double currentTime) {
        currentTimeStartOffset = startTime - currentTime;
        active = calculateActive(currentTime);
        if (active != wasActive && delegate != null) {
            if (active) {
                delegate.onBecameActive(this);
            } else {
                delegate.onStoppedActive(this);
            }
        }
        wasActive = active;
    }

    public double getStartTime() {
        return startTime;
    }

    public double getCurrentTimeStartOffset() {
        return currentTimeStartOffset;
    }

    protected void clearActive() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

}
