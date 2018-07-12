package com.mush.cheapestsaber.common;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

/**
 * Created by mush on 12/07/2018.
 */
public class Label extends Frame {

    public String text;
    public float textSize;

    private Paint.Align align = Paint.Align.LEFT;
    private Paint textPaint;
    private float drawTextSize;

    public Label(RectF rect, String text) {
        super(rect);
        this.text = text;

        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(ColorPalette.opaque(ColorPalette.TEXT));
        Typeface typeface = Typeface.create("sans-serif", Typeface.BOLD);
        textPaint.setTypeface(typeface);
        textPaint.setTextAlign(align);
        textSize = 7f / 100;
    }

    public void setAlign(Paint.Align align) {
        this.align = align;
        textPaint.setTextAlign(align);
    }

    public void setColor(int color) {
        textPaint.setColor(color);
    }

    public void draw(Canvas canvas) {
        textPaint.setTextSize(drawTextSize);
        float textHeight = textPaint.descent() + textPaint.ascent();

        float drawX = align == Paint.Align.CENTER
                ? getDrawArea().centerX()
                : getDrawArea().left;

        canvas.drawText(text, drawX, getDrawArea().centerY() - textHeight / 2, textPaint);
    }

    @Override
    protected void resize(int x0, int y0, int dim) {
        super.resize(x0, y0, dim);
        drawTextSize = textSize * dim;
    }

}
