package com.mush.cheapestsaber.common;

import android.graphics.Canvas;

/**
 * Created by mush on 11/07/2018.
 */
public abstract class StateRender {

    protected int screenWidth;
    protected int screenHeight;

    public abstract void draw(Canvas canvas);

    public void resize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }
}
