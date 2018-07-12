package com.mush.cheapestsaber.select;

import android.view.MotionEvent;

import com.mush.cheapestsaber.common.Button;
import com.mush.cheapestsaber.common.StateInput;

/**
 * Created by mush on 10/07/2018.
 */
public class SelectInput implements StateInput, Button.ButtonDelegate {
    private boolean pressed;

    public SelectMain main;

    public boolean getWasPressed() {
        boolean wasPressed = pressed;
        pressed = false;
        return wasPressed;
    }

    public void onTouchEvent(MotionEvent event) {
        main.startButton.onTouchEvent(event);
    }

    @Override
    public void onButtonClicked(Button button) {
        if (button == main.startButton) {
            pressed = true;
        }
    }
}
