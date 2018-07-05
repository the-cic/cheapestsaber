package com.mush.cheapestsaber.game;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mush on 25/06/2018.
 */
public class TargetSequence implements Target.ActivationDelegate {

    public interface SequenceDelegate {
        public void onSequenceFinished();
        public void onBecameActive(Target target);
        public void onStoppedActive(Target target);
    }

    private List<Target> targetList;
    private double timePosition;
    private List<Target> window;
    private int windowIndex;
    private double windowDuration;
    private boolean finished;

    public SequenceDelegate delegate;

    public TargetSequence() {
        targetList = new ArrayList<>();
        window = new ArrayList<>();
        reset();
    }

    public void addTarget(Target target) {
        Target lastTarget = getNextTarget(targetList.size() - 1);
        if (lastTarget == null) {
            target.setTimeFrom(0);
        } else {
            target.setTimeFrom(lastTarget.getStartTime());
        }
        targetList.add(target);
        target.delegate = this;
    }

    public void reset() {
        Log.i("seq", "reset");
        timePosition = 0;
        windowIndex = 0;
        finished = false;

        for (Target target : targetList) {
            target.reset();
        }

        makeWindow();
    }

    public void advance(double seconds) {
        timePosition += seconds;

        Target nextTarget = getNextTarget(windowIndex);
        if (nextTarget == null) {
            if (!finished) {
                delegate.onSequenceFinished();
            }
            finished = true;
        } else {

            boolean changed = false;
            while (nextTarget != null && nextTarget.getEndTime() < timePosition - 0.5) {
                windowIndex++;
                changed = true;
                nextTarget = getNextTarget(windowIndex);
            }
            if (changed) {
                makeWindow();
            }
        }

        applyTimeToWindow();
    }

    private Target getNextTarget(int index) {
        if (index >= 0 && targetList.size() > index) {
            return targetList.get(index);
        } else {
            return null;
        }
    }

    public void setWindowDuration(double windowDuration) {
        this.windowDuration = windowDuration;
        makeWindow();
    }

    public void makeWindow() {
        window.clear();
        double endTime = timePosition + windowDuration;
        int index = windowIndex;

        Target nextTarget = getNextTarget(index);
        while (nextTarget != null && nextTarget.getStartTime() < endTime) {
            window.add(nextTarget);
            nextTarget = getNextTarget(index);
            index++;
        }
    }

    private void applyTimeToWindow(){
        for (Target target : window) {
            target.setCurrentTime(timePosition);
        }
    }

    public List<Target> getWindow() {
        return window;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void onBecameActive(Target target) {
        if (delegate != null) {
            delegate.onBecameActive(target);
        }
    }

    @Override
    public void onStoppedActive(Target target) {
        if (delegate != null) {
            delegate.onStoppedActive(target);
        }
    }
}
