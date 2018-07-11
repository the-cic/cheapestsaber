package com.mush.cheapestsaber.game;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import com.mush.cheapestsaber.R;
import com.mush.cheapestsaber.common.StateMain;
import com.mush.cheapestsaber.game.sequence.SequenceItem;
import com.mush.cheapestsaber.game.sequence.SequenceLoader;
import com.mush.cheapestsaber.game.sequence.SequenceSound;
import com.mush.cheapestsaber.game.sequence.Target;
import com.mush.cheapestsaber.game.sequence.TargetSequence;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mush on 25/06/2018.
 */
public class GameMain implements StateMain, TargetSequence.SequenceDelegate {

    private static final String TAG = GameMain.class.getSimpleName();

    public interface GameMainDelegate {
        public void onGameFinished();
    }

    private Context applicationContext;

    private PointF leftPoint;
    private PointF rightPoint;
    private Tool leftTool;
    private Tool rightTool;
    private TargetSequence targetSequence;
    private double targetWindowDuration = 2.0;
    private Set<Target> activeTargets;

    private int comboLength;
    private int maxComboLength;
    private int hitCount;
    private int totalCount;

    private boolean paused = true;
    private boolean soundEnabled = false;

    private SoundPlayer soundPlayer;
    private GameInput input;

    public GameMainDelegate delegate;

    public GameMain(Context appContext, GameInput gameInput) {
        applicationContext = appContext;

        soundPlayer = new SoundPlayer(appContext);
        input = gameInput;

        targetSequence = new TargetSequence();
        targetSequence.delegate = this;
        targetSequence.setWindowDuration(targetWindowDuration);

        createSequence();

        leftTool = new Tool();
        rightTool = new Tool();
        activeTargets = new HashSet<>();
    }

    private void createSequence() {
        SequenceLoader loader = new SequenceLoader(applicationContext, R.raw.sequence2);

        loader.parseInto(targetSequence);
    }

    public void processInput() {
        leftPoint = input.getLeftPoint();
        rightPoint = input.getRightPoint();
    }

    public void update(double secondsPerFrame) {
        if (paused) {
            return;
        }

        leftTool.update(leftPoint, secondsPerFrame);
        rightTool.update(rightPoint, secondsPerFrame);

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

        targetSequence.advance(/*secondsPerFrame*/);
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
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

    public List<SequenceItem> getTargetWindow() {
        return targetSequence.getTargetWindow();
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

    public int getMaxComboLength() {
        return maxComboLength;
    }

    private void reset() {
        targetSequence.reset();
        totalCount = 0;
        hitCount = 0;
        comboLength = 0;
        maxComboLength = 0;
    }

    @Override
    public void onSequenceFinished() {
        Log.i(TAG, "finished");
        if (delegate != null) {
            delegate.onGameFinished();
        }
    }

    @Override
    public void onBecameActive(Target target) {
  //      Log.i(TAG, "became active: "+target);
        Tool tool = getToolForTarget(target);
        if (tool != null) {
            tool.resetStartPoint();
        }
        activeTargets.add(target);
    }

    @Override
    public void onStoppedActive(Target target) {
//        Log.i(TAG, "stopped active: "+target);
        boolean wasActive = activeTargets.remove(target);
        if (wasActive) {
            onMiss(target);
        }
    }

    @Override
    public void onBecameActive(SequenceSound soundItem) {
//        Log.i(TAG, "sound: " + soundItem.getName());
        if (soundEnabled) {
            soundPlayer.play(soundItem.getName());
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
        Log.i(TAG, "HIT!");
        totalCount++;
        hitCount++;
        comboLength++;
        if (comboLength > maxComboLength) {
            maxComboLength = comboLength;
        }
    }

    private void onMiss(Target target) {
//        Log.i(TAG, "missed target");
        target.setMiss();
        totalCount++;
        comboLength = 0;
    }

}
