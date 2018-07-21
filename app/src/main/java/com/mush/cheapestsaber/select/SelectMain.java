package com.mush.cheapestsaber.select;

import android.graphics.Paint;
import android.graphics.RectF;

import com.mush.cheapestsaber.common.StateMain;
import com.mush.cheapestsaber.game.Levels;
import com.mush.cheapestsaber.ui.UiButton;
import com.mush.cheapestsaber.ui.UiLabel;
import com.mush.cheapestsaber.ui.UiPanel;

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
    private Levels levels;

    public UiPanel panel;
    public UiButton startButton;
    private UiLabel selectedLabel;
    public Levels.Level selectedLevel;

    public SelectMainDelegate delegate;

    public SelectMain(Levels levels, SelectInput selectInput) {
        input = selectInput;
        input.main = this;

        this.levels = levels;

        panel = new UiPanel(new RectF(0.1f, 0.1f, 0.9f, 0.9f), 0.02f);
        startButton = new UiButton(new RectF(0.1f, 0.6f, 0.7f, 0.7f), "Start");
        startButton.action = "start";
        startButton.delegate = input;
        selectedLabel = new UiLabel(new RectF(0.1f, 0.5f, 0.7f, 0.6f), "Lorem ipsum");
        selectedLabel.setTextSize(0.04f);

        panel.add(startButton);
        panel.add(selectedLabel);

        int i = 0;
        for (Levels.Level level : levels.levels) {
            UiButton button = new UiButton(new RectF(0.1f, 0.1f + i * 0.075f, 0.7f, 0.15f + i * 0.075f), level.title + ":" + level.difficulty);
            button.setCornerRadius(0.01f);
            button.setTextSize(0.04f);
            button.setTextAlign(Paint.Align.LEFT);
            button.delegate = input;
            button.action = "select";
            button.actionInfo = level;
            panel.add(button);
            selectedLevel = level;
            i++;
        }

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
        selectedLabel.text = selectedLevel.title + ":" + selectedLevel.difficulty;
        if (input.getWasPressed()) {
            ready = true;
            transitionSpeed = absTransitionSpeed;
            startButton.enabled = false;
        }
    }

}
