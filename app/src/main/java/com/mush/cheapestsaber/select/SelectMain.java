package com.mush.cheapestsaber.select;

import com.mush.cheapestsaber.common.StateMain;

/**
 * Created by mush on 10/07/2018.
 */
public class SelectMain implements StateMain {

    public interface SelectMainDelegate {
        public void onSelectFinished();
    }

    private double showDuration;
    private double minShowDuration = 2.0;

    private boolean ready = false;
    private SelectInput input;

    public SelectMainDelegate delegate;

    public SelectMain(SelectInput selectInput) {
        showDuration = 0;
        input = selectInput;
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

    public void processInput() {
        if (showDuration < minShowDuration) {
            return;
        }
        if (input.getWasPressed()) {
            ready = true;
        }
    }

}
