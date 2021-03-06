package com.mush.cheapestsaber.game.sequence;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mush on 25/06/2018.
 */
public class TargetSequence implements SequenceItem.ActivationDelegate {

    private final static String TAG = TargetSequence.class.getSimpleName();

    public interface SequenceDelegate {
        public void onSequenceFinished();

        public void onBecameActive(Target target);

        public void onBecameActive(SequenceSound soundItem);

        public void onStoppedActive(Target target);
    }

    private List<SequenceItem> itemList;
    private double timePosition;
    private List<SequenceItem> targetWindow;
    private int windowIndex;
    private double windowDuration;
    private boolean finished;

    private long startTime;
    private double realTimePosition;

    public SequenceDelegate delegate;

    public TargetSequence() {
        itemList = new ArrayList<>();
        targetWindow = new ArrayList<>();
        clear();
        reset();
    }

    public void clear(){
        itemList.clear();
        targetWindow.clear();
        reset();
    }

    public void addItem(SequenceItem item) {
        SequenceItem lastItem = getLastItem();
        if (lastItem == null) {
            item.setTimeFrom(0);
        } else {
            item.setTimeFrom(lastItem.getStartTime());
        }
        itemList.add(item);
        item.delegate = this;
    }

    public void reset() {
        Log.i(TAG, "reset");
        timePosition = 0;
        windowIndex = 0;
        finished = false;

        for (SequenceItem item : itemList) {
            item.reset();
        }

        makeTargetWindow();

        startTime = System.nanoTime();
        realTimePosition = 0;
    }

    public void advance(/*double seconds*/) {
        double lastRealTimePosition = realTimePosition;
        realTimePosition = (System.nanoTime() - startTime) / 1000000000.0;
        double realSeconds = realTimePosition - lastRealTimePosition;

        double seconds = realSeconds;

        timePosition += seconds;

//        Log.i(TAG, "timePosition: " + timePosition + " realTimePosition: " + realTimePosition + " diff: " + (realTimePosition - timePosition));
//        Log.i(TAG, "seconds: " + seconds + " realSeconds: " + realSeconds);

//        timePosition = realTimePosition;

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

    // TODO: this skips new items if items are sparse and spaced apart more than window size
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
            return item.getStartTime() < timePosition - 0.5;
        }
    }

    private SequenceItem getNextItem(int index) {
        if (index >= 0 && itemList.size() > index) {
            return itemList.get(index);
        } else {
            return null;
        }
    }

    public SequenceItem getLastItem() {
        return getNextItem(itemList.size() - 1);
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

        if (targetWindow.isEmpty()) {
            SequenceItem firstNextItem = getNextItem(windowIndex);
            if (firstNextItem != null) {
                targetWindow.add(firstNextItem);
            }
            SequenceItem nextNextItem = getNextItem(windowIndex + 1);
            if (nextNextItem != null) {
                targetWindow.add(nextNextItem);
            }
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
//        Log.i(TAG, "on became active: " + item);
        if (item instanceof Target && delegate != null) {
            delegate.onBecameActive((Target) item);
        } else if (item instanceof SequenceSound) {
            delegate.onBecameActive((SequenceSound) item);
        }
    }

    @Override
    public void onStoppedActive(SequenceItem item) {
        if (item instanceof Target && delegate != null) {
            delegate.onStoppedActive((Target) item);
        }
    }

    public void log() {
        for (SequenceItem item : itemList) {
            Log.i(TAG, item.getStartTime() + " : " + item.getClass());

        }
    }
}
