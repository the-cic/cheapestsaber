package com.mush.cheapestsaber.game;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mush on 25/06/2018.
 */
public class TargetSequence {

    private List<Target> targetList;
    private double timePosition;
    private int nextIndex;
    private boolean finished;

    public TargetSequence() {
        targetList = new ArrayList<>();
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
    }

    public void reset() {
        Log.i("seq", "reset");
        timePosition = 0;
        nextIndex = 0;
        finished = false;
    }

    public void advance(double seconds) {
        timePosition += seconds;

        Target nextTarget = getNextTarget(nextIndex);
        if (nextTarget == null) {
            finished = true;
        } else {

            while (nextTarget != null && nextTarget.getEndTime() < timePosition) {
                nextIndex++;
                nextTarget = getNextTarget(nextIndex);
            }
        }
    }

    private Target getNextTarget(int index) {
        if (index >= 0 && targetList.size() > index) {
            return targetList.get(index);
        } else {
            return null;
        }
    }

    public List<Target> getWindow(double duration) {
        List<Target> window = new ArrayList<>();
        double endTime = timePosition + duration;
        int index = nextIndex;

        Target nextTarget = getNextTarget(index);
        while (nextTarget != null && nextTarget.getStartTime() < endTime) {
            window.add(nextTarget);
            nextTarget = getNextTarget(index);
            index++;
        }

        for (Target target : window) {
            target.setCurrentTime(timePosition);
        }
        return window;
    }

    public boolean isFinished() {
        return finished;
    }
}
