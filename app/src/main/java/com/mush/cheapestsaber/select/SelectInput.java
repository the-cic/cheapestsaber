package com.mush.cheapestsaber.select;

import android.view.MotionEvent;

import com.mush.cheapestsaber.common.StateInput;
import com.mush.cheapestsaber.game.Levels;
import com.mush.cheapestsaber.ui.UiActionDelegate;
import com.mush.cheapestsaber.ui.UiComponent;

/**
 * Created by mush on 10/07/2018.
 */
public class SelectInput implements StateInput, UiActionDelegate {

    private boolean pressedStart;

    public SelectMain main;

    public boolean getWasPressed() {
        boolean wasPressed = pressedStart;
        pressedStart = false;
        return wasPressed;
    }

    public void onTouchEvent(MotionEvent event) {
        main.panel.onTouchEvent(event);
    }

    @Override
    public void onUiAction(UiComponent component, String action, Object actionInfo) {
        if ("start".equals(action)) {
            pressedStart = true;
        }
        if ("select".equals(action)) {
            main.selectedLevel = (Levels.Level) actionInfo;
        }
    }
}
