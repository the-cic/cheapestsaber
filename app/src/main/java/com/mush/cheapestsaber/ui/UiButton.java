package com.mush.cheapestsaber.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import static com.mush.cheapestsaber.common.ColorPalette.BUTTON;
import static com.mush.cheapestsaber.common.ColorPalette.BUTTON_PRESSED;
import static com.mush.cheapestsaber.common.ColorPalette.BUTTON_SELECTED;
import static com.mush.cheapestsaber.common.ColorPalette.TEXT;
import static com.mush.cheapestsaber.common.ColorPalette.TEXT_DISABLED;
import static com.mush.cheapestsaber.common.ColorPalette.TEXT_SELECTED;
import static com.mush.cheapestsaber.common.ColorPalette.opaque;

/**
 * Created by mush on 21/07/2018.
 */
public class UiButton extends UiPanel {

    private boolean pressed;

    private UiLabel label;

    private int color;
    private int pressedColor;
    private int selectedColor;
    private int textColor;
    private int disabledTextColor;
    private int selectedTextColor;

    public UiActionDelegate delegate;
    public String action = null;
    public Object actionInfo = null;
    public boolean selected;

    public UiButton(RectF rect, String text) {
        super(rect);

        label = new UiLabel(new RectF(0, 0, rect.right - rect.left, rect.bottom - rect.top), text);
        label.setTextAlign(Paint.Align.CENTER);

        add(label);

        color = opaque(BUTTON);
        pressedColor = opaque(BUTTON_PRESSED);
        selectedColor = opaque(BUTTON_SELECTED);
        textColor = opaque(TEXT);
        disabledTextColor = opaque(TEXT_DISABLED);
        selectedTextColor = opaque(TEXT_SELECTED);

        pressed = false;
    }

    private int getBackgroundColor() {
        if (selected) {
            return selectedColor;
        }
        return pressed ? pressedColor : color;
    }

    private int getLabelColor() {
        if (selected) {
            return selectedTextColor;
        }
        return enabled ? textColor : disabledTextColor;
    }

    @Override
    protected void render(Canvas canvas, RectF trueArea) {
        super.setBackgroundColor(getBackgroundColor());
        label.setColor(getLabelColor());

        super.render(canvas, trueArea);
    }

    @Override
    public void onTouchThisEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            pressed = true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            boolean clicked = pressed;
            pressed = false;
            if (clicked && delegate != null) {
                delegate.onUiAction(this, action, actionInfo);
            }
        }
    }

    @Override
    public void onTouchElsewhereEvent(MotionEvent event) {
        pressed = false;
    }

    public void setText(String text) {
        label.text = text;
    }

    public void setTextSize(float size) {
        label.setTextSize(size);
    }

    public void setTextAlign(Paint.Align align) {
        label.setTextAlign(align);
        label.setMargin(align != Paint.Align.CENTER);
    }

}