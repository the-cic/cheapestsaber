package com.mush.cheapestsaber.score;

import android.graphics.RectF;

import com.mush.cheapestsaber.common.StateMain;
import com.mush.cheapestsaber.game.GameMain;
import com.mush.cheapestsaber.ui.UiButton;
import com.mush.cheapestsaber.ui.UiLabel;
import com.mush.cheapestsaber.ui.UiPanel;

import java.util.List;

/**
 * Created by mush on 10/07/2018.
 */
public class ScoreMain implements StateMain {

    public interface ScoreMainDelegate {
        public void onScoreFinished();
    }

    public ScoreMainDelegate delegate;

    private boolean finished = false;
    public float transition = 1;
    public float absTransitionSpeed = 0.05f;
    public float transitionSpeed = - absTransitionSpeed;
    private ScoreInput input;

    public UiPanel panel;
    private UiButton okButton;
    private UiLabel levelNameLabel;
    private UiLabel levelDifficultyLabel;
    private UiLabel hitsLabel;
    private UiLabel missesLabel;
    private UiLabel comboLabel;

    public ScoreMain(GameMain game, ScoreInput scoreInput) {
        input = scoreInput;
        input.main = this;

        panel = new UiPanel(new RectF(0.1f, 0.1f, 0.9f, 0.9f), 0.05f);
        okButton = new UiButton(new RectF(0.1f, 0.6f, 0.7f, 0.7f), "OK");
        okButton.setCornerRadius(0.2f);

        levelNameLabel = new UiLabel(new RectF(0.1f, 0.05f, 0.8f, 0.25f),
                game.getLevel().title);
        levelNameLabel.setTextSize(0.05f);
        levelDifficultyLabel = new UiLabel(new RectF(0.1f, 0.1f, 0.8f, 0.3f),
                game.getLevel().difficultyNames[game.getLevelDifficulty()]);
        levelDifficultyLabel.setTextSize(0.04f);

        hitsLabel = new UiLabel(new RectF(0.1f, 0.25f, 0.8f, 0.45f),
                "Hit: " + game.getHitCount());
        missesLabel = new UiLabel(new RectF(0.1f, 0.325f, 0.8f, 0.525f),
                "Missed: " + (game.getTotalCount() - game.getHitCount()));
        comboLabel = new UiLabel(new RectF(0.1f, 0.4f, 0.8f, 0.6f),
                "Best combo: " + game.getMaxComboLength());

        okButton.delegate = input;
        okButton.action = "ok";

        panel.add(okButton);
        panel.add(levelNameLabel);
        panel.add(levelDifficultyLabel);
        panel.add(hitsLabel);
        panel.add(missesLabel);
        panel.add(comboLabel);
    }

    public void update(double seconds) {
        transition += transitionSpeed;

        if (transition < 0) {
            transition = 0;
            transitionSpeed = 0;
        }

        if (finished) {
            if (transition > 1) {
                if (delegate != null) {
                    delegate.onScoreFinished();
                }
            }
        }
    }

    public void processInput() {
        if (finished) {
            return;
        }
        if (input.getWasPressed()) {
            finished = true;
            transitionSpeed = absTransitionSpeed;
            okButton.enabled = false;
        }
    }
}
