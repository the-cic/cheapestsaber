package com.mush.cheapestsaber.common;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
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

    private boolean pressed;

    private Label label;

    private int color;
    private int pressedColor;
    private int textColor;
    private int disabledTextColor;

    public ButtonDelegate delegate;
    public boolean enabled = true;

    public Button(RectF rect, float corner, String text) {
        super(rect, corner);

        label = new Label(rect, text);
        label.setAlign(Paint.Align.CENTER);

        color = opaque(BUTTON);
        pressedColor = opaque(BUTTON_PRESSED);
        textColor = opaque(TEXT);
        disabledTextColor = opaque(TEXT_DISABLED);

        pressed = false;
    }

    @Override
    public void draw(Canvas canvas) {
        super.setPanelColor(pressed ? pressedColor : color);
        super.draw(canvas);

        label.setColor(enabled ? textColor : disabledTextColor);
        label.draw(canvas);
    }

    @Override
    protected void resize(int x0, int y0, int dim) {
        super.resize(x0, y0, dim);
        label.resize(x0, y0, dim);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!enabled) {
            return false;
        }
        if (getDrawArea().contains(event.getX(), event.getY())) {
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
