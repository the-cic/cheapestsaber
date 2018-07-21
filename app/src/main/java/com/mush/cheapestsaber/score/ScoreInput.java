package com.mush.cheapestsaber.score;

import android.view.MotionEvent;

import com.mush.cheapestsaber.common.StateInput;
import com.mush.cheapestsaber.ui.UiActionDelegate;
import com.mush.cheapestsaber.ui.UiComponent;

/**
 * Created by mush on 10/07/2018.
 */
public class ScoreInput implements StateInput, UiActionDelegate {

    private  boolean pressed = false;

    public ScoreMain main;

    public boolean getWasPressed() {
        boolean wasPressed = pressed;
        pressed = false;
        return wasPressed;
    }

    public void onTouchEvent(MotionEvent event) {
        main.panel.onTouchEvent(event);
    }

    @Override
    public void onUiAction(UiComponent component, String action, Object actionInfo) {
        if ("ok".equals(action)) {
            pressed = true;
        }
    }
}
