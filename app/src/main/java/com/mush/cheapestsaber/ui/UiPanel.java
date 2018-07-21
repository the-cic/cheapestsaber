package com.mush.cheapestsaber.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import static com.mush.cheapestsaber.common.ColorPalette.PANEL;
import static com.mush.cheapestsaber.common.ColorPalette.opaque;

/**
 * Created by mush on 21/07/2018.
 */
public class UiPanel extends UiComponent {

    private float unitCornerRadius;
    private float trueCornerRadius;
    private Paint bgPaint;

    public UiPanel(RectF rect, float cornerRadius) {
        super(rect);
        unitCornerRadius = cornerRadius;
        bgPaint = new Paint();
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(opaque(PANEL));
    }

    public UiPanel(RectF rect) {
        this(rect, 0.05f);
    }


    @Override
    protected void render(Canvas canvas, RectF trueArea) {
        canvas.drawRoundRect(trueArea, trueCornerRadius, trueCornerRadius, bgPaint);
    }

    @Override
    protected void resize(float x0, float y0, int size) {
        super.resize(x0, y0, size);
        trueCornerRadius = unitCornerRadius * size;
    }

    public void setBackgroundColor(int color) {
        bgPaint.setColor(color);
    }

    public void setCornerRadius(float cornerRadius) {
        this.unitCornerRadius = cornerRadius;
    }
}