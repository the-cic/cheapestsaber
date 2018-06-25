package com.mush.cheapestsaber.game;

import android.graphics.PointF;
import android.util.Log;

import java.util.List;

/**
 * Created by mush on 25/06/2018.
 */
public class GameMain {

    private PointF leftPoint;
    private PointF rightPoint;
    private TargetSequence targetSequence;

    public GameMain() {
        targetSequence = new TargetSequence();

        targetSequence.addTarget(new Target(1.0, 0.25).setShape(-1, 1, 0).setOffset(-1, 0));
        targetSequence.addTarget(new Target(1.0, 0.25).setShape( 1, 1, 0).setOffset( 1, 1));
        targetSequence.addTarget(new Target(1.0, 0.25).setShape(-1, -1, 0).setOffset(-1, 0));
        targetSequence.addTarget(new Target(2.0, 0.25).setShape(-1, 0, 1).setOffset(-1, -1));
        targetSequence.addTarget(new Target(1.0, 0.25).setShape( 1, -1, 0).setOffset(-1, 0));
        targetSequence.addTarget(new Target(1.0, 0.25).setShape(-1, 0, -1).setOffset( 1, 0));
    }

    public void processInput(GameInput input) {
        leftPoint = input.getLeftPoint();
        rightPoint = input.getRightPoint();
    }

    public void update(double secondsPerFrame) {
        targetSequence.advance(secondsPerFrame);
        if (targetSequence.isFinished()) {
            targetSequence.reset();
        }
    }

    public PointF getLeftPoint() {
        return leftPoint;
    }

    public PointF getRightPoint() {
        return rightPoint;
    }

    public List<Target> getTargetWindow(double duration) {
        return targetSequence.getWindow(duration);
    }
}
