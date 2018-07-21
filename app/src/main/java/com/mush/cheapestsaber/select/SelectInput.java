package com.mush.cheapestsaber.select;

import android.view.MotionEvent;

import com.mush.cheapestsaber.common.StateInput;
import com.mush.cheapestsaber.game.Levels;
import com.mush.cheapestsaber.ui.UiActionDelegate;
import com.mush.cheapestsaber.ui.UiButton;
import com.mush.cheapestsaber.ui.UiComponent;

/**
 * Created by mush on 10/07/2018.
 */
public class SelectInput implements StateInput, UiActionDelegate {

    private boolean pressedStart;

    public SelectMain main;

    public boolean getWasStartPressed() {
        boolean wasPressed = pressedStart;
        pressedStart = false;
        return wasPressed;
    }

    public void onTouchEvent(MotionEvent event) {
        main.panel.onTouchEvent(event);
    }

    @Override
    public void onUiAction(UiComponent component, String action, Object actionInfo) {
        if (SelectMain.ACTION_START.equals(action)) {
            pressedStart = true;
        }
        if (SelectMain.ACTION_SELECT_LEVEL.equals(action)) {
//            main.selectedLevel = (Levels.Level) actionInfo;
            main.onSelectLevel((UiButton)component, (Levels.Level) actionInfo);
        }
        if (SelectMain.ACTION_SELECT_DIFFICULTY.equals(action)) {
//            main.selectedDifficulty = (Integer)actionInfo;
            main.onSelectDifficulty((UiButton) component, (Integer) actionInfo);
        }
    }
}
