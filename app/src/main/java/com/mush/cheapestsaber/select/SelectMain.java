package com.mush.cheapestsaber.select;

import android.graphics.RectF;

import com.mush.cheapestsaber.common.Button;
import com.mush.cheapestsaber.common.Panel;
import com.mush.cheapestsaber.common.StateMain;

/**
 * Created by mush on 10/07/2018.
 */
public class SelectMain implements StateMain {

    public interface SelectMainDelegate {
        public void onSelectFinished();
    }

    private boolean ready = false;
    public float transition = 0;
    public final float transitionSpeed = 0.05f;
    private SelectInput input;

    public Panel panel;
    public Button startButton;

    public SelectMainDelegate delegate;

    public SelectMain(SelectInput selectInput) {
        input = selectInput;
        input.main = this;

        panel = new Panel(new RectF(0.1f, 0.1f, 0.9f, 0.9f));
        startButton = new Button(new RectF(0.2f, 0.7f, 0.8f, 0.8f), "Start");

        startButton.delegate = input;
    }

    public void update(double seconds) {
        if (ready) {
            transition += transitionSpeed;
            if (transition > 1) {
                if (delegate != null) {
                    delegate.onSelectFinished();
                }
            }
        }
    }

    public void processInput() {
        if (ready) {
            return;
        }
        if (input.getWasPressed()) {
            ready = true;
            startButton.enabled = false;
        }
    }

}
