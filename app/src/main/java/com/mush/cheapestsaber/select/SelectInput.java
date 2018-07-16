package com.mush.cheapestsaber.select;

import android.view.MotionEvent;

import com.mush.cheapestsaber.common.Button;
import com.mush.cheapestsaber.common.StateInput;
import com.mush.cheapestsaber.game.Levels;

/**
 * Created by mush on 10/07/2018.
 */
public class SelectInput implements StateInput, Button.ButtonDelegate {

    private boolean pressedStart;

    public SelectMain main;

    public boolean getWasPressed() {
        boolean wasPressed = pressedStart;
        pressedStart = false;
        return wasPressed;
    }

    public void onTouchEvent(MotionEvent event) {
        main.uiElements.onTouchEvent(event);
    }

    @Override
    public void onButtonClicked(Button button, String action) {
        if ("start".equals(action)) {
            pressedStart = true;
        }
        if ("select".equals(action)) {
            main.selectedLevel = (Levels.Level) button.value;
        }
    }
}
