package com.mush.cheapestsaber.select;

import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.mush.cheapestsaber.common.StateMain;
import com.mush.cheapestsaber.game.Levels;
import com.mush.cheapestsaber.ui.UiButton;
import com.mush.cheapestsaber.ui.UiLabel;
import com.mush.cheapestsaber.ui.UiPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mush on 10/07/2018.
 */
public class SelectMain implements StateMain {

    public interface SelectMainDelegate {
        public void onSelectFinished();
    }

    public static final String ACTION_START = "start";
    public static final String ACTION_SELECT_LEVEL = "selectLevel";
    public static final String ACTION_SELECT_DIFFICULTY = "selectDifficulty";

    private boolean ready = false;
    public float transition = 1;
    public float absTransitionSpeed = 0.05f;
    public float transitionSpeed = - absTransitionSpeed;
    private SelectInput input;
    private Levels levels;

    public UiPanel panel;
    public UiButton startButton;
    private UiLabel selectedLabel;
    private List<UiButton> levelButtons;
    private List<UiButton> difficultyButtons;

    public Levels.Level selectedLevel;
    public int selectedDifficulty = 0;

    public SelectMainDelegate delegate;

    public SelectMain(Levels levels, SelectInput selectInput) {
        input = selectInput;
        input.main = this;

        this.levels = levels;
        levelButtons = new ArrayList<>();
        difficultyButtons = new ArrayList<>();

        panel = new UiPanel(new RectF(0.1f, 0.1f, 0.9f, 0.9f), 0.02f);
        startButton = new UiButton(new RectF(0.1f, 0.6f, 0.7f, 0.7f), "Start");
        startButton.action = ACTION_START;
        startButton.delegate = input;
        selectedLabel = new UiLabel(new RectF(0.1f, 0.5f, 0.7f, 0.6f), "Lorem ipsum");
        selectedLabel.setTextSize(0.04f);

        panel.add(startButton);
        panel.add(selectedLabel);

        int index = 0;
        for (Levels.Level level : levels.levels) {
            UiButton button = new UiButton(new RectF(0.1f, 0.1f + index * 0.075f, 0.7f, 0.15f + index * 0.075f), level.title/* + ":" + level.difficulty*/);
            button.setCornerRadius(0.01f);
            button.setTextSize(0.04f);
            button.setTextAlign(Paint.Align.LEFT);
            button.delegate = input;
            button.action = ACTION_SELECT_LEVEL;
            button.actionInfo = level;
            panel.add(button);
            selectedLevel = level;
            levelButtons.add(button);
            index++;
        }

        float difButtonStep = 0.6f / 4;
        float difButtonWidth = difButtonStep * 0.95f;
        difButtonStep += (difButtonStep - difButtonWidth) / 4;
        for (int i = 0; i < 4; i++) {
            float x = 0.1f + i * difButtonStep;
            UiButton button = new UiButton(new RectF(x, 0.45f, x + difButtonWidth, 0.5f), "diff");
            button.setCornerRadius(0.01f);
            button.setTextSize(0.03f);
            button.action = ACTION_SELECT_DIFFICULTY;
            button.actionInfo = i;
            button.delegate = input;
            difficultyButtons.add(button);
            panel.add(button);
        }

        onSelectLevel(levelButtons.get(0), levels.levels.get(0));
    }

    public void onSelectLevel(UiButton button, Levels.Level level) {
        selectedLevel = level;
        for (UiButton b : levelButtons) {
            b.selected = b == button;
        }
        for (UiButton b : difficultyButtons) {
            b.visible = false;
        }
        int index = 0;
        int difficulty = -1;
        for (String difficultyName : level.difficultyNames) {
            if (index < 4) {
                UiButton diffButton = difficultyButtons.get(index);
                diffButton.setText(difficultyName);
                diffButton.visible = true;
                diffButton.selected = false;
                if ("Normal".equals(difficultyName)) {
                    difficulty = index;
                }
            }
            index++;
        }

        difficulty = difficulty >= 0 ? difficulty : 0;

        onSelectDifficulty(difficultyButtons.get(difficulty), difficulty);
    }

    public void onSelectDifficulty(UiButton button, int difficulty) {
        selectedDifficulty = difficulty;
        for (UiButton b : difficultyButtons) {
            b.selected = b == button;
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
        selectedLabel.text = selectedLevel.title + " - " + selectedLevel.difficultyNames[selectedDifficulty];
        if (input.getWasStartPressed()) {
            ready = true;
            transitionSpeed = absTransitionSpeed;
            startButton.enabled = false;
        }
    }

}
