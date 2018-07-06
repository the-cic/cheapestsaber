package com.mush.cheapestsaber.game;

import android.graphics.PointF;
import android.util.Log;

import java.util.List;

/**
 * Created by mush on 25/06/2018.
 */
public class GameMain implements TargetSequence.SequenceDelegate {

    private PointF leftPoint;
    private PointF rightPoint;
    private Tool leftTool;
    private Tool rightTool;
    private TargetSequence targetSequence;
    private double targetWindowDuration = 5.0;

    public GameMain() {
        targetSequence = new TargetSequence();
        targetSequence.delegate = this;
        targetSequence.setWindowDuration(targetWindowDuration);

        createSequence();

        leftTool = new Tool();
        rightTool = new Tool();
    }

    private void createSequence() {
        targetSequence.addTarget(new Target(1.0, 0.25).setShape(-1, 1, 0).setOffset(-1, 0));
        targetSequence.addTarget(new Target(1.0, 0.25).setShape( 1, 1, 0).setOffset( 1, 1));
        targetSequence.addTarget(new Target(1.0, 0.25).setShape(-1, -1, 0).setOffset(-1, 0));
        targetSequence.addTarget(new Target(2.0, 0.25).setShape(-1, 1, 0).setOffset(-1, -1));
        targetSequence.addTarget(new Target(1.0, 0.25).setShape( 1, -1, 0).setOffset(-1, 0));
        targetSequence.addTarget(new Target(1.0, 0.25).setShape(-1, 0, -1).setOffset( 1, 0));
    }

    public void processInput(GameInput input, double secondsPerFrame) {
        leftPoint = input.getLeftPoint();
        rightPoint = input.getRightPoint();
        leftTool.update(leftPoint, secondsPerFrame);
        rightTool.update(rightPoint, secondsPerFrame);
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

    public Tool getLeftTool() {
        return leftTool;
    }

    public Tool getRightTool() {
        return rightTool;
    }

    public double getTargetWindowDuration() {
        return targetWindowDuration;
    }

    public List<Target> getTargetWindow() {
        return targetSequence.getWindow();
    }

    @Override
    public void onSequenceFinished() {
        Log.i("main", "finished");
    }

    @Override
    public void onBecameActive(Target target) {
        Log.i("main", "became active: "+target);
    }

    @Override
    public void onStoppedActive(Target target) {
        Log.i("main", "stopped active: "+target);
    }
}
