package com.mush.cheapestsaber.game;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Set<Target> activeTargets;

    private int comboLength;
    private int hitCount;
    private int totalCount;

    public GameMain() {
        targetSequence = new TargetSequence();
        targetSequence.delegate = this;
        targetSequence.setWindowDuration(targetWindowDuration);

        createSequence();

        leftTool = new Tool();
        rightTool = new Tool();
        activeTargets = new HashSet<>();
    }

    private void createSequence() {
        targetSequence.addTarget(new Target(1.0, 0.25).setShape(Target.SIDE_LEFT, 1, 0).setOffset(-1, 0));
        targetSequence.addTarget(new Target(1.0, 0.25).setShape(Target.SIDE_RIGHT, 1, 0).setOffset( 1, 1));
        targetSequence.addTarget(new Target(1.0, 0.25).setShape(Target.SIDE_LEFT, -1, 0).setOffset(-1, 0));
        targetSequence.addTarget(new Target(2.0, 0.25).setShape(Target.SIDE_LEFT, 1, 0).setOffset(-1, -1));
        targetSequence.addTarget(new Target(1.0, 0.25).setShape(Target.SIDE_RIGHT, -1, 0).setOffset(-1, 0));
        targetSequence.addTarget(new Target(1.0, 0.25).setShape(Target.SIDE_LEFT, 0, -1).setOffset( 1, 0));
    }

    public void processInput(GameInput input, double secondsPerFrame) {
        leftPoint = input.getLeftPoint();
        rightPoint = input.getRightPoint();
        leftTool.update(leftPoint, secondsPerFrame);
        rightTool.update(rightPoint, secondsPerFrame);
    }

    public void update(double secondsPerFrame) {
        List<Target> removeTargets = new ArrayList<>();
        for (Target target : activeTargets) {
            Tool tool = getToolForTarget(target);
            if (toolMatchesTarget(tool, target)) {
                removeTargets.add(target);
            }
        }
        for (Target target : removeTargets) {
            activeTargets.remove(target);
            onHit(target);
        }

        targetSequence.advance(secondsPerFrame);
        if (targetSequence.isFinished()) {
            reset();
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

    public int getTotalCount() {
        return totalCount;
    }

    public int getHitCount() {
        return hitCount;
    }

    public int getComboLength() {
        return comboLength;
    }

    public void reset() {
        targetSequence.reset();
        totalCount = 0;
        hitCount = 0;
        comboLength = 0;
    }

    @Override
    public void onSequenceFinished() {
        Log.i("main", "finished");
    }

    @Override
    public void onBecameActive(Target target) {
  //      Log.i("main", "became active: "+target);
        Tool tool = getToolForTarget(target);
        if (tool != null) {
            tool.resetStartPoint();
        }
        activeTargets.add(target);
    }

    @Override
    public void onStoppedActive(Target target) {
//        Log.i("main", "stopped active: "+target);
        boolean wasActive = activeTargets.remove(target);
        if (wasActive) {
            onMiss(target);
        }
    }

    private Tool getToolForTarget(Target target) {
        int side = target.getSide();
        Tool tool = side == Target.SIDE_LEFT ? leftTool : null;
        tool = side == Target.SIDE_RIGHT ? rightTool : tool;
        return tool;
    }

    private boolean toolMatchesTarget(Tool tool, Target target) {
        Point toolDirection = tool.getDirection();
        Point targetDirection = target.getDirection();
        return toolDirection.equals(targetDirection);
    }

    private void onHit(Target target) {
        target.setHit();
        Log.i("main", "HIT!");
        totalCount++;
        hitCount++;
        comboLength++;
    }

    private void onMiss(Target target) {
        Log.i("main", "missed target");
        totalCount++;
        comboLength = 0;
    }

}
