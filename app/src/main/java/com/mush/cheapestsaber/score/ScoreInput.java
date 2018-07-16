package com.mush.cheapestsaber.score;

import android.view.MotionEvent;

import com.mush.cheapestsaber.common.Button;
import com.mush.cheapestsaber.common.StateInput;

/**
 * Created by mush on 10/07/2018.
 */
public class ScoreInput implements StateInput, Button.ButtonDelegate {

    private  boolean pressed = false;

    public ScoreMain main;

    public boolean getWasPressed() {
        boolean wasPressed = pressed;
        pressed = false;
        return wasPressed;
    }

    public void onTouchEvent(MotionEvent event) {
        main.uiElements.onTouchEvent(event);
    }

    @Override
    public void onButtonClicked(Button button, String action) {
        if ("ok".equals(action)) {
            pressed = true;
        }
    }

}
