package com.mush.cheapestsaber.select;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.mush.cheapestsaber.common.ColorPalette;
import com.mush.cheapestsaber.common.Frame;
import com.mush.cheapestsaber.common.StateRender;
import com.mush.cheapestsaber.common.PaintPalette;


/**
 * Created by mush on 10/07/2018.
 */
public class SelectRender extends StateRender {

    private SelectMain main;
    private Paint transitionPaint;

    public SelectRender(SelectMain selectMain) {
        this.main = selectMain;
        transitionPaint = new Paint();
        transitionPaint.setStyle(Paint.Style.FILL);
    }

    public void draw(Canvas canvas) {
        main.uiElements.draw(canvas);

        if (main.transition > 0) {
            transitionPaint.setColor(ColorPalette.fade(ColorPalette.BACKGROUND, main.transition));
            canvas.drawRect(main.panel.getDrawArea(), transitionPaint);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        main.uiElements.resize(width, height);
    }
}
