package com.mush.cheapestsaber.score;

import android.graphics.RectF;

import com.mush.cheapestsaber.common.Button;
import com.mush.cheapestsaber.common.Label;
import com.mush.cheapestsaber.common.Panel;
import com.mush.cheapestsaber.common.StateMain;
import com.mush.cheapestsaber.game.GameMain;

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

    public Panel panel;
    public Button okButton;
    public Label hitsLabel;
    public Label missesLabel;
    public Label comboLabel;

    public ScoreMain(GameMain game, ScoreInput scoreInput) {
        input = scoreInput;
        input.main = this;

        panel = new Panel(new RectF(0.1f, 0.1f, 0.9f, 0.9f), 0.05f);
        okButton = new Button(new RectF(0.2f, 0.7f, 0.8f, 0.8f), 0.2f, "OK");

        hitsLabel = new Label(new RectF(0.2f, 0.2f, 0.8f, 0.3f),
                "Hit: " + game.getHitCount());
        missesLabel = new Label(new RectF(0.2f, 0.3f, 0.8f, 0.4f),
                "Missed: " + (game.getTotalCount() - game.getHitCount()));
        comboLabel = new Label(new RectF(0.2f, 0.4f, 0.8f, 0.5f),
                "Best combo: " + game.getMaxComboLength());

        okButton.delegate = input;
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
