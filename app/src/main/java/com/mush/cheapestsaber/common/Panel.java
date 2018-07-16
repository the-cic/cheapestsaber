package com.mush.cheapestsaber.common;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import static com.mush.cheapestsaber.common.ColorPalette.PANEL;
import static com.mush.cheapestsaber.common.ColorPalette.opaque;

/**
 * Created by mush on 12/07/2018.
 */
public class Panel extends Frame implements DrawableUIElement {

    private float corner;
    private float drawCorner;
    private Paint panelPaint;

    public Panel(RectF rect, float corner) {
        super(rect);
        this.panelPaint = new Paint();
        panelPaint.setStyle(Paint.Style.FILL);
        panelPaint.setColor(opaque(PANEL));
        this.corner = corner;
    }

    public void draw(Canvas canvas) {
        canvas.drawRoundRect(getDrawArea(), drawCorner, drawCorner, panelPaint);
    }

    public void setPanelColor(int color) {
        panelPaint.setColor(color);
    }

    protected void resize(int x0, int y0, int dim) {
        super.resize(x0, y0, dim);
        drawCorner = corner * dim;
    }
}
