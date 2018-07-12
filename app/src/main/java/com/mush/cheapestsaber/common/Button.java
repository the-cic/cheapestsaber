package com.mush.cheapestsaber.common;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.MotionEvent;

import static com.mush.cheapestsaber.common.ColorPalette.BUTTON;
import static com.mush.cheapestsaber.common.ColorPalette.BUTTON_PRESSED;
import static com.mush.cheapestsaber.common.ColorPalette.TEXT;
import static com.mush.cheapestsaber.common.ColorPalette.TEXT_DISABLED;
import static com.mush.cheapestsaber.common.ColorPalette.opaque;

/**
 * Created by mush on 12/07/2018.
 */
public class Button extends Panel {

    public interface ButtonDelegate {
        public void onButtonClicked(Button button);
    }

    private Paint textPaint;
    private float textSize;
    private float drawTextSize;
    private boolean pressed;

    private int color;
    private int pressedColor;
    private int textColor;
    private int disabledTextColor;

    public String text;
    public ButtonDelegate delegate;
    public boolean enabled = true;

    public Button(RectF rect, String text) {
        super(rect);
        this.text = text;

        color = opaque(BUTTON);
        pressedColor = opaque(BUTTON_PRESSED);
        textColor = opaque(TEXT);
        disabledTextColor = opaque(TEXT_DISABLED);

        paint.setColor(color);

        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        Typeface typeface = Typeface.create("sans-serif", Typeface.BOLD);
        textPaint.setTypeface(typeface);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textSize = 7f / 100;

        pressed = false;
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setColor(pressed ? pressedColor : color);
        super.draw(canvas);

        textPaint.setColor(enabled ? textColor : disabledTextColor);
        textPaint.setTextSize(drawTextSize);
        float textHeight = textPaint.descent() + textPaint.ascent();

        canvas.drawText(text, drawArea.centerX(), drawArea.centerY() - textHeight / 2, textPaint);
    }

    @Override
    protected void resize(int x0, int y0, int dim) {
        super.resize(x0, y0, dim);
        drawTextSize = textSize * dim;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!enabled) {
            return false;
        }
        if (drawArea.contains(event.getX(), event.getY())) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                pressed = true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                boolean clicked = pressed;
                pressed = false;
                if (clicked && delegate != null) {
                    delegate.onButtonClicked(this);
                }
            }
            return true;
        } else {
            pressed = false;
            return false;
        }
    }
}
