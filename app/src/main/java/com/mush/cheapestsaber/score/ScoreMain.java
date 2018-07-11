package com.mush.cheapestsaber.score;

import com.mush.cheapestsaber.common.StateMain;
import com.mush.cheapestsaber.game.GameMain;

/**
 * Created by mush on 10/07/2018.
 */
public class ScoreMain implements StateMain {

    public interface ScoreMainDelegate {
        public void onScoreFinished();
    }

    public int comboLength;
    public int maxComboLength;
    public int hitCount;
    public int totalCount;

    private double showDuration;
    private double minShowDuration = 2.0;

    public ScoreMainDelegate delegate;

    private boolean finished = false;
    private ScoreInput input;

    public ScoreMain(GameMain game, ScoreInput scoreInput) {
        showDuration = 0;

        comboLength = game.getComboLength();
        maxComboLength = game.getMaxComboLength();
        hitCount = game.getHitCount();
        totalCount = game.getTotalCount();

        input = scoreInput;
    }

    public void update(double seconds) {
        showDuration += seconds;
        if (finished) {
            finished = false;
            if (delegate != null) {
                delegate.onScoreFinished();
            }
        }
    }

    public void processInput() {
        if (showDuration < minShowDuration) {
            return;
        }
        if (input.getWasPressed()) {
            finished = true;
        }
    }
}
