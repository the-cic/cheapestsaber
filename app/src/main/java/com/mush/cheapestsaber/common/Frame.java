package com.mush.cheapestsaber.common;

import android.graphics.RectF;

/**
 * Created by mush on 12/07/2018.
 */
public class Frame {

    public enum FrameAlign {
        TOP,
        MIDDLE,
        BOTTOM
    }

    public FrameAlign frameAlign = FrameAlign.MIDDLE;

    private RectF area;
    private RectF drawArea;

    public Frame(RectF rect) {
        this.area = rect;
        this.drawArea = new RectF(area);
    }

    public RectF getDrawArea() {
        return drawArea;
    }

    public void resize(int screenWidth, int screenHeight) {
        int dim = Math.min(screenWidth, screenHeight);

        int x0 = 0;
        int y0 = 0;
        switch (frameAlign) {
            case MIDDLE:
                y0 = screenHeight / 2 - dim / 2;
                break;
            case BOTTOM:
                y0 = screenHeight - dim;
                break;
        }

        resize(x0, y0, dim);
    }

    protected void resize(int x0, int y0, int dim) {
        drawArea.set(x0 + dim * area.left, y0 + dim * area.top, x0 + dim * area.right, y0 + dim * area.bottom);
    }

}
