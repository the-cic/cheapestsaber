package com.mush.cheapestsaber.common;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import static com.mush.cheapestsaber.common.ColorPalette.PANEL;
import static com.mush.cheapestsaber.common.ColorPalette.opaque;

/**
 * Created by mush on 12/07/2018.
 */
public class Panel {

    public enum Align {
        TOP,
        MIDDLE,
        BOTTOM
    }

    public RectF area;
    public RectF drawArea;
    public float corner;
    public float drawCorner;
    public Align align = Align.MIDDLE;

    protected Paint paint;

    public Panel(RectF rect) {
        this.area = rect;
        this.drawArea = new RectF(area);
        this.paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(opaque(PANEL));
        corner = 0.01f;
        resize(100, 100);
    }

    public void draw(Canvas canvas) {
        canvas.drawRoundRect(drawArea, drawCorner, drawCorner, paint);
    }

    public void setPanelColor(int color) {
        paint.setColor(color);
    }

    public void resize(int screenWidth, int screenHeight) {
        int dim = Math.min(screenWidth, screenHeight);

        int x0 = 0;
        int y0 = 0;
        switch (align) {
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
        drawCorner = corner * dim;
    }
}
