package com.mush.cheapestsaber.game;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mush on 25/06/2018.
 */
public class TargetSequence implements SequenceItem.ActivationDelegate {

    public interface SequenceDelegate {
        public void onSequenceFinished();

        public void onBecameActive(Target target);

        public void onStoppedActive(Target target);
    }

    private List<SequenceItem> itemList;
    private double timePosition;
    private List<SequenceItem> targetWindow;
    private int windowIndex;
    private double windowDuration;
    private boolean finished;

    public SequenceDelegate delegate;

    public TargetSequence() {
        itemList = new ArrayList<>();
        targetWindow = new ArrayList<>();
        reset();
    }

    public void addItem(SequenceItem item) {
        SequenceItem lastItem = getNextItem(itemList.size() - 1);
        if (lastItem == null) {
            item.setTimeFrom(0);
        } else {
            item.setTimeFrom(lastItem.getStartTime());
        }
        itemList.add(item);
        item.delegate = this;
    }

    public void reset() {
        Log.i("seq", "reset");
        timePosition = 0;
        windowIndex = 0;
        finished = false;

        for (SequenceItem item : itemList) {
            item.reset();
        }

        makeTargetWindow();
    }

    public void advance(double seconds) {
        timePosition += seconds;

        SequenceItem nextItem = getNextItem(windowIndex);
        if (nextItem == null) {
            if (!finished) {
                delegate.onSequenceFinished();
            }
            finished = true;
        } else {
            if (advanceWindowIndex(nextItem)) {
                makeTargetWindow();
            }
        }

        applyTimeToWindow();
    }

    private boolean advanceWindowIndex(SequenceItem firstItem) {
        SequenceItem nextItem = firstItem;
        boolean changed = false;
        while (nextItem != null && isItemTooOldForWindow(nextItem)) {
            windowIndex++;
            changed = true;
            nextItem = getNextItem(windowIndex);
        }
        return changed;
    }

    private boolean isItemTooOldForWindow(SequenceItem item) {
        if (item instanceof Target) {
            Target target = (Target) item;
            // stay .5 more seconds in window
            return target.getEndTime() < timePosition - 0.5;
        } else {
            // no duration, also no need to keep in window
            return item.getStartTime() < timePosition;
        }
    }

    private SequenceItem getNextItem(int index) {
        if (index >= 0 && itemList.size() > index) {
            return itemList.get(index);
        } else {
            return null;
        }
    }

    public void setWindowDuration(double windowDuration) {
        this.windowDuration = windowDuration;
        makeTargetWindow();
    }

    public void makeTargetWindow() {
        targetWindow.clear();
        double endTime = timePosition + windowDuration;
        int index = windowIndex;

        SequenceItem nextItem = getNextItem(index);
        while (nextItem != null && nextItem.getStartTime() < endTime) {
            targetWindow.add(nextItem);
            nextItem = getNextItem(index);
            index++;
        }
    }

    private void applyTimeToWindow() {
        for (SequenceItem target : targetWindow) {
            target.setCurrentTime(timePosition);
        }
    }

    public List<SequenceItem> getTargetWindow() {
        return targetWindow;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void onBecameActive(SequenceItem item) {
        if (item instanceof Target && delegate != null) {
            delegate.onBecameActive((Target) item);
        } else if (item instanceof SequenceSound) {
            Log.i("seq", "sound: " + ((SequenceSound) item).getText());
        }
    }

    @Override
    public void onStoppedActive(SequenceItem item) {
        if (item instanceof Target && delegate != null) {
            delegate.onStoppedActive((Target) item);
        }
    }
}
