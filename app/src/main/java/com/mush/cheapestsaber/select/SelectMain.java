package com.mush.cheapestsaber.select;

/**
 * Created by mush on 10/07/2018.
 */
public class SelectMain {

    public interface SelectMainDelegate {
        public void onSelectFinished();
    }

    private double showDuration;
    private double minShowDuration = 2.0;

    private boolean ready = false;

    public SelectMainDelegate delegate;

    public SelectMain() {
        showDuration = 0;
    }

    public void update(double seconds) {
        showDuration += seconds;
        if (ready) {
            ready = false;
            if (delegate != null) {
                delegate.onSelectFinished();
            }
        }
    }

    public void processInput(SelectInput input) {
        if (showDuration < minShowDuration) {
            return;
        }
        if (input.getWasPressed()) {
            ready = true;
        }
    }

}
