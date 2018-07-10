package com.mush.cheapestsaber.select;

import android.view.MotionEvent;

/**
 * Created by mush on 10/07/2018.
 */
public class SelectInput {
    private boolean pressed;

    public boolean getWasPressed() {
        boolean wasPressed = pressed;
        pressed = false;
        return wasPressed;
    }

    public void onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            pressed = true;
        }
    }
}
