package com.mush.cheapestsaber.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.mush.cheapestsaber.common.ColorPalette;

/**
 * Created by mush on 21/07/2018.
 */
public class UiLabel extends UiComponent {

    public String text;
    private float unitTextSize;
    private float trueTextSize;
    private Paint textPaint;
    private boolean margin = false;

    public UiLabel(RectF rect, String text, float textSize) {
        super(rect);
        this.text = text;

        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(ColorPalette.opaque(ColorPalette.TEXT));
        Typeface typeface = Typeface.create("sans-serif", Typeface.BOLD);
        textPaint.setTypeface(typeface);
        unitTextSize = textSize;
        setTextAlign(Paint.Align.LEFT);
    }

    public UiLabel(RectF rect, String text) {
        this(rect, text, 0.07f);
    }

    @Override
    protected void render(Canvas canvas, RectF trueArea) {
        textPaint.setTextSize(trueTextSize);
        float textHeight = textPaint.descent() + textPaint.ascent();

        float drawX = textPaint.getTextAlign() == Paint.Align.CENTER
                ? trueArea.centerX()
                : trueArea.left + (margin ? trueTextSize / 2 : 0);

        canvas.drawText(text, drawX, trueArea.centerY() - textHeight / 2, textPaint);
    }

    @Override
    protected void resize(float x0, float y0, int size) {
        super.resize(x0, y0, size);
        trueTextSize = unitTextSize * size;
    }

    public void setTextSize(float textSize) {
        this.unitTextSize = textSize;
    }

    public void setTextAlign(Paint.Align textAlign) {
        textPaint.setTextAlign(textAlign);
    }

    public void setColor(int color) {
        textPaint.setColor(color);
    }

    public void setMargin(boolean margin) {
        this.margin = margin;
    }

}