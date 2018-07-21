package com.mush.cheapestsaber.ui;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mush on 19/07/2018.
 */
public abstract class UiComponent {

    public enum ScreenAlign {
        TOP_LEFT,
        MIDDLE,
        BOTTOM_RIGHT
    }

    private List<UiComponent> components;

    private RectF unitArea;
    private RectF trueArea;
    public ScreenAlign screenAlign = ScreenAlign.MIDDLE;
    public boolean enabled = true;
    public boolean visible = true;

    public UiComponent(RectF rect) {
        this.unitArea = new RectF(rect);
        this.trueArea = new RectF(unitArea);
        components = new ArrayList<>();
    }

    public void add(UiComponent component) {
        components.add(component);
    }

    public void draw(Canvas canvas) {
        if (!visible) {
            return;
        }

        render(canvas, trueArea);

        for (UiComponent component : components) {
            component.draw(canvas);
        }
    }

    protected abstract void render(Canvas canvas, RectF trueArea);

    public boolean onTouchEvent(MotionEvent event) {
        if (!enabled) {
            return false;
        }
        if (trueArea.contains(event.getX(), event.getY())) {
            onTouchThisEvent(event);
            for (UiComponent component : components) {
                if (component.onTouchEvent(event)) {
                    return true;
                }
            }
            return true;
        } else {
            onTouchElsewhereEvent(event);
            return false;
        }
    }

    public void onTouchThisEvent(MotionEvent event) {}
    public void onTouchElsewhereEvent(MotionEvent event) {}

    public void setScreenAlign(ScreenAlign screenAlign) {
        this.screenAlign = screenAlign;
    }

    /**
     * Called only on the first component
     *
     * @param screenWidth
     * @param screenHeight
     */
    public final void resize(int screenWidth, int screenHeight) {
        int size = Math.min(screenWidth, screenHeight);
        boolean vertical = screenHeight > screenWidth;

        int x0 = 0;
        int y0 = 0;
        switch (screenAlign) {
            case MIDDLE:
                if (vertical) {
                    y0 = screenHeight / 2 - size / 2;
                } else {
                    x0 = screenWidth / 2 - size / 2;
                }
                break;
            case BOTTOM_RIGHT:
                if (vertical) {
                    y0 = screenHeight - size;
                } else {
                    x0 = screenWidth - size;
                }
                break;
        }

        resize(x0, y0, size);
    }

    protected void resize(float x0, float y0, int size) {
        trueArea.set(
                x0 + size * unitArea.left,
                y0 + size * unitArea.top,
                x0 + size * unitArea.right,
                y0 + size * unitArea.bottom);

        for (UiComponent component : components) {
            component.resize(trueArea.left, trueArea.top, size);
        }
    }

    public RectF getTrueArea() {
        return trueArea;
    }
}
