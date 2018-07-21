package com.mush.cheapestsaber.game;

import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.mush.cheapestsaber.common.StateInput;
import com.mush.cheapestsaber.ui.UiActionDelegate;
import com.mush.cheapestsaber.ui.UiComponent;

/**
 * Created by mush on 25/06/2018.
 */
public class GameInput implements StateInput, UiActionDelegate {

    private PointF leftPoint;
    private PointF rightPoint;
    private boolean leftPointValid;
    private boolean rightPointValid;

    private RectF inputArea;

    public GameMain main;
    private boolean quitPressed = false;

    public GameInput() {
        leftPoint = new PointF();
        rightPoint = new PointF();
    }

    public void onTouchEvent(MotionEvent event) {
        if (main.quitButton.onTouchEvent(event)) {
            return;
        }

        if (!inputArea.contains(event.getX(), event.getY())) {
            return;
        }

        for (int pointerIndex = 0; pointerIndex < event.getPointerCount(); pointerIndex++) {
            onTouchEventPointer(event.getAction(), pointerIndex, event.getX(pointerIndex), event.getY(pointerIndex));
        }
    }

    private int getPointerUpIndex(int action) {
        int index = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        if ((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP) {
            return index;
        }
        return -1;
    }

    private void onTouchEventPointer(int action, int index, float pointerX, float pointerY) {

        int pointerUpIndex = getPointerUpIndex(action);

        boolean pointerUp = pointerUpIndex == index;
        boolean up = action == MotionEvent.ACTION_UP;

        boolean pressed = !up && !pointerUp;
        boolean isLeftNotRight = pointerX < inputArea.centerX();

        PointF point = isLeftNotRight ? leftPoint : rightPoint;

        if (pressed) {
            float x = pointerX - inputArea.centerX();
            float y = pointerY - inputArea.centerY();
            // x / (w / 2)
            point.x = 2 * x / inputArea.width();
            point.y = 2 * y / inputArea.height();
        }

        if (isLeftNotRight) {
            point.x = point.x * 2 + 1;
            leftPointValid = pressed;
        } else {
            point.x = point.x * 2 - 1;
            rightPointValid = pressed;
        }
    }

    public void resize(RectF inputArea) {
        this.inputArea = inputArea;
    }

    public PointF getLeftPoint() {
        return leftPointValid ? leftPoint : null;
    }

    public PointF getRightPoint() {
        return rightPointValid ? rightPoint : null;
    }

    public boolean getWasQuitPressed() {
        boolean wasPressed = quitPressed;
        quitPressed = false;
        return wasPressed;
    }

    @Override
    public void onUiAction(UiComponent component, String action, Object actionInfo) {
        if ("quit".equals(action)) {
            quitPressed = true;
        }
    }
}
