package com.mush.cheapestsaber.common;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mush on 14/07/2018.
 */
public class UIElements {

    private List<Frame> elements;

    public UIElements() {
        elements = new ArrayList<>();
    }

    public void add(Frame element) {
        elements.add(element);
    }

    public Frame get(int i) {
        return elements.get(i);
    }

    public void resize(int width, int height) {
        for (Frame element : elements) {
            element.resize(width, height);
        }
    }

    public void draw(Canvas canvas) {
        for (Frame element : elements) {
            if (element instanceof DrawableUIElement) {
                ((DrawableUIElement) element).draw(canvas);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        for (Frame element : elements) {
            if (element instanceof InputUIElement) {
                boolean handled = ((InputUIElement) element).onTouchEvent(event);
                if (handled) {
                    return true;
                }
            }
        }
        return false;
    }


}
