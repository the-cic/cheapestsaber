package com.mush.cheapestsaber.select;

import android.graphics.RectF;

import com.mush.cheapestsaber.common.Button;
import com.mush.cheapestsaber.common.Label;
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
    public float transition = 1;
    public float absTransitionSpeed = 0.05f;
    public float transitionSpeed = - absTransitionSpeed;
    private SelectInput input;

    public Panel panel;
    public Button startButton;
    public Label label;

    public SelectMainDelegate delegate;

    public SelectMain(SelectInput selectInput) {
        input = selectInput;
        input.main = this;

        panel = new Panel(new RectF(0.1f, 0.1f, 0.9f, 0.9f), 0.02f);
        startButton = new Button(new RectF(0.2f, 0.7f, 0.8f, 0.8f), 0.2f, "Start");
        label = new Label(new RectF(0.2f, 0.3f, 0.8f, 0.4f), "Lorem ipsum");

        startButton.delegate = input;
    }

    public void update(double seconds) {
        transition += transitionSpeed;

        if (transition < 0) {
            transition = 0;
            transitionSpeed = 0;
        }

        if (ready) {
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
            transitionSpeed = absTransitionSpeed;
            startButton.enabled = false;
        }
    }

}
