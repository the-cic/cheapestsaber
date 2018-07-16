package com.mush.cheapestsaber.select;

import android.graphics.Paint;
import android.graphics.RectF;

import com.mush.cheapestsaber.common.Button;
import com.mush.cheapestsaber.common.Label;
import com.mush.cheapestsaber.common.Panel;
import com.mush.cheapestsaber.common.StateMain;
import com.mush.cheapestsaber.common.UIElements;
import com.mush.cheapestsaber.game.Levels;

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

    public Panel panel;
    public Button startButton;
    private Label selectedLabel;
    public Levels.Level selectedLevel;

    public UIElements uiElements;

    public SelectMainDelegate delegate;

    public SelectMain(Levels levels, SelectInput selectInput) {
        input = selectInput;
        input.main = this;

        this.levels = levels;

        uiElements = new UIElements();

        panel = new Panel(new RectF(0.1f, 0.1f, 0.9f, 0.9f), 0.02f);
        startButton = new Button(new RectF(0.2f, 0.7f, 0.8f, 0.8f), 0.2f, "Start");
        startButton.action = "start";
        startButton.delegate = input;
        selectedLabel = new Label(new RectF(0.2f, 0.6f, 0.8f, 0.7f), "Lorem ipsum");
        selectedLabel.textSize = 0.04f;

        uiElements.add(panel);
        uiElements.add(startButton);
        uiElements.add(selectedLabel);

        int i = 0;
        for (Levels.Level level : levels.levels) {
            Button button = new Button(new RectF(0.2f, 0.2f + i * 0.075f, 0.8f, 0.25f + i * 0.075f), 0.01f, level.title + ":" + level.difficulty);
            button.setTextSize(0.04f);
            button.setTextAlign(Paint.Align.LEFT);
            button.delegate = input;
            button.action = "select";
            button.value = level;
            uiElements.add(button);
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
