package com.mush.cheapestsaber.score;

import android.view.MotionEvent;

import com.mush.cheapestsaber.common.StateInput;

/**
 * Created by mush on 10/07/2018.
 */
public class ScoreInput implements StateInput {

    private  boolean pressed = false;
    private boolean dePressed = false;

    public boolean getWasPressed() {
        boolean wasPressed = dePressed;
        dePressed = false;
        return wasPressed;
    }

    public void onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            pressed = true;
        }
        if (pressed && event.getAction() == MotionEvent.ACTION_UP) {
            dePressed = true;
        }
    }
}
